package com.nlevchenko.sccZioApp.domain.actor

import io.circe.Encoder
import io.circe.generic.semiauto._

final case class Actor(id: Int, name: String)

object Actor {
  implicit val encoder: Encoder[Actor] = deriveEncoder
}
