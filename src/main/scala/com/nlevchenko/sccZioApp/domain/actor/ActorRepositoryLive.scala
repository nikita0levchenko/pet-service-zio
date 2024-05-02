package com.nlevchenko.sccZioApp.domain.actor

import com.nlevchenko.sccZioApp.config.DbConfig
import com.nlevchenko.sccZioApp.repository.Repository
import doobie._
import doobie.implicits._
import doobie.util.transactor.Transactor
import zio._
import zio.blocking.Blocking
import zio.interop.catz._

final case class ActorRepositoryLive(xa: Transactor[Task]) extends Repository {

  private val getActorsSQL: Query0[String] = sql"SELECT name FROM actors".query[String]

  override def getActors: UIO[List[String]] = getActorsSQL.to[List].transact(xa).orDie
}

object ActorRepositoryLive {
  val layer: ZLayer[Blocking with Has[DbConfig], Throwable, Has[Repository]] = ZLayer.fromManaged {
    for {
      cfg        <- DbConfig.getDbConfig.toManaged_
      transactor <- Repository.mkTransactor(cfg)
    } yield new ActorRepositoryLive(transactor)
  }
}
