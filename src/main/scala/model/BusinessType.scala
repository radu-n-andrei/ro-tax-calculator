package org.personal.projects
package model

import dividend.{DividendDto, DividendSimulation}
import taxes._

import cats.effect.IO

sealed trait BusinessType {
  val key: String

  def evaluateEarnings(revenue: Revenue, wage: Option[Double]): IO[List[DividendDto]]
}

case object MicroSRL extends BusinessType {
  override val key = "micro"

  override def evaluateEarnings(revenue: Revenue, wage: Option[Double]): IO[List[DividendDto]] = for {
    netSalary <- Salary.fromGrossIncome(Revenue.fromOtherAmount(wage.get, Ron))
    totalPayedForEmp <- Revenue.fromOtherIO(wage.get, Ron).map(_.taxContribution(CamTax))
    afterSrl <- IO(revenue.tax(SRLIncomeTax))
    sim <- DividendSimulation.runSimulation(afterSrl - totalPayedForEmp, netSalary.revenue)
  } yield sim
}

case object SRL extends BusinessType {
  override val key = "srl"

  override def evaluateEarnings(revenue: Revenue, wage: Option[Double] = None): IO[List[DividendDto]] = for {
    availableForDivs <- IO(revenue.tax(ProfitSRLTax))
    divs <- DividendSimulation.runSimulation(availableForDivs, Revenue.empty)
  } yield divs
}

case object PFA extends BusinessType {
  override val key = "pfa"

  override def evaluateEarnings(revenue: Revenue, wage: Option[Double] = None): IO[List[DividendDto]] = for {
    yearlyAmount <- IO(revenue * 12)
    casAmount <- CasContribution.contributionIO(yearlyAmount)
    cassAmount <- CassContribution.contributionIO(yearlyAmount)
    net = (yearlyAmount - casAmount).tax(PFAIncomeTax) - cassAmount
  } yield List(DividendDto("PFA", 0.0, net.euroAmount / 12.0))
}

object BusinessType {
  def fromString(businessType: String): Either[String, BusinessType] = businessType.toLowerCase match {
    case MicroSRL.key => Right(MicroSRL)
    case SRL.key => Right(SRL)
    case PFA.key => Right(PFA)
    case _ => Left(s"INCORRECT BUSINESS TYPE: $businessType")
  }
}