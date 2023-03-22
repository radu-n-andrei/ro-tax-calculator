package org.personal.projects
package model

sealed trait BusinessType {
  val key: String
}

case object MicroSRL extends BusinessType {
  override val key = "micro"
}

case object SRL extends BusinessType {
  override val key = "srl"
}

case object PFA extends BusinessType {
  override val key = "pfa"
}

object BusinessType {
  def fromString(businessType: String): Either[String, BusinessType] = businessType.toLowerCase match {
    case MicroSRL.key => Right(MicroSRL)
    case SRL.key => Right(SRL)
    case PFA.key => Right(PFA)
    case _ => Left(s"INCORRECT BUSINESS TYPE: $businessType")
  }
}