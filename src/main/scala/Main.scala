package org.personal.projects

import cats.effect._
import io.circe.Json
import io.circe.syntax.EncoderOps
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.{EntityEncoder, HttpRoutes, circe}
import org.personal.projects.model.{BusinessType, Hourly, Incorrect, Monthly, Rate}

object Main extends IOApp {

  implicit val encoder: EntityEncoder[IO, Json] = circe.jsonEncoderOf[IO, Json]


  val helloWorldService = HttpRoutes.of[IO] {
    case GET -> Root / "businessType" / bType / "rate" / rateType / amount =>
      (BusinessType.fromString(bType), Rate.fromString(rateType)) match {
        case (Right(_), _) | (_, Right(_)) => BadRequest(s"Incorrect types supplied")
        case (Left(b), Left(r)) => Ok(Simulation.runSimulationFromConfig(
          Simulation(amount = Integer.parseInt(amount), rate = r, businessType = b)).asJson)
      }

  }.orNotFound

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(helloWorldService)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)

}
