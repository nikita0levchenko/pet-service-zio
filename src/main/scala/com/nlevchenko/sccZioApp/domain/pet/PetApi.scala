package com.nlevchenko.sccZioApp.domain.pet

import com.nlevchenko.sccZioApp.repository.Repository
import io.circe.Encoder
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import zio._
import zio.interop.catz._

object PetApi {

  def routes[R <: Has[Repository]]: HttpRoutes[RIO[R, *]] = {
    type Task[A] = RIO[R, A]

    val dsl: Http4sDsl[Task] = Http4sDsl[Task]
    import dsl._

    implicit def circeJsonEncoder[A: Encoder]: EntityEncoder[Task, A] = jsonEncoderOf[Task, A]

    object IdQueryParamMatcher     extends OptionalValidatingQueryParamDecoderMatcher[Long]("id")
    object NameQueryParamMatcher   extends QueryParamDecoderMatcher[String]("name")
    object StatusQueryParamMatcher extends QueryParamDecoderMatcher[String]("status")
    object TagsQueryParamMatcher   extends QueryParamDecoderMatcher[String]("tags")

    HttpRoutes.of[Task] {
      // http://127.0.0.1:8083/pets
      case GET -> Root                                            => Ok(Repository.getPets)
      // http://127.0.0.1:8083/pets/add?id=12&name=Martin&status=Ok&tags=sometags
      case GET -> Root / "add" :? IdQueryParamMatcher(maybeId) +&
        NameQueryParamMatcher(name) +&
        StatusQueryParamMatcher(status) +&
        TagsQueryParamMatcher(tags) =>
        maybeId match {
          case Some(foldId) =>
            foldId.fold(
              _ => BadRequest("Id isn't formatted properly"),
              id => Ok(Repository.putPet(id, name, status, tags))
            )
          case None         => BadRequest("Id isn't formatted properly")
        }
      // http://127.0.0.1:8083/pets/update?id=2&name=NewName&status=NewStatus&tags=NewTags
      case GET -> Root / "update" :? IdQueryParamMatcher(maybeId) +&
        NameQueryParamMatcher(name) +&
        StatusQueryParamMatcher(status) +&
        TagsQueryParamMatcher(tags) =>
        maybeId match {
          case Some(foldId) =>
            foldId.fold(
              _ => BadRequest("Id isn't formatted properly"),
              id => Ok(Repository.updatePet(id, name, status, tags))
            )
          case None         => BadRequest("Id isn't formatted properly")
        }
      // http://127.0.0.1:8083/pets/delete?id=2
      case GET -> Root / "delete" :? IdQueryParamMatcher(maybeId) =>
        maybeId match {
          case Some(foldId) =>
            foldId.fold(
              _ => BadRequest("Id isn't formatted properly"),
              id => Ok(Repository.deletePet(id))
            )
          case None         => BadRequest("Id isn't formatted properly")
        }
    }
  }

}
