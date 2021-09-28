package json.schema.derivation.annotations

import scala.annotation.StaticAnnotation
import scala.annotation.RefiningAnnotation

final case class Comment(value: String) extends RefiningAnnotation
