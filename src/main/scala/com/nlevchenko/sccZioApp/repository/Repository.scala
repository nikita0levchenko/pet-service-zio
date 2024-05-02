package com.nlevchenko.sccZioApp.repository

import cats.effect.Blocker
import com.nlevchenko.sccZioApp.config.DbConfig
import com.nlevchenko.sccZioApp.domain.pet.Pet
import doobie.hikari._
import zio._
import zio.blocking.Blocking
import zio.interop.catz._
import zio.logging.Logging
import zio.logging.log

import java.time.LocalDateTime

trait Repository extends Serializable {
  def getUptime: UIO[LocalDateTime]                                                       = UIO(LocalDateTime.now())
  def getActors: UIO[List[String]]                                                        = UIO(List.empty)
  def getPets: UIO[List[Pet]]                                                             = UIO(List.empty)
  def putPet(id: Long, name: String, status: String = "", tags: String = ""): UIO[String] = UIO("")
  def updatePet(id: Long, name: String, status: String = "", tags: String = ""): UIO[String] = UIO("")
  def deletePet(id: Long): UIO[String] = UIO("")

}

object Repository extends Serializable {
  def getUptime: URIO[Has[Repository], LocalDateTime] = ZIO.serviceWith[Repository](_.getUptime)
  def getActors: URIO[Has[Repository], List[String]]  = ZIO.serviceWith[Repository](_.getActors)
  def getPets: URIO[Has[Repository], List[Pet]] = ZIO.serviceWith[Repository](_.getPets)
  def putPet(id: Long, name: String, status: String = "", tags: String = ""): URIO[Has[Repository], String]  = ZIO.serviceWith[Repository](_.putPet(id, name, status, tags))
  def updatePet(id: Long, name: String, status: String = "", tags: String = ""): URIO[Has[Repository], String]  = ZIO.serviceWith[Repository](_.updatePet(id, name, status, tags))
  def deletePet(id: Long): URIO[Has[Repository], String] = ZIO.serviceWith[Repository](_.deletePet(id))


  def withTracing[RIn, ROut <: Has[Repository] with Logging, E](layer: ZLayer[RIn, E, ROut]): ZLayer[RIn, E, ROut] =
    layer >>> ZLayer.fromFunctionMany[ROut, ROut] { env =>
      def trace(call: => String) = log.trace(s"Repository.$call")

      env.update[Repository] { service =>
        new Repository {
          override val getUptime: UIO[LocalDateTime]                                          = (trace("getUptime") *> service.getUptime).provide(env)
          override val getActors: UIO[List[String]]                                           = (trace("getActors") *> service.getActors).provide(env)
          override val getPets: UIO[List[Pet]]                                                = (trace("getPets") *> service.getPets).provide(env)
          override def putPet(id: Long, name: String, status: String = "", tags: String = ""): UIO[String] =
            (trace("putPet") *> service.putPet(id, name, status, tags)).provide(env)
          override def updatePet(id: Long, name: String, status: String, tags: String): UIO[String] = (trace("updatePet") *> service.updatePet(id, name, status, tags)).provide(env)
          override def deletePet(id: Long): UIO[String] = (trace("deletePet") *> service.deletePet(id)).provide(env)
        }
      }
    }

  def mkTransactor(cfg: DbConfig): ZManaged[Blocking, Throwable, HikariTransactor[Task]] =
    ZIO.runtime[Blocking].toManaged_.flatMap { implicit rt =>
      for {
        transactEC <- Managed.succeed(rt.environment.get[Blocking.Service].blockingExecutor.asEC)
        transactor <- HikariTransactor
                        .newHikariTransactor[Task](
                          cfg.driver,
                          cfg.url,
                          cfg.user,
                          cfg.password,
                          rt.platform.executor.asEC,
                          Blocker.liftExecutionContext(transactEC)
                        )
                        .toManaged
      } yield transactor
    }
}
