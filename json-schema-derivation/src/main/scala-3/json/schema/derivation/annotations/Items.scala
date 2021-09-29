package json.schema.derivation.annotations

import json.schema.JsonSchema
import scala.annotation.StaticAnnotation

/** https://json-schema.org/draft/2020-12/json-schema-core.html#rfc.section.10.3.1.2
  */
final case class Items(value: JsonSchema) extends StaticAnnotation
