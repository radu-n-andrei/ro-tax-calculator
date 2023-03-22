package org.personal.projects
package model

sealed trait Rate {
  val key: String
}

case object Hourly extends Rate {
  override val key = "hourly"
}

case object Monthly extends Rate {
  override val key = "monthly"
}

object Rate {

  def fromString(rate: String): Either[String, Rate] = rate.toLowerCase match {
    case Hourly.key => Right(Hourly)
    case Monthly.key => Right(Monthly)
    case _ => Left(s"INCORRECT RATE: $rate")
  }

}

