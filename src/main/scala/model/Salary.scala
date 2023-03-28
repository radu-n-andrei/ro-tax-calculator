package org.personal.projects
package model

import config.Config
import taxes.{CasTax, CassTax, IncomeTax, PersonalDeduction}

import cats.Traverse.ops.toAllTraverseOps
import cats.effect.IO

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

  def fromGrossIncome(revenue: Revenue): IO[Salary] = {
    for {
      minWageBonus <- Option.when(revenue.ronAmount == minimumWage.ronAmount)(200.0).traverse(Revenue.fromOtherIO(_, Ron))
      beforeIncomeTax <- minWageBonus.fold(IO(revenue))(r => IO(revenue - r)).map(_.tax(CasTax + CassTax))
      personalDeduction <- IO(PersonalDeduction.deductionAmount(revenue))
      netSalary = (beforeIncomeTax - personalDeduction).tax(IncomeTax)
    } yield NetSalary(netSalary + personalDeduction + minWageBonus.getOrElse(Revenue.empty))
  }
}
