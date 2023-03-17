package org.personal.projects
package config

case object Config {

  val minimumWage: Double = 3000

  // cat vreau sa-mi dau salariu lunar net in euro
  val netEmployeeWage: Double = 3000

  // cat cer pe ora in euro
  val hourlyRate = 55

  val euroExRate: Double = 4.92

  // taxele salariale
  val cas = 25
  val cass = 10
  val iv = 10
  val cam = 2.25

  // dividend tax
  val div = 8

  // srl tax
  val srl = 1
}
