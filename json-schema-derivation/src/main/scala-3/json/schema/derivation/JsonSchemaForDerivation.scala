package json.schema.derivation

import scala.deriving.Mirror
import json.schema._

/** Derives a {{{JsonSchemaFor}}} for a given type {{{T}}}, provided the type
  * meets Scala 3's conditions for type class derivation, i.e., is essentially
  * an ADT composed of sealed traits, case classes, enums.
  *
  * @see [[https://docs.scala-lang.org/scala3/reference/contextual/derivation.html]]
  * @see [[https://dotty.epfl.ch/docs/reference/contextual/derivation.html]]
  * @see [[https://blog.philipp-martini.de/blog/magic-mirror-scala3/]]
  */
private object JsonSchemaForDerivation {

  inline def derive[T](using mirror: Mirror.Of[T]): JsonSchemaFor[T] = {
    inline mirror match {
      case sum: Mirror.SumOf[T] => deriveSum[T](sum)
      case product: Mirror.ProductOf[T] => deriveProduct[T](product)
    }
  }

  inline def deriveSum[T](sum: Mirror.SumOf[T]): JsonSchemaFor[T] = ???

  inline def deriveProduct[T](
    product: Mirror.ProductOf[T]
  ): JsonSchemaFor[T] = {
    val jsonSchemaProperties = getProductJsonSchemaProperties(product)
    val jsonSchemaForProductWithoutAnnotations = new JsonSchemaFor[T] {
      def jsonSchema: JsonSchema = {
        JsonSchemaObject.EmptySchema
          .copy(`type` = Some(Left(SimpleType.Object)))
          .copy(properties = jsonSchemaProperties)
      }
    }

    jsonSchemaForProductWithoutAnnotations
  }

  inline def getProductJsonSchemaProperties[T](
    product: Mirror.ProductOf[T]
  ): Option[Map[String, JsonSchema]] = {
    val propertiesMap = getProductElementLabelsAndJsonSchemaFors[T](using product)
      .map({ case (label, jsonSchemaFor) => (label, jsonSchemaFor.jsonSchema) })
      .toMap

    if (propertiesMap.isEmpty) None
    else Some(propertiesMap)
  }

  inline def getProductElementLabelsAndJsonSchemaFors[T](using mirror: Mirror.ProductOf[T]): List[(String, JsonSchemaFor[_])] = {
    val elementLabels = getProductElementLabels[mirror.MirroredElemLabels]
    val elementJsonSchemaFors = getProductElementJsonSchemaFors[mirror.MirroredElemTypes]
    elementLabels.zip(elementJsonSchemaFors)
  }

  import scala.compiletime.{erasedValue, constValue, summonInline}

  inline def getProductElementLabels[T <: Tuple]: List[String] = {
    inline erasedValue[T] match {
      case _: EmptyTuple => Nil
      case _: (head *: tail) => 
        val headLabel = constValue[head].toString
        val tailLabels = getProductElementLabels[tail]
        headLabel :: tailLabels
    }
  }

  inline def getProductElementJsonSchemaFors[T <: Tuple]: List[JsonSchemaFor[_]] = {
    inline erasedValue[T] match {
      case _: EmptyTuple => Nil
      case _: (head *: tail) => 
        val headJsonSchemaFor = summonInline[JsonSchemaFor[head]]
        val tailJsonSchemaFors = getProductElementJsonSchemaFors[tail]
        headJsonSchemaFor :: tailJsonSchemaFors
    }
  }

}
