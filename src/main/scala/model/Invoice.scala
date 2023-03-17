package org.personal.projects
package model

case class Invoice(hourlyRate: Int, currency: Currency, workingDays: Int = 20, workRate: WorkRate = FullTime) {
  val grossBilled: Revenue = Revenue.fromOtherAmount(hourlyRate * workRate.hoursPerDay * workingDays, currency)
}
