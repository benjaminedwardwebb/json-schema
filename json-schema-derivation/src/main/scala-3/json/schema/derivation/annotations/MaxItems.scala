package json.schema.derivation.annotations

import scala.annotation.StaticAnnotation

/** https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.6.4.1
  */
final case class MaxItems(value: Long) extends StaticAnnotation
