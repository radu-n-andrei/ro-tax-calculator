package org.personal.projects

import config.Config.{hourlyRate, netEmployeeWage}
import model.{CamTax, Euro, Invoice, Revenue, SRLIncomeTax, Salary}

import org.personal.projects.dividend.{DividendDto, DividendSimulation}

case class Simulation(hRate: Int = hourlyRate,
                      empWage: Double = netEmployeeWage)

object Simulation {

  def runSimulationFromConfig(simulationData: Simulation = Simulation()) : List[DividendDto] = {
    val invoice = Invoice(simulationData.hRate, Euro)
    println("AMOUNT BILLED PER MONTH: " + invoice.grossBilled)
    // WAGE STUFF
    val wageAsRevenue = Revenue.fromOtherAmount(simulationData.empWage, Euro)
    val grossSalary = Salary.fromNetIncome(wageAsRevenue)
    println(s"GROSS SALARY PAYED TO EMPLOYEE: $grossSalary")
    val totalPayedForEmployee = grossSalary.revenue.taxContribution(CamTax)
    println(s"TOTAL PAYED FOR EMPLOYEE: $totalPayedForEmployee")
    // SRL TAXES
    val afterSrlTaxes = invoice.grossBilled.tax(SRLIncomeTax)
    val totalUpForDividends = afterSrlTaxes - totalPayedForEmployee
    println(s"TOTAL UP FOR DIVIDENDS: ${totalUpForDividends}")
    // run simulation
    DividendSimulation.runSimulation(totalUpForDividends)
  }


}
