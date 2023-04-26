package org.personal.projects
package model

import cats.effect.IO

sealed trait Rate {
  val key: String

  def generatedRevenue(simulation: Simulation): IO[Revenue]
}

case object Hourly extends Rate {
  override val key = "hourly"

  override def generatedRevenue(simulation: Simulation): IO[Revenue] =
    IO(Revenue.fromOtherAmount(simulation.amount * simulation.workRate.hoursPerDay * 20, Euro))
}

case object Monthly extends Rate {
  override val key = "monthly"

  override def generatedRevenue(simulation: Simulation): IO[Revenue] =
    IO(Revenue.fromOtherAmount(simulation.amount, Euro))
}

object Rate {

  def fromString(rate: String): Either[String, Rate] = rate.toLowerCase match {
    case Hourly.key => Right(Hourly)
    case Monthly.key => Right(Monthly)
    case _ => Left(s"INCORRECT RATE: $rate")
  }

}

