package org.personal.projects
package earnings

import dividend.{DividendDto, DividendSimulation}
import model.{BusinessType, MicroSRL, PFA, Revenue, Ron, SRL, Salary, SwedishSoleTrader}
import taxes.{CamTax, CasContribution, CassContribution, PFAIncomeTax, ProfitSRLTax, SRLIncomeTax, SwedishTraderCas, SwedishTraderTax}

import cats.effect.IO

trait IncomeGenerator[T <: BusinessType] {
  def evaluateEarnings(revenue: Revenue, wage: Option[Double]): IO[List[DividendDto]]
}

object IncomeGenerator {

  def apply[T <: BusinessType](implicit incomeGenerator: IncomeGenerator[T]): IncomeGenerator[T] = incomeGenerator

  implicit val microSrlIncomeGenerator: IncomeGenerator[MicroSRL] =
    (revenue: Revenue, wage: Option[Double]) => for {
      netSalary <- Salary.fromGrossIncome(Revenue.fromOtherAmount(wage.get, Ron))
      totalPayedForEmp <- Revenue.fromOtherIO(wage.get, Ron).flatMap(_.taxContribution(CamTax))
      afterSrl <- revenue.tax(SRLIncomeTax)
      sim <- DividendSimulation.runSimulation(afterSrl - totalPayedForEmp, netSalary.revenue)
    } yield sim

  implicit val srlIncomeGenerator: IncomeGenerator[SRL] =
    (revenue: Revenue, wage: Option[Double]) => for {
      availableForDivs <- revenue.tax(ProfitSRLTax)
      divs <- DividendSimulation.runSimulation(availableForDivs, Revenue.empty)
    } yield divs

  implicit val pfaIncomeGenerator: IncomeGenerator[PFA] =
    (revenue: Revenue, wage: Option[Double]) => for {
      yearlyAmount <- IO(revenue * 12)
      casAmount <- CasContribution.contributionIO(yearlyAmount)
      cassAmount <- CassContribution.contributionIO(yearlyAmount)
      net <- (yearlyAmount - casAmount).tax(PFAIncomeTax).map(amt => amt - cassAmount)
    } yield List(DividendDto("PFA", 0.0, net.euroAmount / 12.0))

  implicit val swedishGenerator: IncomeGenerator[SwedishSoleTrader] =
    (revenue: Revenue, wage: Option[Double]) => for {
      monthlyAmount <- IO(revenue)
      taxable <- monthlyAmount.tax(SwedishTraderCas)
      net <- taxable.tax(SwedishTraderTax)
    } yield List(DividendDto("SWE", 0.0, net.euroAmount))

}