package json.schema

sealed trait SimpleType
case object SimpleType {
  case object Array extends SimpleType
  case object Boolean extends SimpleType
  case object Integer extends SimpleType
  case object Null extends SimpleType
  case object Number extends SimpleType
  case object Object extends SimpleType
  case object String extends SimpleType
}
