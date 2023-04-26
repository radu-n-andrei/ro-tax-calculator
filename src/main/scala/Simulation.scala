package org.personal.projects

import config.Config.grossEmployeeWage
import dividend.DividendDto
import earnings.IncomeGenerator
import model.{Rate, WorkRate}

import cats.effect.IO

case class Simulation(amount: Int, rate: Rate, incomeGenerator: IncomeGenerator[_], empWage: Double = grossEmployeeWage, workRate: WorkRate)

object Simulation {
  def runSimulationFromConfig(simulationData: Simulation): IO[List[DividendDto]] =
    for {
      billedAmount <- simulationData.rate.generatedRevenue(simulationData)
      revs <- simulationData.incomeGenerator.evaluateEarnings(billedAmount, Some(simulationData.empWage))
    } yield revs


}
