package org.personal.projects
package model

sealed trait WorkRate {
  val hoursPerDay: Int

  val key: String
}

case object FullTime extends WorkRate {
  override val hoursPerDay: Int = 8

  override val key: String = "full_time"
}

case object PartTime extends WorkRate {
  override val hoursPerDay: Int = 4

  override val key: String = "part_time"
}

object WorkRate {

  def fromString(wRate: String): Either[String, WorkRate] = wRate.toLowerCase match {
    case FullTime.key => Right(FullTime)
    case PartTime.key => Right(PartTime)
    case _ => Left(s"INCORRECT WORK RATE: $wRate")
  }
}