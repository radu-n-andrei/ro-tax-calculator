package org.personal.projects
package dividend

import config.Config.minimumWage

import model.{Revenue, Ron}

object DividendCassContribution {

  private def contribution(div: Revenue): Revenue = {
    val r = div.ronAmount / minimumWage

    val multiplier = r.intValue match {
      case r if (6 until 12).contains(r) => 6
      case r if (12 until 24).contains(r) => 12
      case r if r >= 24 => 24
      case _ => 0
    }
    Revenue.fromOtherAmount(minimumWage * multiplier * 0.1, Ron)
  }

  def afterContribution(div: Revenue): Revenue = div - contribution(div)

}