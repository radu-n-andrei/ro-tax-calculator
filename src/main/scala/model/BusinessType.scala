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
  def fromString(businessType: String): Either[BusinessType, String] = businessType.toLowerCase match {
    case MicroSRL.key => Left(MicroSRL)
    case SRL.key => Left(SRL)
    case PFA.key => Left(PFA)
    case _ => Right("UNKNOWN BUSINESS TYPE")
  }
}