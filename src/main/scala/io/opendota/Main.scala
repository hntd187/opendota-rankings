package io.opendota

import io.opendota.glicko.{Match, Team}

/**
  * Created by scarman on 11/7/16.
  */
object Main extends App {
  val g = Team(1500, 200)
  val op1 = Team(1400, 30)
  val op2 = Team(1550, 100)
  val op3 = Team(1700, 300)
  val matches = Seq(Match(1, op1), Match(0, op2), Match(0, op3))
  val n: Team = Team.updateRating(g, matches)
  println(n.rating)
  println(n.rd)
}
