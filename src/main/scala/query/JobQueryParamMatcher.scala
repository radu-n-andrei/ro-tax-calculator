package org.personal.projects
package query

import org.http4s.dsl.impl.OptionalMultiQueryParamDecoderMatcher
import org.personal.projects.query.QueryParameterImplicits.jobQueryParamDecoder

object JobQueryParamMatcher extends
  OptionalMultiQueryParamDecoderMatcher[JobDescription]("job_description")
