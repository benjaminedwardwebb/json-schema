package json.schema

import json.Json

sealed trait JsonSchema
object JsonSchema

sealed trait JsonSchemaBoolean extends JsonSchema
object JsonSchemaBoolean {
  case object True extends JsonSchemaBoolean
  case object False extends JsonSchemaBoolean
}

sealed trait SimpleType
case object SimpleType {
  case object Array extends SimpleType
  case object Boolean extends SimpleType
  case object Integer extends SimpleType
  case object Null extends SimpleType
  case object Number extends SimpleType
  case object Object extends SimpleType
  case object String extends SimpleType
}

// you can have type-specific assertion keywords at any schema node, but they
// will only apply to that of the correct type (and be undefined elsewhere) --
// cite if possible
case class JsonSchemaObject(
  // core vocab
  `$id`: Option[String], 
  `$vocabulary`: Option[Map[String, Boolean]], // TODO special format required for keys
  `$comment`: Option[String],
  `$defs`: Option[Map[String, JsonSchema]],

  // applicator vocab
  prefixItems: Option[Seq[JsonSchema]], // TODO min items 1
  items: Option[JsonSchema],
  contains: Option[JsonSchema],
  additionalProperties: Option[JsonSchema], // TODO check the defiinition of this keyword
  properties: Option[Map[String, JsonSchema]], // TODO
  patternProperties: Option[Map[String, JsonSchema]],
  dependentSchema: Option[Unit], // TODO
  propertyNames: Option[Unit], // TODO
  `if`: Option[JsonSchema], // TODO
  `then`: Option[JsonSchema], // TODO
  `else`: Option[JsonSchema], // TODO
  allOf: Option[Seq[JsonSchema]], // TODO min items 1
  anyOf: Option[Seq[JsonSchema]], // TODO min items 1
  oneOf: Option[Seq[JsonSchema]], // TODO min items 1
  not: Option[JsonSchema],
  
  // validation vocab
  `type`: Option[Either[SimpleType, Seq[SimpleType]]], // TODO simpleTypes are an enum
  `enum`: Option[Seq[Json]],
  const: Option[Json], // TODO the meta-schema seems to define this type as just "true"
  multipleOf: Option[Double],
  maximum: Option[Double],
  exclusiveMaximum: Option[Double],
  minimum: Option[Double],
  exclusiveMinimum: Option[Double],
  maxLength: Option[Long], // TODO non-negative
  minLength: Option[Long], // TODO non-negative, default 0
  pattern: Option[String],
  maxItems: Option[Long], // TODO non-negative
  minItems: Option[Long], // TODO non-negative, default 0
  uniqueItems: Option[Boolean], // TODO default false
  maxContains: Option[Long], // TODO non-negative
  minContains: Option[Long], // TODO non-negative, default 1
  maxProperties: Option[Long], // TODO non-negative
  minProperties: Option[Long], // TODO non-negative
  required: Option[Set[String]], // TODO unique items, default empty
  dependentRequired: Option[Map[String, Seq[String]]], // TODO unique items, default empty (on values)

  // meta-data vocab
  title: Option[String],
  description: Option[String],
  default: Option[Json], // TODO the meta-schema seems to define this type as just "true"?
  deprecated: Option[Boolean], // TODO default false
  readOnly: Option[Boolean], // TODO default false
  writeOnly: Option[Boolean], // TODO default false
  examples: Option[Seq[Json]] // TODO meta-schema defines "items" of the array as just "true"?
) extends JsonSchema

object JsonSchemaObject {
  val EmptySchema = JsonSchemaObject(
    `$id` = None,
    `$vocabulary` = None,
    `$comment` = None,
    `$defs` = None,
    prefixItems = None,
    items = None,
    contains = None,
    additionalProperties = None,
    properties = None,
    patternProperties = None,
    dependentSchema = None,
    propertyNames = None,
    `if` = None,
    `then` = None,
    `else` = None,
    allOf = None,
    anyOf = None,
    oneOf = None,
    not = None,
    `type` = None,
    `enum` = None,
    const = None,
    multipleOf = None,
    maximum = None,
    exclusiveMaximum = None,
    minimum = None,
    exclusiveMinimum = None,
    maxLength = None,
    minLength = None,
    pattern = None,
    maxItems = None,
    minItems = None,
    uniqueItems = None,
    maxContains = None,
    minContains = None,
    maxProperties = None,
    minProperties = None,
    required = None,
    dependentRequired = None,
    title = None,
    description = None,
    default = None,
    deprecated = None,
    readOnly = None,
    writeOnly = None,
    examples = None
  )
}
