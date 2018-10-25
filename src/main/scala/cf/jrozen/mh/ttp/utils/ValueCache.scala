package cf.jrozen.mh.ttp.utils

case class ValueCache[T](valueInitializer: () => T) {

  var value: Option[T] = None

  def set(newValue: T) = {
    value = Some(newValue)
  }

  def get = {
    if (value.isEmpty) {
      set(valueInitializer())
    }
    value.get
  }

  def invalidate = value = None

}
