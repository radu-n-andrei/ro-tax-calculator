package org.personal.projects
package model

import config.Config._

import org.personal.projects.config.Config
import org.personal.projects.taxes.{CasTax, CassTax, IncomeTax, PersonalDeduction}

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

  private val minimumWage: Revenue = Revenue.fromOtherAmount(Config.minimumWage, Ron)

  def fromGrossIncome(rev: Revenue): Salary = {
    val minWageBonus = if(rev.ronAmount == minimumWage.ronAmount) 200 else 0
    val minWageBonusRev = Revenue.fromOtherAmount(minWageBonus, Ron)
    val upForTaxes = rev - minWageBonusRev
    val beforeIncomeTax = upForTaxes.tax(CasTax + CassTax)
    val personalDeduction = PersonalDeduction.deductionAmount(rev)
    val taxableAmount = beforeIncomeTax - personalDeduction
    val netSalary = taxableAmount.tax(IncomeTax)
    NetSalary(netSalary + personalDeduction + minWageBonusRev)
  }
}
