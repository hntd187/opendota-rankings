package io.opendota.sql.schema

import scalikejdbc._

case class Player(account_id: Int, name: String, country_code: String, fantasy_role: Int, team_id: Int, team_name: String, team_tag: String, is_locked: Boolean, is_pro: Boolean, locked_until: Int)

case object Players extends SQLSyntaxSupport[Player] {
  def apply(rs: WrappedResultSet): Player = {
    Player(
      rs.int("account_id"),
      rs.string("name"),
      rs.string("country_code"),
      rs.int("fantasy_role"),
      rs.int("team_id"),
      rs.string("team_name"),
      rs.string("team_tag"),
      rs.boolean("is_locked"),
      rs.boolean("is_pro"),
      rs.int("locked_until")
    )
  }
}
