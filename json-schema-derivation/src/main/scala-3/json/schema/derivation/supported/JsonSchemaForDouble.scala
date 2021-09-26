package json.schema.derivation.supported

import json.schema.derivation.JsonSchemaFor
import json.schema._

object JsonSchemaForDouble extends JsonSchemaFor[Double] {
  def jsonSchema: JsonSchema =
    JsonSchemaObject.EmptySchema
      .copy(`type` = Some(Left(SimpleType.Number)))
}
