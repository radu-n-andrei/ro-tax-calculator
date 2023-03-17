package org.personal.projects
package dividend

import model.Revenue

sealed trait DividendExtractionStrategy {
  def totalAmount(revenue: Revenue): Revenue
  val strategyName: String
}

case object Trimester extends DividendExtractionStrategy {
  override def totalAmount(revenue: Revenue): Revenue = revenue * 4

  override val strategyName: String = "TRIMESTRIALA"
}

case object Semester extends DividendExtractionStrategy {
  override def totalAmount(revenue: Revenue): Revenue = revenue * 2

  override val strategyName: String = "SEMESTRIALA"
}

case object Yearly extends DividendExtractionStrategy {
  override def totalAmount(revenue: Revenue): Revenue = revenue

  override val strategyName: String = "ANUALA"
}
