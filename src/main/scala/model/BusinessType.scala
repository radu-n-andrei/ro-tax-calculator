package org.personal.projects
package model

import earnings.IncomeGenerator

sealed trait BusinessType {
  val key: String
}
trait MicroSRL extends BusinessType
trait SRL extends BusinessType
trait PFA extends BusinessType
trait SwedishSoleTrader extends BusinessType

object BusinessType {
  def fromString(businessType: String): Either[String, IncomeGenerator[_]] = businessType.toLowerCase match {
    case "micro" => Right(IncomeGenerator[MicroSRL])
    case "srl" => Right(IncomeGenerator[SRL])
    case "pfa" => Right(IncomeGenerator[PFA])
    case "swe" => Right(IncomeGenerator[SwedishSoleTrader])
    case _ => Left(s"INCORRECT BUSINESS TYPE: $businessType")
  }
}

