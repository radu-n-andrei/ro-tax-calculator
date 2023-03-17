package org.personal.projects
package dividend

import model.{DividendTax, Euro, Revenue}
import config.Config._

import cats.implicits.catsSyntaxEitherId

object DividendSimulation {

  def runSimulation(monthlyAmount: Revenue): List[DividendDto] = {
    val dividendMap = Map(Trimester -> monthlyAmount * 3,
      Semester -> monthlyAmount * 6,
      Yearly -> monthlyAmount * 12)
    val dividendsUpForExtraction: Map[DividendExtractionStrategy, Revenue] = dividendMap.map(dividend =>
      dividend._1 -> DividendCassContribution.afterContribution(dividend._2).tax(DividendTax))
    println("DIVIDEND EXTRACTION BY STRATEGY")
    dividendsUpForExtraction.map(
      div => {
        val extractedAmount = div._1.totalAmount(div._2)
        val apparentMonthlyNetSalary = (extractedAmount + Revenue.fromOtherAmount(netEmployeeWage, Euro) * 12.0) / 12.0
        println(s"\t${div._1.strategyName} GIVES OUT A TOTAL OF ${extractedAmount} LEADING TO AN APPARENT MONTHLY WAGE OF ${apparentMonthlyNetSalary}")
        DividendDto(div._1.strategyName, extractedAmount.euroAmount, apparentMonthlyNetSalary.euroAmount)
      }
    ).toList
  }

}
