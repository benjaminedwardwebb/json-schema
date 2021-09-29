package json.schema.derivation.annotations

import scala.annotation.StaticAnnotation

/** https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.6.2.3
  */
final case class ExclusiveMaximum(value: Double) extends StaticAnnotation
