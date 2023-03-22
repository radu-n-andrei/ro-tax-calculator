package org.personal.projects
package taxes

import config.Config.minimumWage
import model.{Revenue, Ron}

trait Contribution {
  def contribution(div: Revenue): Revenue
  def afterContribution(div: Revenue): Revenue = div - contribution(div)
}

object CassContribution extends Contribution {

  def contribution(div: Revenue): Revenue = {
    val r = div.ronAmount / minimumWage

    val multiplier = r.intValue match {
      case r if (6 until 12).contains(r) => 6
      case r if (12 until 24).contains(r) => 12
      case r if r >= 24 => 24
      case _ => 0
    }
    Revenue.fromOtherAmount(minimumWage * multiplier * 0.1, Ron)
  }
}

object CasContribution extends Contribution {

  def contribution(div: Revenue): Revenue = {
    val r = div.ronAmount / minimumWage

    val multiplier = r.intValue match {
      case r if r < 12 => 0
      case r if (12 until 24).contains(r) => 12
      case r if r >= 24 => 24
    }
    Revenue.fromOtherAmount(minimumWage * multiplier * 0.25, Ron)
  }
}