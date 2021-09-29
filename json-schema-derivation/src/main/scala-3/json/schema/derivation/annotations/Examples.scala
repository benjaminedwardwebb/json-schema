package json.schema.derivation.annotations

import json.Json
import scala.annotation.StaticAnnotation

/** https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.9.5
  */
final case class Examples(value: Seq[Json]) extends StaticAnnotation
