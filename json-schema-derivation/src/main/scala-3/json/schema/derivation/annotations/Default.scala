package json.schema.derivation.annotations

import json.Json
import scala.annotation.StaticAnnotation

/** https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.9.2
  */
final case class Default(value: Json) extends StaticAnnotation
