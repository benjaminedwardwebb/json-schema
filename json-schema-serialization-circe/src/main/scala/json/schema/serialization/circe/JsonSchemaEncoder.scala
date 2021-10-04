package json.schema.serialization.circe

import json.schema._
import io.circe._
import io.circe.generic.semiauto._

/** @see [[https://circe.github.io/circe/codecs/semiauto-derivation.html]] */
object JsonSchemaEncoder {
  implicit val jsonDecoder: Decoder[Json] = deriveDecoder[json.Json]
  implicit val jsonEncoder: Encoder[Json] = deriveEncoder[json.Json]
  implicit val jsonSchemaDecoder: Decoder[JsonSchemaObject] = deriveDecoder[JsonSchemaObject]
  implicit val jsonSchemaEncoder: Encoder[JsonSchemaObject] = deriveEncoder[JsonSchemaObject]
}
