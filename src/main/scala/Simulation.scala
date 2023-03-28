package org.personal.projects

import config.Config.grossEmployeeWage
import dividend.DividendDto
import model.{BusinessType, Rate, WorkRate}

import cats.effect.IO

case class Simulation(amount: Int, rate: Rate, businessType: BusinessType, empWage: Double = grossEmployeeWage, workRate: WorkRate)

object Simulation {
  def runSimulationFromConfig(simulationData: Simulation): IO[List[DividendDto]] =
    for {
      billedAmount <- simulationData.rate.generatedRevenue(simulationData)
      revs <- simulationData.businessType.evaluateEarnings(billedAmount, Some(simulationData.empWage))
    } yield revs


}
