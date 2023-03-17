package org.personal.projects
package model

sealed trait Currency {
  def exchangeRage: Double
}

trait LocalCurrency extends Currency {
  override def exchangeRage: Double = 1.0
}

case object Euro extends Currency {
  override def exchangeRage: Double = 4.91
}

case object Ron extends LocalCurrency