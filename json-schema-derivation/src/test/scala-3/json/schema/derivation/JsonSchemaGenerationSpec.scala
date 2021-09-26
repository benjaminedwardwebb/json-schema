package json.schema.derivation

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers._
import json.schema._

class JsonSchemaForDerivationSpec extends AnyFlatSpec {

  "The JsonSchemaFor type class" must "derive a json schema with type boolean for a Scala Boolean" in {
    val jsonSchema = summon[JsonSchemaFor[Boolean]].jsonSchema
    val expectedJsonSchema = JsonSchemaObject.EmptySchema.copy(`type` = Some(Left(SimpleType.Boolean)))
    jsonSchema mustBe expectedJsonSchema
  }

  it must "derive a json schema with type integer for a Scala Int" in {
    val jsonSchema = summon[JsonSchemaFor[Int]].jsonSchema
    val expectedJsonSchema = JsonSchemaObject.EmptySchema.copy(`type` = Some(Left(SimpleType.Integer)))
    jsonSchema mustBe expectedJsonSchema
  }

  it must "derive a json schema for a case class" in {
    import json.schema.derivation.given
    case class CaseClass(a: Boolean, b: Int) derives JsonSchemaFor

    val jsonSchema = summon[JsonSchemaFor[CaseClass]].jsonSchema
    val expectedJsonSchema = JsonSchemaObject.EmptySchema
      .copy(`type` = Some(Left(SimpleType.Object)))
      .copy(properties = Some(Map(
        "a" -> JsonSchemaObject.EmptySchema.copy(`type` = Some(Left(SimpleType.Boolean))),
        "b" -> JsonSchemaObject.EmptySchema.copy(`type` = Some(Left(SimpleType.Integer)))
      )))
    jsonSchema mustBe expectedJsonSchema
  }

  it must "derive a json schema for a nested case class" in {
    import json.schema.derivation.given
    case class NestedCaseClass(a: Boolean, b: Int) derives JsonSchemaFor
    case class CaseClass(x: NestedCaseClass, c: String) derives JsonSchemaFor

    val jsonSchema = summon[JsonSchemaFor[CaseClass]].jsonSchema

    val expectedNestedJsonSchema = JsonSchemaObject.EmptySchema
      .copy(`type` = Some(Left(SimpleType.Object)))
      .copy(properties = Some(Map(
        "a" -> JsonSchemaObject.EmptySchema.copy(`type` = Some(Left(SimpleType.Boolean))),
        "b" -> JsonSchemaObject.EmptySchema.copy(`type` = Some(Left(SimpleType.Integer)))
      )))
    val expectedJsonSchema = JsonSchemaObject.EmptySchema
      .copy(`type` = Some(Left(SimpleType.Object)))
      .copy(properties = Some(Map(
        "x" -> expectedNestedJsonSchema,
        "c" -> JsonSchemaObject.EmptySchema.copy(`type` = Some(Left(SimpleType.String)))
      )))

    jsonSchema mustBe expectedJsonSchema
  }

  it must "derive a json schema for a case class with a maximum annotation" in {
    import json.schema.derivation.given
    import json.schema.derivation.annotations._
    case class CaseClass(a: Boolean, @Maximum(10) b: Int) derives JsonSchemaFor

    val jsonSchema = summon[JsonSchemaFor[CaseClass]].jsonSchema
    val expectedJsonSchema = JsonSchemaObject.EmptySchema
      .copy(`type` = Some(Left(SimpleType.Object)))
      .copy(properties = Some(Map(
        "a" -> JsonSchemaObject.EmptySchema
          .copy(`type` = Some(Left(SimpleType.Boolean)))
          .copy(maximum = Some(10)),
        "b" -> JsonSchemaObject.EmptySchema.copy(`type` = Some(Left(SimpleType.Integer)))
      )))
    jsonSchema mustBe expectedJsonSchema
  }

}
