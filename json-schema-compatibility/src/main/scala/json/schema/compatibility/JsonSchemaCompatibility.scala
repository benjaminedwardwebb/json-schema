package json.schema.compatibility

import json._
import json.schema._

/** Defines functionality for comparing two json schemas and checking their
  * compatiblity.
  * @todo Only checks "pair-wise" keyword compatibility for now, and does not
  * check semantic compatibility of a keyword against all potentially relevant
  * keywords in the corresponding other schema. For example, compatibility of
  * one schema's {{{minimum}}} keyword is compared to the other schema's
  * {{{minimum}}} keyword, but not to the other schema's {{{maximum}}} keyword
  * (e.g., to compare that the {{{minimum}}} is not greater than the
  * {{{maximum}}}). This "pair-wise" keyword compatibility is likely the most
  * useful when comparing compatibility between different versions of an
  * evolving schema, since a schema would likely* need to evolve into a degenerate
  * schema (i.e., a schema that can validate no JSON instances) in order to
  * cause a compatiblity issue that is not captured by "pair-wise" keyword
  * compatibility, and these evolutions into degenerate schemas should be a
  * more obvious bug by the schema author than the more subtle cases in which
  * a schema evolves to a version that will likley break downstream readers.
  */
object JsonSchemaCompatibility {

  def isCompatibleWith(writer: JsonSchema, reader: JsonSchema): CompatibilityOutput =
    isCompatibleWith(asJsonSchemaObject(writer), asJsonSchemaObject(reader))

  // See https://json-schema.org/draft/2020-12/json-schema-core.html#rfc.section.4.3.2
  private def asJsonSchemaObject(schema: JsonSchema): JsonSchemaObject =
    schema match {
      case JsonSchemaBoolean.True => JsonSchemaObject.EmptySchema
      case JsonSchemaBoolean.False =>
        JsonSchemaObject.EmptySchema
          .copy(not = Some(JsonSchemaObject.EmptySchema))
      case schema: JsonSchemaObject => schema
    }

  private def isCompatibleWith(writer: JsonSchemaObject, reader: JsonSchemaObject): CompatibilityOutput = {
    optionally(writer.`type`, isTypeCompatibleWithType, reader.`type`) and
    optionally(writer.`enum`, isEnumCompatibleWithEnum, reader.`enum`) and
    optionally(writer.const, isConstCompatibleWithConst, reader.const) and
    optionally(writer.multipleOf, isMultipleOfCompatibleWithMultipleOf, reader.multipleOf) and
    optionally(writer.maximum, isMaximumCompatibleWithMaximum, reader.maximum) and
    optionally(writer.exclusiveMaximum, isExclusiveMaximumCompatibleWithExclusiveMaximum, reader.exclusiveMaximum) and
    optionally(writer.minimum, isMinimumCompatibleWithMinimum, reader.minimum) and
    optionally(writer.exclusiveMinimum, isExclusiveMinimumCompatibleWithExclusiveMinimum, reader.exclusiveMinimum) and
    optionally(writer.maxLength, isMaxLengthCompatibleWithMaxLength, reader.maxLength) and
    optionally(writer.minLength, isMinLengthCompatibleWithMinLength, reader.minLength) and
    optionally(writer.pattern, isPatternCompatibleWithPattern, reader.pattern) and
    optionally(writer.maxItems, isMaxItemsCompatbileWithMaxItems, reader.maxItems) and
    optionally(writer.minItems, isMinItemsCompatibleWithMinItems, reader.minItems) and
    optionally(writer.uniqueItems, isUniqueItemsCompatibleWithUniqueItems, reader.uniqueItems) and
    optionally(writer.maxContains, isMaxContainsCompatibleWithMaxContains, reader.maxContains) and
    optionally(writer.minContains, isMinContainsCompatibleWithMinContains, reader.minContains) and
    optionally(writer.maxProperties, isMaxPropertiesCompatibleWithMaxProperties, reader.maxProperties) and
    optionally(writer.minProperties, isMinPropertiesCompatibleWithMinProperties, reader.minProperties) and
    optionally(writer.required, isRequiredCompatibleWithRequired, reader.required) and
    optionally(writer.dependentRequired, isDependentRequiredCompatibleWithDependentRequired, reader.dependentRequired)
  }

