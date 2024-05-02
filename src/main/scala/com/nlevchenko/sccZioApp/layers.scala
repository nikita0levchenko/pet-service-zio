package com.nlevchenko.sccZioApp


import com.nlevchenko.sccZioApp.config.{ApiConfig, AppConfig, DbConfig}
import com.nlevchenko.sccZioApp.domain.actor.ActorRepositoryLive
import com.nlevchenko.sccZioApp.domain.pet.PetRepositoryLive
import com.nlevchenko.sccZioApp.domain.uptime.UptimeRepositoryLive
import com.nlevchenko.sccZioApp.repository.Repository
import zio._
import zio.blocking.Blocking
import zio.logging.Logging
import zio.logging.slf4j.Slf4jLogger

object layers {
  type Layer0Env = Blocking with Logging with Has[AppConfig]
  type Layer1Env = Layer0Env with Has[ApiConfig] with Has[DbConfig]
  type Layer2Env = Layer1Env with Has[Repository]
  type AppEnv    = Layer2Env

  object live {
    val layer0: ZLayer[Blocking, Throwable, Layer0Env] =
      Blocking.any ++ Slf4jLogger.make((_, msg) => msg) ++ AppConfig.live

    val layer1: ZLayer[Layer0Env, Throwable, Layer1Env] =
      ApiConfig.fromAppConfig ++ DbConfig.fromAppConfig ++ ZLayer.identity

    val layer2: ZLayer[Layer1Env, Throwable, Layer2Env] =
      UptimeRepositoryLive.layer ++ ZLayer.identity

    val layer3: ZLayer[Layer1Env, Throwable, Layer2Env] =
      ActorRepositoryLive.layer ++ ZLayer.identity

    val layer4: ZLayer[Layer1Env, Throwable, Layer2Env] =
      PetRepositoryLive.layer ++ ZLayer.identity

    val appLayer: ZLayer[Blocking, Throwable, AppEnv] =
      layer0 >>> layer1 >>> layer2 >>> layer3 >>> layer4
  }
}
