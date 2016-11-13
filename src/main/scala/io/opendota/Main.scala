package io.opendota

import io.opendota.glicko.{Match, Team}

object Main extends App {
  val g = Team(1500, 200)
  val op1 = Team(1400, 30)
  val op2 = Team(1550, 100)
  val op3 = Team(1700, 300)
  val matches = Seq(Match(1, op1), Match(0, op2), Match(0, op3))
  val n: Team = Team.updateRating(g, matches)
  val tt: Team = Team.updateRating(op3, Seq(Match(1, g), Match(1, op2), Match(1, op1)))
  println(n.rating)
  println(n.rd)

  println(tt.rating)
  println(tt.rd)
}
