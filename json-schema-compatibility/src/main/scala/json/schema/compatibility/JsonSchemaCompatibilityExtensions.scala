package json.schema.compatibility

import json.schema._

object JsonSchemaCompatibilityExtensions {
  implicit class JsonSchemaCompatibilityOps(schema: JsonSchema) {
    def isCompatibleWith(reader: JsonSchema): CompatibilityOutput = JsonSchemaCompatibility.isCompatibleWith(schema, reader)
    def canBeReadBy(reader: JsonSchema): CompatibilityOutput = JsonSchemaCompatibility.isCompatibleWith(schema, reader)
    def canReadFrom(writer: JsonSchema): CompatibilityOutput = JsonSchemaCompatibility.isCompatibleWith(writer, schema)
    def canSafelyWriteTo(reader: JsonSchema): CompatibilityOutput = JsonSchemaCompatibility.isCompatibleWith(schema, reader)
    def ~(reader: JsonSchema): CompatibilityOutput = JsonSchemaCompatibility.isCompatibleWith(schema, reader)
  }
}
