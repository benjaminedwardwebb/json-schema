package json.schema.derivation.supported

import json.schema.derivation.JsonSchemaFor
import json.schema._

object JsonSchemaForString extends JsonSchemaFor[String] {
  def jsonSchema: JsonSchema =
    JsonSchemaObject.EmptySchema
      .copy(`type` = Some(Left(SimpleType.String)))
}
