package com.nlevchenko.sccZioApp.domain.uptime

import com.nlevchenko.sccZioApp.config.DbConfig
import com.nlevchenko.sccZioApp.repository.Repository
import doobie._
import doobie.implicits._
import doobie.postgres.implicits._
import doobie.util.transactor.Transactor
import zio._
import zio.blocking.Blocking
import zio.interop.catz._

import java.time.LocalDateTime

final case class UptimeRepositoryLive(xa: Transactor[Task]) extends Repository {
  private val getUptimeSQL: Query0[LocalDateTime] =
    sql"select current_timestamp::timestamp".query[LocalDateTime]

  override def getUptime: UIO[LocalDateTime] =
    getUptimeSQL.unique.transact(xa).orDie

}

object UptimeRepositoryLive {
  val layer: ZLayer[Blocking with Has[DbConfig], Throwable, Has[Repository]] =
    ZLayer.fromManaged {
      for {
        cfg        <- DbConfig.getDbConfig.toManaged_
        transactor <- Repository.mkTransactor(cfg)
      } yield new UptimeRepositoryLive(transactor)
    }
}
