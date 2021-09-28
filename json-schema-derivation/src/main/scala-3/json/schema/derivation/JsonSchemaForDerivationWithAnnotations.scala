package json.schema.derivation

import json.schema._
import json.schema.derivation.annotations._
import scala.Product
import scala.annotation.Annotation
import scala.quoted._

/** Inspects Scala annotations on a type T and applies them as keywords to the 
  * type's JSON schema.
  *
  * Unfortunately, based on my limited understanding of Scala 3
  * metaprogramming, we cannot inspect annotations without using reflection.
  * Since this aspect of {{{JsonSchemaFor}}} derivation is implemented by a
  * separate Scala language feature (not type class deriviation, but
  * reflection), we house it in a separate module.
  *
  * @see [[https://dotty.epfl.ch/docs/reference/metaprogramming/reflection.html]]
  */
private object JsonSchemaForDerivationWithAnnotations {

  def applyAnnotations[T](jsonSchemaFor: JsonSchemaFor[T], annotations: Seq[Annotation]): JsonSchemaFor[T] = {
    annotations.foldLeft(jsonSchemaFor)((jsonSchemaFor, annotation) => applyAnnotation(jsonSchemaFor, annotation))
  }

  def applyAnnotation[T](jsonSchemaFor: JsonSchemaFor[T], annotation: Annotation): JsonSchemaFor[T] = {
    annotation match {
      case Maximum(value) => applyMaximumAnnotation(jsonSchemaFor, value)
      case Comment(value) => applyCommentAnnotation(jsonSchemaFor, value)
      case Description(value) => jsonSchemaFor.mutateObject(_.copy(description = Some(value)))
      case Title(value) => jsonSchemaFor.mutateObject(_.copy(title = Some(value)))
      case _ => jsonSchemaFor
    }
  }

  def applyMaximumAnnotation[T](jsonSchemaFor: JsonSchemaFor[T], value: Double): JsonSchemaFor[T] = {
    jsonSchemaFor.mutateObject(_.copy(maximum = Some(value)))
  }

  def applyCommentAnnotation[T](jsonSchemaFor: JsonSchemaFor[T], value: String): JsonSchemaFor[T] = {
    jsonSchemaFor.mutateObject(_.copy(`$comment` = Some(value)))
  }

  extension [T](jsonSchemaFor: JsonSchemaFor[T])
    def mutate(function: JsonSchema => JsonSchema): JsonSchemaFor[T] = 
      new JsonSchemaFor[T] {
        def jsonSchema: JsonSchema = function(jsonSchemaFor.jsonSchema)
      }

    def mutateObject(function: JsonSchemaObject => JsonSchemaObject): JsonSchemaFor[T] = {
      val f = (jsonSchema: JsonSchema) => jsonSchema match {
        case o: JsonSchemaObject => function(o)
        case _ => jsonSchema
      }
      jsonSchemaFor.mutate(f)
    }

}
