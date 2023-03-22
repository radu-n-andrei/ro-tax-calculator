package org.personal.projects

import model.{BusinessType, Rate, WorkRate}

import cats.effect._
import io.circe.Json
import io.circe.syntax.EncoderOps
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.{EntityEncoder, HttpApp, HttpRoutes, circe}

object Main extends IOApp {

  implicit val encoder: EntityEncoder[IO, Json] = circe.jsonEncoderOf[IO, Json]


  private val revenueService: HttpApp[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "businessType" / bType / "workRate" / workRate / "rate" / rateType / amount =>
      (BusinessType.fromString(bType), WorkRate.fromString(workRate), Rate.fromString(rateType)) match {
        case (br, wr, lr) if br.isLeft || wr.isLeft || lr.isLeft =>
          BadRequest(s"Incorrect types supplied: " +
            s"${List(br, wr, lr).flatMap(_.left.toOption).mkString(" | ")}")
        case (Right(b), Right(w), Right(r)) => Ok(Simulation.runSimulationFromConfig(
          Simulation(amount = Integer.parseInt(amount), rate = r, businessType = b, workRate = w)).asJson)
      }

  }.orNotFound

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(revenueService)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)

}
