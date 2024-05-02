package com.nlevchenko.sccZioApp.domain.pet

import io.circe.Encoder
import io.circe.generic.semiauto._

final case class Pet(id: Long, name: String, status: String, tags: String)

object Pet {
  implicit val encoder: Encoder[Pet] = deriveEncoder
}
