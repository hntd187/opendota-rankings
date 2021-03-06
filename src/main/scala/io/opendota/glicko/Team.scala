package io.opendota.glicko

import org.slf4j.{Logger, LoggerFactory}

import scala.math.{E => e, _}

case class Team(rating: Double, rd: Double, sigma: Double = 0.06, tau: Double = 0.5) {
  protected def srating: Double = (rating - 1500) / Team.scale
  protected def srd: Double = rd / Team.scale
  private val mu: Double = Team.scaleN(rating - 1500)
  private val phi: Double = Team.scaleN(rd)
}

object Team {
  private val logger: Logger = LoggerFactory.getLogger(getClass)

  private val pi2: Double = 9.869604401089358

  var scale: Double = 173.7178
  var tau: Double = 0.06
  var epsilon: Double = 0.000001

  protected def scaleN(x: Double): Double = x / scale

  @inline final private def phiP(phi: Double, sigma: Double): Double = sqrt(pow(phi, 2) + pow(sigma, 2))
  @inline final private def g(rd: Double): Double = 1.0 / sqrt(1.0 + 3.0 * pow(rd, 2) / pi2)
  @inline final private def E(mu: Double, mu2: Double, phi: Double): Double = 1.0 / (1.0 + exp(-g(phi) * (mu - mu2)))
  @inline final private def f(x: Double, delta: Double, phi: Double, v: Double, a: Double): Double = {
    (pow(e, x) * (pow(delta, 2) - pow(phi, 2) - v - pow(e, x))) / (2 * pow(pow(phi, 2) + v + pow(e, x), 2)) - (x - a) / pow(tau, 2)
  }

  def updateRating(team: Team, matches: Seq[Match]): Team = {

    if (matches.isEmpty) {
      val phistar = sqrt(pow(team.phi, 2) + pow(team.sigma, 2))
      return team.copy(rd = scale * phistar)
    }

    var vsum: Double = 0.0
    var deltasum: Double = 0.0
    var mupsum: Double = 0.0

    matches.foreach { m =>
      val ee = E(team.mu, m.opponent.srating, m.opponent.srd)
      val gg = g(m.opponent.srd)
      vsum += (gg * gg * ee * (1 - ee))
      deltasum += gg * (m.outcome - ee)
      mupsum += gg * (m.outcome - ee)
    }

    val v: Double = 1.0 / vsum
    val delta: Double = v * deltasum

    logger.info(s"V is $v")
    logger.info(s"Delta is $delta")

    var a: Double = log(pow(team.sigma, 2))
    var b: Double = 0.0

    val d2: Double = pow(delta, 2)
    val p2: Double = pow(team.phi, 2)

    if (d2 > p2 + v) b = log(d2 - p2 - v)
    else {
      var k: Int = 1
      do {
        k += 1
        b = a - k * tau
      } while (f(a - k * tau, delta, team.phi, v, a) < 0)
    }

    var fa: Double = f(a, delta, team.phi, v, a)
    var fb: Double = f(b, delta, team.phi, v, a)

    while (abs(b - a) > epsilon) {
      val c: Double = a + (a - b) * fa / (fb - fa)
      val fc: Double = f(c, delta, team.phi, v, a)
      if (fc * fb < 0) {
        a = b
        fa = fb
      } else {
        fa = fa / 2
      }
      b = c
      fb = fc
      logger.info(s"f(a) is $fa")
      logger.info(s"f(b) is $fb")
      logger.info(s"f(c) is $fc")

    }

    val sigmaPrime: Double = pow(e, a / 2)
    val phiStar: Double = phiP(team.phi, team.sigma)
    val phiPrime: Double = 1.0 / sqrt((1.0 / pow(phiStar, 2)) + (1.0 / v))
    val muPrime: Double = team.mu + phiPrime * phiPrime * mupsum

    logger.info(s"Sigma Prime is $sigmaPrime")
    logger.info(s"Phi Star is $phiStar")
    logger.info(s"Phi Prime is $phiPrime")
    logger.info(s"Mu Prime is $muPrime")
    logger.info(s"A is $a")
    logger.info(s"B is $b")
    logger.info(s"Rating is now ${scale * muPrime + 1500}")
    logger.info(s"Rating Deviation is now ${scale * phiPrime}")
    logger.info(s"Mu is $muPrime")
    logger.info(s"Sigma is $sigmaPrime")

    Team(rating = scale * muPrime + 1500, rd = scale * phiPrime, sigma = sigmaPrime, tau = tau)
  }
}
