package org.personal.projects
package model

sealed trait WorkRate {
  val hoursPerDay: Int
}

case object FullTime extends WorkRate {
  override val hoursPerDay: Int = 8
}

case object PartTime extends WorkRate {
  override val hoursPerDay: Int = 4
}