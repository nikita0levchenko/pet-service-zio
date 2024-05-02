package com.nlevchenko.sccZioApp

import cats.effect._
import com.nlevchenko.sccZioApp.config.AppConfig
import com.nlevchenko.sccZioApp.domain.actor.ActorApi
import com.nlevchenko.sccZioApp.domain.pet.PetApi
import com.nlevchenko.sccZioApp.domain.uptime.UptimeApi
import com.nlevchenko.sccZioApp.repository.Repository
import org.http4s._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.CORS
import zio.clock.Clock
import zio.interop.catz._
import zio.{ExitCode => ZExitCode, _}

object Main extends App {
  type AppTask[A] = RIO[layers.AppEnv with Clock, A]

  override def run(args: List[String]): ZIO[ZEnv, Nothing, ZExitCode] = {
    val prog =
      for {
        cfg    <- AppConfig.getAppConfig
        _      <- logging.log.info(s"Starting with $cfg")
        httpApp = Router[AppTask](
                    "/uptime" -> UptimeApi.routes,
          "actors" -> ActorApi.routes,
          "pets" -> PetApi.routes
                  ).orNotFound
        _      <- runHttp(httpApp, cfg.api.port, cfg.api.endpoint)
      } yield ZExitCode.success

    prog
      .provideSomeLayer[ZEnv](Repository.withTracing(layers.live.appLayer))
      .orDie
  }

  def runHttp[R <: Clock](
    httpApp: HttpApp[RIO[R, *]],
    port: Int,
    endpoint: String
  ): ZIO[R, Throwable, Unit] = {
    type Task[A] = RIO[R, A]

    ZIO.runtime[R].flatMap { implicit rts =>
      BlazeServerBuilder
        .apply[Task](rts.platform.executor.asEC)
        .bindHttp(port, endpoint)
        .withHttpApp(CORS(httpApp))
        .serve
        .compile[Task, Task, ExitCode]
        .drain
    }
  }
}
