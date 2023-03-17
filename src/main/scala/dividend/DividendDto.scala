package org.personal.projects
package dividend

import io.circe.Encoder

case class DividendDto(strategyName: String, dividendValue: Double, expectedSalary: Double)

object DividendDto {

  implicit val encoder: Encoder[DividendDto] = Encoder.forProduct3("strategyName", "dividendValue", "actualMonthlySalary")(x =>
    (x.strategyName, f"""${x.dividendValue}%.2f""", f"""${x.expectedSalary}%.2f"""))

}
