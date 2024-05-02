package com.nlevchenko.sccZioApp.domain.pet

import com.nlevchenko.sccZioApp.config.DbConfig
import com.nlevchenko.sccZioApp.repository.Repository
import doobie.implicits._
import doobie.util.fragment
import doobie.util.transactor.Transactor
import zio._
import zio.blocking.Blocking
import zio.interop.catz._

final case class PetRepositoryLive(xa: Transactor[Task]) extends Repository {

  override def getPets: UIO[List[Pet]] = {
    val getPetsSQL: fragment.Fragment = sql"SELECT * FROM pet"
    getPetsSQL.query[Pet].stream.compile.toList.transact(xa).orDie
  }

  // sqlx for Rust
  override def putPet(id: Long, name: String, status: String, tags: String): UIO[String] = {
    val putPetSQL: fragment.Fragment = sql"INSERT INTO pet (id, name, status, tags) VALUES ($id, $name, $status, $tags)"
    putPetSQL.update.run
      .transact(xa)
      .map {
        case result if result > 0 => "Pet created"
      }
      .orDie
  }

  override def updatePet(id: Long, name: String, status: String, tags: String): UIO[String] = {
    val updatePetSQL = sql"UPDATE pet SET id = $id, name = '$name', status= '$status', tags= '$tags' WHERE id = $id"
    updatePetSQL.update.run
      .transact(xa)
      .map {
        case result if result > 0 => "Pet updated"
      }
      .orDie
  }

  override def deletePet(id: Long): UIO[String] = {
    val deletePetSQL =  sql"DELETE FROM pet WHERE id=$id"
    deletePetSQL.update.run.transact(xa).map {
      case result if result > 0 => "Pet deleted"
    }.orDie
  }
}

object PetRepositoryLive {
  val layer: ZLayer[Blocking with Has[DbConfig], Throwable, Has[Repository]] = ZLayer.fromManaged {
    for {
      cfg        <- DbConfig.getDbConfig.toManaged_
      transactor <- Repository.mkTransactor(cfg)
    } yield new PetRepositoryLive(transactor)
  }
}