  import json.schema.compatibility.CompatibilityOutput.{compatible, incompatible}

  private def optionally[A, B](
    writer: Option[A],
    isCompatibleWithFunction: (A, B) => CompatibilityOutput,
    reader: Option[B]
  ): CompatibilityOutput =
    (writer, reader) match {
      case (None, None) => compatible
      case (Some(_), None) => compatible
      case (None, Some(_)) => incompatible
      case (Some(writer), Some(reader)) => isCompatibleWithFunction(writer, reader)
    }

  private def isTypeCompatibleWithType(
    writer: Either[SimpleType, Seq[SimpleType]],
    reader: Either[SimpleType, Seq[SimpleType]]
  ): CompatibilityOutput = {
    import json.schema.utils.EitherExtensions._
    val writerTypes = writer.leftMap(Seq(_)).merge.toSet
    val readerTypes = writer.leftMap(Seq(_)).merge.toSet
    if (writerTypes.subsetOf(readerTypes)) compatible
    else incompatible
  }

  private def isEnumCompatibleWithEnum(
    writer: Seq[Json],
    reader: Seq[Json]
  ): CompatibilityOutput = {
    val writerEnum = writer.toSet
    val readerEnum = reader.toSet
    if (writerEnum.subsetOf(readerEnum)) compatible
    else incompatible
  }
    
  private def isConstCompatibleWithConst(writer: Json, reader: Json): CompatibilityOutput =
    if (writer == reader) compatible
    else incompatible

  def isMultipleOfCompatibleWithMultipleOf(writer: Double, reader: Double): CompatibilityOutput =
    if (writer / reader == 0) compatible
    else incompatible

  def isMaximumCompatibleWithMaximum(writer: Double, reader: Double): CompatibilityOutput =
    if (writer <= reader) compatible
    else incompatible

  def isExclusiveMaximumCompatibleWithExclusiveMaximum(writer: Double, reader: Double): CompatibilityOutput =
    if (writer <= reader) compatible
    else incompatible

  def isMinimumCompatibleWithMinimum(writer: Double, reader: Double): CompatibilityOutput =
    if (writer >= reader) compatible
    else incompatible

  def isExclusiveMinimumCompatibleWithExclusiveMinimum(writer: Double, reader: Double): CompatibilityOutput =
    if (writer >= reader) compatible
    else incompatible

  def isMaxLengthCompatibleWithMaxLength(writer: Long, reader: Long): CompatibilityOutput =
    if (writer <= reader) compatible
    else incompatible

  def isMinLengthCompatibleWithMinLength(writer: Long, reader: Long): CompatibilityOutput =
    if (writer >= reader) compatible
    else incompatible

  def isPatternCompatibleWithPattern(writer: String, reader: String): CompatibilityOutput =
    ???

  def isMaxItemsCompatbileWithMaxItems(writer: Long, reader: Long): CompatibilityOutput =
    if (writer <= reader) compatible
    else incompatible

  def isMinItemsCompatibleWithMinItems(writer: Long, reader: Long): CompatibilityOutput =
    if (writer >= reader) compatible
    else incompatible

  def isUniqueItemsCompatibleWithUniqueItems(writer: Boolean, reader: Boolean): CompatibilityOutput =
    if (writer == reader) compatible
    else if (writer) compatible
    else incompatible

  def isMaxContainsCompatibleWithMaxContains(writer: Long, reader: Long): CompatibilityOutput =
    if (writer <= reader) compatible
    else incompatible

  def isMinContainsCompatibleWithMinContains(writer: Long, reader: Long): CompatibilityOutput =
    if (writer >= reader) compatible
    else incompatible

  def isMaxPropertiesCompatibleWithMaxProperties(writer: Long, reader: Long): CompatibilityOutput =
    if (writer <= reader) compatible
    else incompatible

  def isMinPropertiesCompatibleWithMinProperties(writer: Long, reader: Long): CompatibilityOutput =
    if (writer >= reader) compatible
    else incompatible

  def isRequiredCompatibleWithRequired(writer: Set[String], reader: Set[String]): CompatibilityOutput =
    if (writer.subsetOf(reader)) compatible
    else incompatible

  def isDependentRequiredCompatibleWithDependentRequired(
    writer: Map[String, Seq[String]],
    reader: Map[String, Seq[String]]
  ): CompatibilityOutput =
    ???

}
