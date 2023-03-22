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

  def fromString(rate: String): Either[Rate, String] = rate match {
    case Hourly.key => Left(Hourly)
    case Monthly.key => Left(Monthly)
    case _ => Right("unexpected rate type")
  }

}

