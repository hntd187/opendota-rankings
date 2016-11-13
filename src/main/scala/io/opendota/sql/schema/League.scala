package io.opendota.sql.schema

import java.time._

import scalikejdbc._

case class League(leagueid: Int, ticket: String, banner: String, tier: String, name: String)

case class LeagueTimes(leagueid: Int, start: Long, end: Long) {
  val startDate: LocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(start), ZoneId.systemDefault())
  val endDate: LocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(end), ZoneId.systemDefault())
}

case object LeagueTimes {
  def apply(rs: WrappedResultSet): LeagueTimes = {
    LeagueTimes(rs.int("leagueid"), rs.long("min"), rs.long("max"))
  }
}

case object Leagues extends SQLSyntaxSupport[League] {
  def apply(rs: WrappedResultSet): League = {
    League(rs.int("leagueid"), rs.string("ticket"), rs.string("banner"), rs.string("tier"), rs.string("name"))
  }
}
