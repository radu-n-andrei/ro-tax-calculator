package org.personal.projects
package taxes

import config.Config._

sealed trait Tax {

  val taxAmount: Double

  def taxedValue(sum: Double): Double = sum * taxAmount / 100.0

  def applyTax(sum: Double): Double = sum * (1 - taxAmount / 100)

  def subtractTax(sum: Double): Double = sum * (1 / (1 - taxAmount / 100))

  def asContribution(sum: Double): Double = sum * (1 + taxAmount / 100)

  def +(tax: Tax): Tax = {
    val thisTaxAmt = taxAmount
    new Tax {
      override val taxAmount: Double = tax.taxAmount + thisTaxAmt
    }
  }

}

case object SRLIncomeTax extends Tax {
  override val taxAmount: Double = srl
}

case object DividendTax extends Tax {
  override val taxAmount: Double = div
}

case object CassTax extends Tax {
  override val taxAmount: Double = cass
}

case object CasTax extends Tax {
  override val taxAmount: Double = cas
}

case object IncomeTax extends Tax {
  override val taxAmount: Double = iv
}

case object CamTax extends Tax {
  override val taxAmount: Double = cam
}

case object PFAIncomeTax extends Tax {
  override val taxAmount: Double = pfa
}

case object ProfitSRLTax extends Tax {
  override val taxAmount: Double = profit
}

case object SwedishTraderTax extends Tax {
  override val taxAmount: Double = swe
}

case object SwedishTraderCas extends Tax {
  override val taxAmount: Double = swecas
}

