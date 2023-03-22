package org.personal.projects
package model

import config.Config.euroExRate

import org.personal.projects.taxes.Tax

case class Revenue(euroAmount: Double, ronAmount: Double) {
  def *(d: Double) = Revenue(euroAmount * d, ronAmount * d)

  def /(d: Double) = if (d == 0) Revenue.empty else Revenue(euroAmount / d, ronAmount / d)

  def -(r: Revenue) = Revenue(euroAmount - r.euroAmount, ronAmount - r.ronAmount)

  def +(r: Revenue) = Revenue(euroAmount + r.euroAmount, ronAmount + r.ronAmount)

  def map(f: Double => Double) = Revenue(f(euroAmount), f(ronAmount))

  def tax(t: Tax): Revenue = map(t.applyTax)

  def reverseTax(t: Tax): Revenue = map(t.subtractTax)

  def taxContribution(tax: Tax): Revenue = map(tax.asContribution)

  override def toString: String = f"Euro: $euroAmount%.2f |||| Ron: $ronAmount%.2f"
}

object Revenue {

  val empty: Revenue = Revenue(0.0, 0.0)

  def fromOtherAmount(amount: Double, currency: Currency): Revenue = currency match {
    case Euro => Revenue(amount, amount * euroExRate)
    case Ron => Revenue(amount / euroExRate, amount)
  }

}
