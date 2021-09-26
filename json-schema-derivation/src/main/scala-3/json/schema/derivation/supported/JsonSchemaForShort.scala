package json.schema.derivation.supported

import json.schema.derivation.JsonSchemaFor
import json.schema._

object JsonSchemaForShort extends JsonSchemaFor[Short] {
  def jsonSchema: JsonSchema =
    JsonSchemaObject.EmptySchema
      .copy(`type` = Some(Left(SimpleType.Integer)))
}
