package json.schema.utils

object EitherExtensions {
  implicit class EitherExtensionOps[A, B](either: Either[A, B]) {
    def leftMap[C](f: A => C) =
      either.left.map(f)
  }
}
