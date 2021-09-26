package json.schema.compatibility

case class CompatibilityOutput(flag: Boolean) {
  def and(that: CompatibilityOutput): CompatibilityOutput =
    CompatibilityOutput(this.flag && that.flag)
}

object CompatibilityOutput {
  def compatible: CompatibilityOutput = CompatibilityOutput(true)
  def incompatible: CompatibilityOutput = CompatibilityOutput(false)
}
