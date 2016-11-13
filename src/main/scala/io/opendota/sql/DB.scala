package io.opendota
package sql

import io.opendota.sql.schema._
import scalikejdbc._
import sqls._

object DB {
  Class.forName("org.postgresql.Driver")
  ConnectionPool.singleton("jdbc:postgresql://localhost/postgres", "postgres", "12345678")

  implicit val session: AutoSession = AutoSession
  private val (l, t, m, p, r) = (Leagues.syntax("l"), Teams.syntax("t"), Matches.syntax("m"), Players.syntax("p"), Ratings.syntax("r"))

  final private def doInsert(table: Any with SQLSyntaxSupport[_], values: Any*): Int =
    withSQL {
      insert.into(table).values(values).onConflictDoNothing()
    }.update.apply

  def insertLeague(l: League): Int = doInsert(Leagues, l.leagueid, l.ticket, l.banner, l.tier, l.name)
  def insertTeam(t: Team): Int = doInsert(Teams, t.team_id, t.name, t.tag)
  def insertPlayer(p: Player): Int = doInsert(Players, p.account_id, p.name, p.country_code, p.fantasy_role, p.team_id, p.team_name, p.team_tag, p.is_locked, p.is_pro, p.locked_until)
  def insertMatch(m: Match): Int = doInsert(Matches, m.match_id, m.start_time, m.leagueid, m.radiant_team_id, m.dire_team_id, m.radiant_win)

  def insertRating(r: Rating): Int = {
    val rr = Ratings.column
    withSQL {
      insert.into(Ratings).namedValues(rr.rating -> r.rating, rr.team_id -> r.team_id, rr.sigma -> r.sigma, rr.deviation -> r.deviation, rr.created -> r.created).onConflictDoNothing()
    }.update.apply
  }

  def leagues: Seq[League] =
    withSQL {
      select(l.*).from(Leagues as l)
    }.map(Leagues(_)).list.apply

  def teams: Seq[Team] =
    withSQL {
      select(t.*).from(Teams as t)
    }.map(Teams(_)).list.apply

  def leagueStartEnd: Seq[LeagueTimes] =
    withSQL {
      select(l.leagueid, max(m.start_time), min(m.start_time)).from(Matches as m).join(Leagues as l).on(l.leagueid, m.leagueid).groupBy(l.leagueid).orderBy(sqls"min").asc
    }.map(LeagueTimes(_)).list.apply

  def close(): Unit = session.close()
}

object DBTest extends App {
  val leagueTimes: Seq[LeagueTimes] = DB.leagueStartEnd
  leagueTimes.foreach { l =>
    println(s"Start: ${l.startDate}, End: ${l.endDate}")
  }
}
