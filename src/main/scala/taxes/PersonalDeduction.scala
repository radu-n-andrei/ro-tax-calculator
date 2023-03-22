package org.personal.projects
package taxes

import model.{Revenue, Ron}
import config.Config._

object PersonalDeduction {

  def deductionAmount(rev: Revenue): Revenue = {
    val min = Revenue.fromOtherAmount(minimumWage, Ron)
    val dif = rev - min

    val mult = dif.ronAmount.intValue / 50
    val rem = if(dif.ronAmount % 50 == 0) 0 else 1
    val gr = (mult + rem) * 0.50
    val proc = 20.0 - gr
    min * (proc / 100.0)
  }

}
