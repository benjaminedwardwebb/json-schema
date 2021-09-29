package json.schema.derivation.annotations

import scala.annotation.StaticAnnotation

final case class Vocabulary(value: Map[String, Boolean]) extends StaticAnnotation
