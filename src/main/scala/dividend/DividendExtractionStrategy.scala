package org.personal.projects
package dividend

import model.Revenue

import cats.effect.IO

sealed trait DividendExtractionStrategy {
  def extractionAmount(revenue: Revenue): IO[Revenue] = IO(revenue * multiplier)

  def totalAmount(revenue: Revenue): IO[Revenue] = extractionAmount(revenue).map(_ * extractions)

  val strategyName: String
  val multiplier: Int
  val extractions: Int
}

case object Trimester extends DividendExtractionStrategy {
  override val multiplier: Int = 3

  override val strategyName: String = "TRIMESTRIALA"

  override val extractions: Int = 4
}

case object Semester extends DividendExtractionStrategy {
  override val multiplier: Int = 6

  override val strategyName: String = "SEMESTRIALA"

  override val extractions: Int = 2
}

case object Yearly extends DividendExtractionStrategy {
  override val multiplier: Int = 12

  override val strategyName: String = "ANUALA"

  override val extractions: Int = 1
}

object DividendExtractionStrategy {
  val strategyList: List[DividendExtractionStrategy] = List(Trimester, Semester, Yearly)
}

