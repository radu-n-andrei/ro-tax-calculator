package org.personal.projects

import config.Config.{grossEmployeeWage, hourlyRate}
import model.{BusinessType, Euro, Hourly, Invoice, MicroSRL, Monthly, PFA, Rate, Revenue, Ron, SRL, Salary, WorkRate}

import org.personal.projects.dividend.{DividendDto, DividendSimulation, Yearly}
import org.personal.projects.taxes.{CamTax, CasContribution, CassContribution, PFAIncomeTax, ProfitSRLTax, SRLIncomeTax}

case class Simulation(amount: Int, rate: Rate, businessType: BusinessType, empWage: Double = grossEmployeeWage, workRate: WorkRate)

object Simulation {

  private def simulateMicroSRL(billedAmount: Revenue, empWage: Double): List[DividendDto] = {
    // WAGE STUFF
    val grossSalary = Revenue.fromOtherAmount(empWage, Ron)
    val netSalary = Salary.fromGrossIncome(grossSalary)
    val salaryTaxes = grossSalary - netSalary.revenue
    println("======SALARY=======")
    println(s"GROSS SALARY PAYED TO EMPLOYEE: $grossSalary")
    println(s"NET SALARY PAYED TO EMPLOYEE: ${netSalary.revenue}")
    println(s"YEARLY NET SALARY REVENUE: ${netSalary.revenue * 12.0}")
    println(s"TOTAL SALARY TAXES $salaryTaxes")
    val totalPayedForEmployee = grossSalary.taxContribution(CamTax)
    println(s"TOTAL PAYED FOR EMPLOYEE: $totalPayedForEmployee")
    // SRL TAXES
    val afterSrlTaxes = billedAmount.tax(SRLIncomeTax)
    println(s"SRL INCOME TAX: ${billedAmount - afterSrlTaxes}")
    println(s"YEARLY SRL INCOME TAX: ${(billedAmount - afterSrlTaxes) * 12.0}")
    val totalUpForDividends = afterSrlTaxes - totalPayedForEmployee
    println(s"TOTAL UP FOR DIVIDENDS: ${totalUpForDividends}")
    println(s"YEARLY TOTAL UP FOR DIVIDENDS: ${totalUpForDividends * 12.0}")
    // run simulation
    val divs = DividendSimulation.runSimulation(totalUpForDividends, netSalary.revenue)
    val totalNet = Revenue.fromOtherAmount(divs.find(_.strategyName == Yearly.strategyName).map(_.expectedSalary * 12.0).getOrElse(0.0), Euro)
    println(s"TOTAL EARNINGS: ${totalNet}")
    divs
  }

  private def simulateSRL(billedAmount: Revenue): List[DividendDto] = {
    val afterProfitTax = billedAmount.tax(ProfitSRLTax)
    println(s"MONTHLY TAXES: ${billedAmount - afterProfitTax}")
    println(s"YEARLY TAXES: ${(billedAmount - afterProfitTax) * 12}")
    println(s"UP FOR DIVIDENDS: ${afterProfitTax}")
    println(s"UP FOR DIVIDENDS PER YEAR: ${afterProfitTax * 12.0}")
    val divs = DividendSimulation.runSimulation(afterProfitTax, Revenue.empty)
    println(s"TOTAL EARNINGS: ${Revenue.fromOtherAmount(divs.find(_.strategyName == Yearly.strategyName).map(_.dividendValue).getOrElse(0.0), Euro)}")
    divs
  }

  private def simulatePFA(billedAmount: Revenue): List[DividendDto] = {
    val yearlyAmount = billedAmount * 12
    println(s"BILLED IN A YEAR: ${yearlyAmount}")

    def casAmount = CasContribution.contribution(yearlyAmount)

    def cassAmount: Revenue = CassContribution.contribution(yearlyAmount)

    def totalContributions = casAmount + cassAmount

    println(s"PFA CONTRIBUTIONS: CASS = [${cassAmount}], CAS = [${casAmount}], TOTAL = [${totalContributions}]")
    // only CAS is extracted for the base yearly income tax
    val taxableRevenue = yearlyAmount - casAmount
    println(s"TAXABLE REVENUE $taxableRevenue")
    val afterPfaTax = taxableRevenue.tax(PFAIncomeTax)
    println(s"INCOME TAX: ${taxableRevenue - afterPfaTax}")
    val netEarnings = afterPfaTax - cassAmount
    println(s"NET EARNINGS: $netEarnings")
    println(s"MONTHLY NET EARNINGS: ${netEarnings / 12.0}")
    List(DividendDto("PFA MONTHLY", 0.0, netEarnings.euroAmount / 12.0))
  }

  def runSimulationFromConfig(simulationData: Simulation): List[DividendDto] = {
    val billedAmount: Revenue = simulationData.rate match {
      case Hourly => Invoice(hourlyRate = simulationData.amount, currency = Euro, workRate = simulationData.workRate).grossBilled
      case Monthly => Revenue.fromOtherAmount(simulationData.amount, Euro)
    }
    println("AMOUNT BILLED PER MONTH: " + billedAmount)
    println("AMOUNT BILLED PER YEAR: " + billedAmount * 12)
    simulationData.businessType match {
      case MicroSRL => simulateMicroSRL(billedAmount, simulationData.empWage)
      case PFA => simulatePFA(billedAmount)
      case SRL => simulateSRL(billedAmount)
    }
  }


}
