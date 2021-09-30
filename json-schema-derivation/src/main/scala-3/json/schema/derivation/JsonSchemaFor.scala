package json.schema.derivation

import json.schema.JsonSchema

/** A type class witnessing the existence of a JSON schema for {{{T}}}. */
trait JsonSchemaFor[T] {
  def jsonSchema: JsonSchema
}

object JsonSchemaFor {
  inline given derived[T]: JsonSchemaFor[T] =
    ${ JsonSchemaForDerivationMacro.deriveMacro[T] }
}
