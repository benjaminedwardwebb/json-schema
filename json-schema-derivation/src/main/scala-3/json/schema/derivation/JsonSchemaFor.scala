package json.schema.derivation

import json.schema.JsonSchema

/** A type class witnessing the existence of a JSON schema for {{{T}}}. */
trait JsonSchemaFor[T] {
  def jsonSchema: JsonSchema
}

object JsonSchemaFor {

  inline given derived[T]: JsonSchemaFor[T] =
    ${ JsonSchemaForDerivationMacro.deriveMacro[T] }

  /*
   *given derived[T: Type](using Quotes): Expr[Eq[T]] =
   *  ${ JsonSchemaForDerivation.deriveM[T] }
   */

  /*
   *import scala.deriving.Mirror
   *inline given derived[T](using Mirror.Of[T]): JsonSchemaFor[T] = JsonSchemaForDerivation.derive[T]
   */
}
