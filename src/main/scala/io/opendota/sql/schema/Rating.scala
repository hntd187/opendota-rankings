package io.opendota.sql.schema

import java.sql.Timestamp

import scalikejdbc._

case class Rating(ratingid: Int, rating: Int, team_id: Int, deviation: Double, sigma: Double, created: Timestamp)

case object Ratings extends SQLSyntaxSupport[Rating] {
  def apply(rs: WrappedResultSet): Rating = {
    Rating(rs.int("ratingid"), rs.int("rating"), rs.int("team_id"), rs.double("deviation"), rs.double("sigma"), rs.timestamp("created"))
  }
}
