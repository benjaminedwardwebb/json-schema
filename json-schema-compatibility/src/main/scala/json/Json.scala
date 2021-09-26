package json

sealed trait Json
case object JsonNull extends Json
case class JsonBoolean(boolean: Boolean) extends Json
case class JsonString(string: String) extends Json
case class JsonNumber(double: Double) extends Json
case class JsonArray(array: Seq[Json]) extends Json
case class JsonObject(`object`: Map[String, Json]) extends Json
