package json.schema.derivation.annotations

import scala.annotation.StaticAnnotation

/** https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.6.3.3
  */
final case class Pattern(value: String) extends StaticAnnotation
