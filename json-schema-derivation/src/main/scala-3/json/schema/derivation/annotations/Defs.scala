package json.schema.derivation.annotations

import json.schema.JsonSchema
import scala.annotation.StaticAnnotation

final case class Defs(value: Map[String, JsonSchema]) extends StaticAnnotation
