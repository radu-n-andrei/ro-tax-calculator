package org.personal.projects
package query

import model.WorkRate

import cats.implicits.toBifunctorOps
import org.http4s.{ParseFailure, QueryParamDecoder}

object QueryParameterImplicits {

  implicit val jobQueryParamDecoder: QueryParamDecoder[JobDescription] = QueryParamDecoder[String].emap(
    str => {
      val p = "([1-9][0-9]*) (full_time|part_time)".r
      str match {
        case p(rate, workRate) => WorkRate.fromString(workRate).map(JobDescription(rate.toInt, _))
          .leftMap(_ => ParseFailure("Failed to parse input work rate", "work rate failed to parse"))
        case _ => Left(ParseFailure("Overall parsing failure", "parse failure"))
      }
    }
  )
}
