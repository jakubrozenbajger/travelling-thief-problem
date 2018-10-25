package cf.jrozen.mh.ttp.slover.tabu

import cf.jrozen.mh.ttp.model.Context
import cf.jrozen.mh.ttp.mutable

case class TabuBase()(implicit context: Context, params: TabuParameters) {

  private var moves = 0
  private val existenceMatrix = cf.jrozen.mh.ttp.model.matrix[Int](context.problem.dimension)

  @mutable
  def move(x: Int, y: Int): Unit = {
    moves += 1
    if (value(x, y) < 0)
      modify(_ => params.tabuDuration)(x, y)
    else
      modify(_ + params.tabuDuration)(x, y)
  }

  def isEmpty(x: Int, y: Int): Boolean = value(x, y) <= 0

  @mutable
  def modify(func: Int => Int)(x: Int, y: Int): Unit = existenceMatrix(x)(y) = func(value(x, y))

  def value(x: Int, y: Int): Int = existenceMatrix(x)(y) - moves

}
