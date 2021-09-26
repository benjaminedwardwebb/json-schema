package json.schema

import org.scalacheck._
import Gen._
import Arbitrary.arbitrary

object ArbitraryJsonSchema {

  implicit val arbitraryJsonSchema: Arbitrary[JsonSchema] = Arbitrary(jsonSchemaGenerator)

  val jsonSchemaBooleanGenerator: Gen[JsonSchemaBoolean] = oneOf(
    JsonSchemaBoolean.False,
    JsonSchemaBoolean.True
  )

  // TODO remove instances of this degenerate generator
  val none = Gen.oneOf(Seq(None))
    
  val jsonSchemaObjectGenerator: Gen[JsonSchemaObject] = for {
    `$id` <- option(alphaStr)
    `$vocabulary` <- none
  } yield JsonSchemaObject.EmptySchema.copy(
    `$id` = `$id`,
    `$vocabulary` = `$vocabulary`
  )

  val jsonSchemaGenerator: Gen[JsonSchema] = oneOf(
    jsonSchemaBooleanGenerator,
    jsonSchemaObjectGenerator
  )

}
