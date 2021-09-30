package json.schema.derivation

import scala.quoted._
import scala.deriving.Mirror
import scala.annotation.Annotation

private object JsonSchemaForDerivationMacro {

  def deriveMacro[T: Type](using Quotes): Expr[JsonSchemaFor[T]] = {
    val mirror: Expr[Mirror.Of[T]] = reflectMirrorOfT[T]
    val caseFieldAnnotations = reflectAnnotationsOnCaseFieldsOfCaseClass[T]
    val string = caseFieldAnnotations
    //Debug.debugSingle(caseFieldAnnotations)
    val annotations: Expr[Seq[Annotation]] = reflectAnnotationsOnType[T]
    '{ derive[T]($annotations, ${string}.toString)(using $mirror) }
  }

  def reflectMirrorOfT[T: Type](using Quotes): Expr[Mirror.Of[T]] = {
    import quotes.reflect.*
    Expr.summon[Mirror.Of[T]].get
  }

  def reflectAnnotationsOnType[T: Type](using Quotes): Expr[Seq[Annotation]] = {
    import quotes.reflect._
    val annotationExpressions = TypeRepr.of[T]
      .typeSymbol
      .annotations
      .map(_.asExprOf[Annotation])
    Varargs(annotationExpressions)
  }

  inline def get[T]: Seq[Annotation] = ${ reflectAnnotationsOnCaseFieldsOfCaseClass }

  //def reflectAnnotationsOnCaseFieldsOfCaseClass[T: Type](using Quotes): Expr[Seq[(String, Seq[Annotation])]] = {
  def reflectAnnotationsOnCaseFieldsOfCaseClass[T: Type](using Quotes): Expr[Seq[Annotation]] = {
    import quotes.reflect._

    def f(x: (String, Seq[Expr[Annotation]])): Expr[(String, Seq[Annotation])] = {
      val annotations = Varargs(x._2)
      val name = Expr(x._1)
      '{ ($name, $annotations) }
    }

    def g(y: Expr[Seq[(String, Seq[Annotation])]]): Expr[Map[String, Seq[Annotation]]] = {
      '{ $y.toMap }
    }

    val x = TypeRepr.of[T]
      .typeSymbol
      .primaryConstructor
      .paramSymss
      .map(_.map(
        _.annotations.map(_.asExprOf[Annotation])
      ))
      .flatten.flatten
      /*
       *.head
       *.head
       *.annotations
       *.toString
       */
      //.map(symbol => symbol.annotations.map(_.toString))
      //.flatten

      /*
       *Seq[(String, Seq[Expr[Annotation]])]
       *Seq[(String, Expr[Seq[Annotation]])]
       */

    /*
     *val y = Varargs(x)
     *y
     */
    Varargs(x)
  }

  def reflectAnnotationsOnChildrenOfSealedTrait[T: Type](using Quotes): Expr[Map[String, Seq[Annotation]]] = ???

  import json.schema._
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

  inline def derive[T](annotations: Seq[Annotation], debugString: String)(using mirror: Mirror.Of[T]): JsonSchemaFor[T] = {
    val jsonSchemaForWithoutAnnotations: JsonSchemaFor[T] = JsonSchemaForDerivation.derive[T]
    JsonSchemaForDerivationWithAnnotations.applyAnnotations(jsonSchemaForWithoutAnnotations, annotations)
      .mutateObject(_.copy(`$id` = Some(debugString)))
  }

}
