package cf.jrozen.mh.ttp

import scala.reflect.ClassTag

package object model {

  type JList[T] = java.util.List[T]
  type Matrix[T] = Array[Array[T]]

  def matrix[T <: AnyVal](dim: Int)(implicit classTag: ClassTag[T]) = Array.ofDim[T](dim, dim)

}
