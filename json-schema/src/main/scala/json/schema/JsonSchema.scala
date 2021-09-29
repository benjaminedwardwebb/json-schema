package json.schema

import json.Json

/** A JSON schema document, or simply a JSON schema.
  *
  * Implemented as a sum type of boolean and object JSON schemas.
  *
  * @see [[https://json-schema.org/draft/2020-12/json-schema-core.html#rfc.section.4.3JSON Schema RFC, draft 2020-12, Section 4.3]] 
  */
sealed trait JsonSchema
object JsonSchema

/** A JSON schema document that is, itself, a boolean.
  *
  * Boolean JSON schemas are trivial, yet valid, JSON schemas that validate
  * everything (true) or nothing (false).
  *
  * @see [[https://json-schema.org/draft/2020-12/json-schema-core.html#rfc.section.4.3.2 JSON Schema RFC, draft 2020-12, Section 4.3.2]]
  */
sealed trait JsonSchemaBoolean extends JsonSchema
object JsonSchemaBoolean {
  case object True extends JsonSchemaBoolean
  case object False extends JsonSchemaBoolean
}

/** An JSON schema document that is, itself, an object.
  *
  * Object JSON schemas are the typical kind of JSON schema. The object's
  * properties are schema keywords, and they define how a schema validates
  * JSON instances.
  *
  * @see [[https://json-schema.org/draft/2020-12/json-schema-core.html#rfc.section.4.3.1 JSON Schema RFC, draft 2020-12, Section 4.3.1]]
  *
  * Object JSON schemas are modeled as a recursive product type. Each field is
  * a schema keyword, with only a few exceptions as discussed below. Since
  * keywords are optional, each field is wrapped in a Scala [[scala.Option]].
  * Some keywords, such as {{{description}}}, can be modeled by relatively
  * simple types, like [[java.lang.String]]. Other keywords, such as
  * {{{properties}}}, can define recursive JSON Schema document structures,
  * and so are modeled recursively with the [[json.schema.JsonSchema]] type.
  *
  * TODO describe extension keywords and schema vocabularies when implemented
  *
  * TODO describe minimalism and simple JSON serialization motivations behind
  * the current model, here and in the README
  *
  * TODO you can have type-specific assertion keywords on any schema node, but
  * they will only apply to that of the correct type -- find a citation for
  * this, if possible
  *
  * The supported keywords are taken from the various singe-vocabulary
  * meta-schemas available at [[https://json-schema.org/specification.html
  * json-schema.org]]. These keywords are documented here.
  *
  * Core Vocabulary
  * @see [[https://json-schema.org/draft/2020-12/meta/core this link downloads the Core Vocabulary meta-schema]]
  * @param $id 
  * @param $vocabulary
  * @param $comment
  * @param $defs
  *
  * Applicator Vocabulary
  * @see [[https://json-schema.org/draft/2020-12/meta/applicator this link downloads the Applicator Vocabulary meta-schema]]
  * @param prefixItems https://json-schema.org/draft/2020-12/json-schema-core.html#rfc.section.10.3.1.1
  * @param items https://json-schema.org/draft/2020-12/json-schema-core.html#rfc.section.10.3.1.2
  * @param contains https://json-schema.org/draft/2020-12/json-schema-core.html#rfc.section.10.3.1.3
  * @param additionalProperties https://json-schema.org/draft/2020-12/json-schema-core.html#rfc.section.10.3.2.3
  * TODO
  * @param dependentSchema https://json-schema.org/draft/2020-12/json-schema-core.html#rfc.section.10.2.2.4
  * @param propertyNames https://json-schema.org/draft/2020-12/json-schema-core.html#rfc.section.10.3.2.4
  * @param if https://json-schema.org/draft/2020-12/json-schema-core.html#rfc.section.10.2.2.1
  * @param then https://json-schema.org/draft/2020-12/json-schema-core.html#rfc.section.10.2.2.2
  * @param else https://json-schema.org/draft/2020-12/json-schema-core.html#rfc.section.10.2.2.3
  * TODO
  * @param not https://json-schema.org/draft/2020-12/json-schema-core.html#rfc.section.10.2.1.4
  *
  * Validation Vocabulary
  * @see [[https://json-schema.org/draft/2020-12/meta/validation this link downloads the Validation Vocabulary meta-schema]]
  * TODO
  * @param maximum https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.6.2.2
  * @param exclusiveMaximum https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.6.2.3
  * @param minimum https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.6.2.4
  * @param exclusiveMinimum https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.6.2.5
  * @param maxLength https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.6.3.1
  * @param minLength https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.6.3.2
  * @param pattern https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.6.3.3
  * @param maxItems https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.6.4.1
  * @param minItems https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.6.4.2
  * @param uniqueItems https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.6.4.3
  * TODO
  *
  * Meta-Data Vocabulary
  * @see [[https://json-schema.org/draft/2020-12/meta/meta-data this link downloads the Meta-Data Vocabulary meta-schema]]
  * @param title https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.9.1
  * @param description https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.9.1
  * TODO
  * @param deprecated https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.9.3
  * @param readOnly https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.9.4
  * @param writeOnly https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.9.4
  * @param examples https://json-schema.org/draft/2020-12/json-schema-validation.html#rfc.section.9.5
  *
  * Extension Keywords and Extension Schema Vocabularies
  * TODO
  */
case class JsonSchemaObject(
  // core vocabulary
  `$id`: Option[String], 
  `$vocabulary`: Option[Map[String, Boolean]], // TODO special format required for keys
  `$comment`: Option[String],
  `$defs`: Option[Map[String, JsonSchema]],

  // applicator vocabulary
  prefixItems: Option[Seq[JsonSchema]], // TODO min items 1
  items: Option[JsonSchema],
  contains: Option[JsonSchema],
  additionalProperties: Option[JsonSchema], // TODO check the defiinition of this keyword
  properties: Option[Map[String, JsonSchema]], // TODO
  patternProperties: Option[Map[String, JsonSchema]], // TODO String should be Regex format
  dependentSchema: Option[Map[String, JsonSchema]], // TODO the default is an empty object, `{}`, as per applicator meta-schema
  propertyNames: Option[JsonSchema],
  `if`: Option[JsonSchema],
  `then`: Option[JsonSchema],
  `else`: Option[JsonSchema],
  allOf: Option[Seq[JsonSchema]], // TODO min items 1
  anyOf: Option[Seq[JsonSchema]], // TODO min items 1
  oneOf: Option[Seq[JsonSchema]], // TODO min items 1
  not: Option[JsonSchema],
  
  // validation vocabulary
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

  // meta-data vocabulary
  title: Option[String],
  description: Option[String],
  default: Option[Json],
  deprecated: Option[Boolean], // TODO default false
  readOnly: Option[Boolean], // TODO default false
  writeOnly: Option[Boolean], // TODO default false
  examples: Option[Seq[Json]],

  // extensions
  extensionKeywords: Option[Seq[Unit]] // TODO this remains unimplemented for now
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
    examples = None,
    extensionKeywords = None
  )
}
