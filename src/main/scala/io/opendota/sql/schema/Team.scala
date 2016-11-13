package io.opendota.sql.schema

import scalikejdbc._

case class Team(team_id: Int, name: String, tag: String)

case object Teams extends SQLSyntaxSupport[Team] {
  def apply(rs: WrappedResultSet): Team = {
    Team(rs.int("team_id"), rs.string("name"), rs.string("name"))
  }
}
