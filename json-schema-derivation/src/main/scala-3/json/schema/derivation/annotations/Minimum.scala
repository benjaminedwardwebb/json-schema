package json.schema.derivation.annotations

import scala.annotation.StaticAnnotation

/** https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.6.2.4
  */
final case class Minimum(value: Double) extends StaticAnnotation
