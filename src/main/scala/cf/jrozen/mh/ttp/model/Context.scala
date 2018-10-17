package cf.jrozen.mh.ttp.model

import scala.util.Random

case class Context(
                    problem: Problem,
                    parameters: Parameters
                  ) {

  type Matrix = Array[Array[Double]]

  lazy val distance: Matrix = {
    {
      for {
        n1 <- problem.nodes
        n2 <- problem.nodes
      } yield Math.hypot(n1.x - n2.x, n1.y - n2.y)
    }
      .sliding(problem.nodes.length, problem.nodes.length)
      .map(_.toArray)
      .toArray
  }

  def nextIntInDims: Int = Random.nextInt(problem.dimension)

  def `mutate?`: Boolean = nextMutate

  def nextMutate: Boolean = Random.nextDouble() < parameters.mutationProbability

  def `crossover?`: Boolean = nextCrossover

  def nextCrossover: Boolean = Random.nextDouble() < parameters.crossoverProbability

  def nextInt(int: Int): Int = Random.nextInt(int)

  def calculate(e: Array[Int]): Double = {
    e.zip(e.tail :+ e.head)
      .map {
        case (l: Int, r: Int) => distance(l)(r)
      }.sum
  }

  def calculate(locationsOrder: Array[Int], items: Array[Item]): Double = {
    val locationToItems = items.groupBy(_.assignedNodeNumber)

    (locationsOrder :+ locationsOrder.head).sliding(2)
      .map {
        case Array(l: Int, r: Int) => locationToItems(l).map(i => i.profit).sum + distance(l)(r)
      }.sum
  }

}

object Context {


}
