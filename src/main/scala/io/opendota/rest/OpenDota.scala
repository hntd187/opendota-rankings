package io.opendota.rest

import java.sql.Timestamp
import java.time.Instant

import io.opendota.sql.DB
import io.opendota.sql.schema._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.slf4j.{Logger, LoggerFactory}

import scalaj.http._

class OpenDota {
  private val logger: Logger = LoggerFactory.getLogger(getClass)
  private implicit val formats: Formats = DefaultFormats

  private val base: HttpRequest = Http("https://api.opendota.com/api/explorer/")
  private val leagueSql: String = "select * from leagues where tier = 'professional' or tier = 'premium' order by leagueid;"
  private val teamSql: String = "select * from teams order by team_id;"
  private val playerSql: String = s"select * from notable_players;"
  private val matchSql: String = "select m.match_id, m.start_time, m.leagueid, m.radiant_team_id, m.dire_team_id, m.radiant_win FROM matches m where leagueid=%d"

  def getLeagueMatches(l: League): Seq[Match] = {
    logger.info(s"Getting matches for: ${l.name} id: ${l.leagueid}")
    val json = base.param("sql", matchSql.format(l.leagueid))
    (parse(json.asString.body) \ "rows").extract[Seq[Match]]
  }

  final private def getRows[T](sql: String)(implicit t: Manifest[T]): Seq[T] = {
    val json: HttpRequest = base.param("sql", sql)
    logger.info(s"Running request: ${json.urlBuilder(json)}")
    (parse(json.asString.body) \ "rows").extract[Seq[T]]
  }

  def getLeagues: Seq[League] = getRows[League](leagueSql)
  def getTeams: Seq[Team] = getRows[Team](teamSql)
  def getPlayers: Seq[Player] = getRows[Player](playerSql)

}

object OpenDota {
  def apply(): OpenDota = new OpenDota()
}

object Runner extends App {
  val logger: Logger = LoggerFactory.getLogger(getClass)
  val od: OpenDota = OpenDota()
  //val leagues: Seq[League] = od.getLeagues
  //val players: Seq[Player] = od.getPlayers
  //val teams: Seq[Team] = od.getTeams


  val t: Seq[Team] = DB.teams

  t.foreach{ team =>
    logger.info(s"Creating initial Rating for ${team.name}")
    val defaultRanking = Rating(0, 1500, team.team_id, 200, 0.6, Timestamp.from(Instant.now()))
    DB.insertRating(defaultRanking)
  }

  /*
  leagues.foreach { l: League =>
    DB.insertLeague(l)
  }

  players.foreach { p =>
    DB.insertPlayer(p)
  }
  teams.foreach { t =>
    DB.insertTeam(t)
  }

  DB.close()
 */
}
