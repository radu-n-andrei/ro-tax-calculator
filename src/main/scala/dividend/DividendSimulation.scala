package org.personal.projects
package dividend

import model.{Euro, Revenue}
import config.Config._

import cats.implicits.catsSyntaxEitherId
import org.personal.projects.taxes.{CassContribution, DividendTax}

object DividendSimulation {

  def runSimulation(monthlyAmount: Revenue, netMonthlyRev: Revenue): List[DividendDto] = {
    val dividendMap: Map[DividendExtractionStrategy, Revenue] = Map(Trimester -> monthlyAmount * 3,
      Semester -> monthlyAmount * 6,
      Yearly -> monthlyAmount * 12)
    val dividendsUpForExtraction: Map[DividendExtractionStrategy, Revenue] = dividendMap.map(dividend =>
      dividend._1 -> CassContribution.afterContribution(dividend._2.tax(DividendTax)))
    println("DIVIDEND EXTRACTION BY STRATEGY")
    dividendsUpForExtraction.map(
      div => {
        val extractedAmount = div._1.totalAmount(div._2)
        val dividendTaxesAndContribs = (monthlyAmount * 12) - extractedAmount
        val apparentMonthlyNetSalary = (extractedAmount + netMonthlyRev * 12.0) / 12.0
        println(s"\t${div._1.strategyName} GIVES OUT A TOTAL OF ${extractedAmount} LEADING TO AN APPARENT MONTHLY WAGE OF ${apparentMonthlyNetSalary}")
        println(s"\t\tTOTAL DIVIDEND TAXES AND CONTRIBUTIONS: $dividendTaxesAndContribs. ${dividendTaxesAndContribs / 12.0} PER MONTH")
        DividendDto(div._1.strategyName, extractedAmount.euroAmount, apparentMonthlyNetSalary.euroAmount)
      }
    ).toList
  }

}
