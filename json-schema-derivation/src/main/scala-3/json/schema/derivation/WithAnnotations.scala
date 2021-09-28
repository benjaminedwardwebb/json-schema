
/*
 *package json.schema.derivation
 *private sealed trait WithAnnotationsTypeTags
 *private object WithAnnotationsTypeTags {
 *  type Tagged[A] = {type Tag = A}
 *
 *  type @@[+T, Tag] = T with Tagged[Tag]
 *
 *  extension [T](t: T)
 *    inline def taggedWith[A]: T @@ A  = t.asInstanceOf[T @@ A]
 *
 *  case object True extends WithAnnotationsTypeTags
 *  case object False extends WithAnnotationsTypeTags
 *
 *  type WithAnnotations = WithAnnotationsTypeTags.True.type
 *  type WithoutAnnotations = WithAnnotationsTypeTags.False.type
 *
 *  type JsonSchemaForWithAnnotations[T] = JsonSchemaFor[T] @@ WithAnnotations
 *  type JsonSchemaForWithoutAnnotations[T] = JsonSchemaFor[T] @@ WithoutAnnotations
 *}
 */
