package org.personal.projects

import cats.effect._
import io.circe.Json
import io.circe.syntax.EncoderOps
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.{EntityEncoder, HttpRoutes, circe}

object Main extends IOApp {

  implicit val encoder: EntityEncoder[IO, Json] = circe.jsonEncoderOf[IO, Json]


  val helloWorldService = HttpRoutes.of[IO] {
    case GET -> Root / "hello" =>
      Ok(Simulation.runSimulationFromConfig().asJson)
    case GET -> Root / "config" / rate =>
      Ok(Simulation.runSimulationFromConfig(Simulation(hRate = Integer.parseInt(rate))).asJson)
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
