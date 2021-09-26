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

}
