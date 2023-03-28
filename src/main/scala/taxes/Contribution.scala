package org.personal.projects
package taxes

import config.Config.minimumWage
import model.{Revenue, Ron}

import cats.effect.IO

trait Contribution {
  def contribution(div: Revenue): Revenue

  def afterContribution(div: Revenue): Revenue = div - contribution(div)

  def contributionIO(div: Revenue): IO[Revenue]
}

object CassContribution extends Contribution {

  private def multiplier(div: Revenue): Int = {
    val r = div.ronAmount / minimumWage
    val multiplier = r.intValue match {
      case r if (6 until 12).contains(r) => 6
      case r if (12 until 24).contains(r) => 12
      case r if r >= 24 => 24
      case _ => 0
    }
    multiplier
  }

  override def contribution(div: Revenue): Revenue = {
    Revenue.fromOtherAmount(minimumWage * multiplier(div) * 0.1, Ron)
  }

  override def contributionIO(div: Revenue): IO[Revenue] =
    Revenue.fromOtherIO(minimumWage * multiplier(div) * 0.1, Ron)
}

object CasContribution extends Contribution {

  private def multiplier(div: Revenue) = {
    val r = div.ronAmount / minimumWage

    val multiplier = r.intValue match {
      case r if r < 12 => 0
      case r if (12 until 24).contains(r) => 12
      case r if r >= 24 => 24
    }
    multiplier
  }

  override def contribution(div: Revenue): Revenue =
    Revenue.fromOtherAmount(minimumWage * multiplier(div) * 0.25, Ron)

  override def contributionIO(div: Revenue): IO[Revenue] = Revenue.fromOtherIO(minimumWage * multiplier(div) * 0.25, Ron)
}