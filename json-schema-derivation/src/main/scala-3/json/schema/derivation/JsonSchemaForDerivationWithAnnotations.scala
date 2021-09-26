package json.schema.derivation

import scala.Product
import json.schema._
import scala.quoted._

/** Essentially, this macro is a function from Scala's type system to JSON
  * schema's type system.
  */
private object JsonSchemaForDerivationWithAnnotations {

  def withAnnotations[T: Type](jsonSchemaFor: Expr[JsonSchemaFor[T]])(using Quotes): Expr[JsonSchemaFor[T]] = {
    import quotes.reflect._
    jsonSchemaFor
    /*
     *val T: TypeRepr = TypeRepr.of[T]
     *  //.asTerm
     *  .typeSymbol
     *  .annotations
     */
    jsonSchemaFor
  }

  /*
  def getAnnotationsOnExpr[T](expr: Expr[T])(using Quotes): List[Term] = {
    import quotes.reflect._
    expr.asTerm
      .symbol
      .annotations
  }
  */
  // Macro variant
  /*
   *def withAnnotations[T: Type](jsonSchemaFor: Expr[JsonSchemaFor[T]])(using Quotes): Expr[JsonSchemaFor[T]] = {
   *  ???
   *}
   */

/*
 *  def jsonSchemaFor[T: Type](using Quotes): Expr[JsonSchema] = {
 *    import quotes.reflect._
 *
 *    val T: TypeRepr = TypeRepr.of[T]
 *    val Nothing: TypeRepr = TypeRepr.of[Nothing]
 *    val Unit: TypeRepr = TypeRepr.of[Unit]
 *    val Boolean: TypeRepr = TypeRepr.of[Boolean]
 *    val Int: TypeRepr = TypeRepr.of[Int]
 *    val Product: TypeRepr = TypeRepr.of[Product]
 *    val Any: TypeRepr = TypeRepr.of[Any]
 *
 *    T match {
 *      case T if T <:< Nothing => jsonSchemaForNothing
 *      case T if T <:< Unit => ???
 *      case T if T <:< Boolean => jsonSchemaForBoolean
 *      case T if T <:< Product => jsonSchemaForProduct
 *      case T if T =:= Any => jsonSchemaForAny
 *      case _ => throw new RuntimeException("Could not match type")
 *    }
 *  }
 *
 *  def jsonSchemaForNothing: Expr[JsonSchema] = ???
 *
 *  def jsonSchemaForBoolean(using Quotes): Expr[JsonSchema] = {
 *    import quotes.reflect._
 *    '{ JsonSchemaObject.EmptySchema.copy(`type` = Some(Left(SimpleType.Boolean))) }
 *  }
 *
 *  def jsonSchemaForProduct: Expr[JsonSchema] = ???
 *
 *  given ToExpr[JsonSchemaBoolean] with {
 *    def apply(x: JsonSchemaBoolean)(using Quotes) = x match {
 *      case JsonSchemaBoolean.True => '{JsonSchemaBoolean.True}
 *      case JsonSchemaBoolean.False => '{JsonSchemaBoolean.False}
 *    }
 *  }
 *
 *  def jsonSchemaForAny(using Quotes): Expr[JsonSchema] = {
 *    import quotes.reflect._
 *    Expr[JsonSchemaBoolean](JsonSchemaBoolean.True)
 *  }
 */

}
