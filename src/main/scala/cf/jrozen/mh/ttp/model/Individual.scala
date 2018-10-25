package cf.jrozen.mh.ttp.model


import cf.jrozen.mh.ttp.utils.ValueCache
import cf.jrozen.mh.ttp.{GreedyKnapsackSolver, mutable}

import scala.util.Random

case class Individual(private val locations: Array[Int])(implicit context: Context) extends Ordered[Individual] {

  def value: Double = valueCache.get

  private val valueCache = ValueCache[Double](() => valueInit)

  private def valueInit = {
    val items = GreedyKnapsackSolver.chooseItems(context, this.locations)
    context.calculate(this.locations, items)
  }

  @mutable
  def mutableSwap(idx1: Int, idx2: Int): Individual = {
    val x = locations(idx1)
    locations(idx1) = locations(idx2)
    locations(idx2) = x
    valueCache.invalidate
    this
  }

  def swap(idx1: Int, idx2: Int): Individual = {
    val swapped = locations.clone()
    val x = swapped(idx1)
    swapped(idx1) = swapped(idx2)
    swapped(idx2) = x
    Individual(swapped)
  }

  override def compare(that: Individual): Int = value.compareTo(that.value)

  def copy() = Individual(locations.clone())
}

object Individual {
  def random(implicit context: Context): Individual =
    Individual(Random.shuffle((0 until context.problem.dimension).toList).toArray)
}
