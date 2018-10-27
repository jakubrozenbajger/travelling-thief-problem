package cf.jrozen.mh.ttp.slover.tabu

import cf.jrozen.mh.ttp.model.{Context, Individual}
import cf.jrozen.mh.ttp.mutable

import scala.collection.mutable

case class TabuBase()(implicit context: Context, params: TabuParameters) {

  private var moves = 0
  private val existenceMatrix = cf.jrozen.mh.ttp.model.matrix[Int](context.problem.dimension)

  @mutable
  def move(x: Int, y: Int): Unit = {
    moves += 1
    existenceMatrix(x)(y) = 0.max(value(x, y)) + params.tabuDuration
    existenceMatrix(y)(x) = 0.max(value(y, x)) + params.tabuDuration
  }

  def canVisit(x: Int, y: Int): Boolean = value(x, y) <= 0

  //
  //  @mutable
  //  @inline
  //  def modify(func: Int => Int)(x: Int, y: Int): Unit = existenceMatrix(x)(y) = func(value(x, y))

  def value(x: Int, y: Int): Int = existenceMatrix(x)(y) - moves

  //  val base = new mutable.HashMap[Individual, Int]()
  val base = new mutable.LinkedHashSet[Individual]()

  //
  @mutable
  def move(indv: Individual): Unit = {
    base.add(indv)
    if (base.size >= params.tabuSize) {
      println(base.size)
      base.remove(base.last)
    }
  }

  def canVisit(indv: Individual): Boolean = {
    !base.contains(indv)
  }

}
