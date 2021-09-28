package json.schema.derivation

import scala.quoted._
import scala.deriving.Mirror
import scala.annotation.Annotation

private object JsonSchemaForDerivationMacro {

  def deriveMacro[T: Type](using Quotes): Expr[JsonSchemaFor[T]] = {
    val mirror: Expr[Mirror.Of[T]] = reflectMirrorOfT[T]
    val annotations: Expr[Seq[Annotation]] = reflectAnnotationsOnType[T]
    val _ = reflectAnnotationsOnCaseFieldsOfCaseClass[T]
    '{ derive[T]($annotations)(using $mirror) }
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

  inline def get[T]: Map[String, Seq[Annotation]] = ${ reflectAnnotationsOnCaseFieldsOfCaseClass }

  def reflectAnnotationsOnCaseFieldsOfCaseClass[T: Type](using Quotes): Expr[Map[String, Seq[Annotation]]] = {
    import quotes.reflect._
    val x = TypeRepr.of[T]
      .typeSymbol
      .caseFields
      .head
      val y = (x.name, x.annotations.map(_.asExpr).collect { case '{ $ann: Annotation } => ann })
      //.map(caseField => (caseField.annotations, caseField.isValDef, caseField.isTerm, caseField.name, caseField.tree))

    Debug.debugSingle(y)
    '{ Map.empty }
  }

  def reflectAnnotationsOnChildrenOfSealedTrait[T: Type](using Quotes): Expr[Map[String, Seq[Annotation]]] = ???

  inline def derive[T](annotations: Seq[Annotation])(using mirror: Mirror.Of[T]): JsonSchemaFor[T] = {
    val jsonSchemaForWithoutAnnotations: JsonSchemaFor[T] = JsonSchemaForDerivation.derive[T]
    JsonSchemaForDerivationWithAnnotations.applyAnnotations(jsonSchemaForWithoutAnnotations, annotations)
  }

}
