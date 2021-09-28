package json.schema.derivation

import scala.quoted._

object Debug {
  inline def debugSingle(inline expr: Any): Unit = ${debugSingleImpl('expr)} 
  
  private def debugSingleImpl(expr: Expr[Any])(using Quotes): Expr[Unit] =
    '{ println("Value of " + ${Expr(expr.show)} + " is " + $expr) }
}
