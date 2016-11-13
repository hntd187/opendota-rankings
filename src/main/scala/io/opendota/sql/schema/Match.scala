package io.opendota.sql.schema

import scalikejdbc._

case class Match(match_id: Int, start_time: Int, leagueid: Int, radiant_team_id: Option[Int], dire_team_id: Option[Int], radiant_win: Option[Boolean])

case object Matches extends SQLSyntaxSupport[Match] {
  def apply(rs: WrappedResultSet): Match = {
    Match(rs.int("match_id"), rs.int("start_time"), rs.int("leagueid"), Some(rs.int("radiant_team_id")), Some(rs.int("dire_team_id")), Some(rs.boolean("radiant_win")))
  }
}
