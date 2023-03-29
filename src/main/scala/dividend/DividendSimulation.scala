package org.personal.projects
package dividend

import model.Revenue
import taxes.{CassContribution, DividendTax}

import cats.effect.IO
import cats.implicits.toTraverseOps

object DividendSimulation {

  private def unfoldStrategy(strategy: DividendExtractionStrategy, revenue: Revenue, baseYearlyWage: Revenue): IO[DividendDto] = for {
    extractedAmountGross <- strategy.extractionAmount(revenue)
    extractedAmountNet <- extractedAmountGross.tax(DividendTax).map(CassContribution.afterContribution)
    totalExtractedNet = extractedAmountNet * strategy.extractions
  } yield DividendDto(strategy.strategyName, totalExtractedNet.euroAmount, (totalExtractedNet + baseYearlyWage).euroAmount / 12.0)


  def runSimulation(monthlyAmount: Revenue, netMonthlyRev: Revenue): IO[List[DividendDto]] = {
    DividendExtractionStrategy.strategyList.traverse(unfoldStrategy(_, monthlyAmount, netMonthlyRev * 12))
  }

}
