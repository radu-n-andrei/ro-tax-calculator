package org.personal.projects
package model

import config.Config._

trait Salary {
  val revenue: Revenue
}

case class NetSalary(override val revenue: Revenue) extends Salary {
  override def toString: String = s"NET: $revenue"
}

case class GrossSalary(override val revenue: Revenue) extends Salary {
  override def toString: String = s"BRUT: $revenue"
}

object Salary {

  def fromNetIncome(rev: Revenue): Salary = {
    val netWithIV = rev.reverseTax(IncomeTax)
    val cassAdded = netWithIV.reverseTax(CassTax + CasTax)
    GrossSalary(cassAdded)
  }

  def fromGrossIncome(rev: Revenue): Salary = {
    val beforeIncomeTax = rev.tax(CasTax + CassTax)
    val netSalary = beforeIncomeTax.tax(IncomeTax)
    NetSalary(netSalary)
  }
}
