package json.schema.compatibility

import json.schema._
import json.schema.ArbitraryJsonSchema.arbitraryJsonSchema
/*
 *import org.scalacheck.Prop.forAll
 *import org.scalacheck.Properties
 */
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers._
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks._

class JsonSchemaCompatibilitySpec extends AnyFlatSpec {

  import JsonSchemaCompatibilityExtensions._
  import CompatibilityOutput.{compatible, incompatible}

  val TrueSchema: JsonSchema = JsonSchemaBoolean.True
  val FalseSchema: JsonSchema = JsonSchemaBoolean.False
  val EmptySchema: JsonSchema = JsonSchemaObject.EmptySchema

  // This is only true for "pair-wise keyword compatibility"
  //property("Any schema is compatible with itself.") =
  "Any json schema" should "be compatible with itself" in {
    forAll { (schema: JsonSchema) =>
      schema ~ schema == compatible
    }
  }

  "The true json schema" should "be compatible with the empty json schema" in {
    TrueSchema ~ EmptySchema == compatible
  }

  "The false json schema" should "be compatible with all json schemas" in {
    forAll { (schema: JsonSchema) =>
      FalseSchema ~ schema == compatible
    }
  }

  "The true json schema" should "be incompatible with any json schema" in {
    forAll { (schema: JsonSchema) =>
      TrueSchema ~ schema == compatible
    }
  }

}
