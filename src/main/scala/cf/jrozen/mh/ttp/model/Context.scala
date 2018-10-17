package cf.jrozen.mh.ttp.model

import scala.collection.mutable.ListBuffer
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
    val locationToItems: Map[Int, List[Item]] = items.toList.groupBy(_.assignedNodeNumber)

    val thief = new Thief(problem)

    val cost = (locationsOrder :+ locationsOrder.head).sliding(2)
      .map {
        case Array(l: Int, r: Int) => thief.take(locationToItems.getOrElse(l, List())).getSpeed() * distance(l)(r) * problem.rentingRatio
      }.sum
    thief.value - cost
  }

  class Thief(problem: Problem) {
    val knapsack = new ListBuffer[Item]()
    var knapsackValue: Double = _
    var knapsackWeight: Double = _

    def take(items: Iterable[Item]) = {
      knapsackValue += items.map(_.profit).sum
      knapsackWeight += items.map(_.weight).sum
      knapsack ++= items
      this
    }

    def getSpeed() = {
      problem.maxSpeed - (knapsackWeight * (problem.maxSpeed - problem.minSpeed) / problem.capacityOfKnapsack)
    }

    def value = knapsackValue

  }

}

object Context {


}
