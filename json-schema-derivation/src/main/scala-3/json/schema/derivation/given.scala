package json.schema.derivation

import json.schema.derivation.supported._

given JsonSchemaFor[Boolean] = JsonSchemaForBoolean
given JsonSchemaFor[Double] = JsonSchemaForDouble
given JsonSchemaFor[Float] = JsonSchemaForFloat
given JsonSchemaFor[Int] = JsonSchemaForInt
given JsonSchemaFor[Null] = JsonSchemaForNull
given JsonSchemaFor[Short] = JsonSchemaForShort
given JsonSchemaFor[String] = JsonSchemaForString

import scala.deriving.Mirror

inline given derived[T](using Mirror.Of[T]): JsonSchemaFor[T] = JsonSchemaForDerivation.derive[T]
