package json.schema.derivation

import json.schema.JsonSchema

trait JsonSchemaFor[T] {
  def jsonSchema: JsonSchema
}

object JsonSchemaFor {
  import scala.deriving.Mirror
  inline given derived[T](using Mirror.Of[T]): JsonSchemaFor[T] = JsonSchemaForDerivation.derive[T]
}
