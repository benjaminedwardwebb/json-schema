package json.schema.derivation.supported

import json.schema.derivation.JsonSchemaFor
import json.schema._

object JsonSchemaForBoolean extends JsonSchemaFor[Boolean] {
  def jsonSchema: JsonSchema =
    JsonSchemaObject.EmptySchema
      .copy(`type` = Some(Left(SimpleType.Boolean)))
}
