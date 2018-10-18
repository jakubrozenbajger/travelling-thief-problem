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


  def calculate(locationsOrder: Array[Int], items: java.util.Set[Item]): Double = {
    import collection.JavaConverters._
    calculate(locationsOrder, items.asScala.toSet)
  }

  def calculate(locationsOrder: Array[Int], items: Set[Item]): Double = {
    val locationToItems: Map[Int, Set[Item]] = items.groupBy(_.assignedNodeNumber)

    def itemsInLocation(location: Int) = locationToItems.getOrElse(location, Set())

    val thief = new Thief(problem)

    val rentingCost = (locationsOrder :+ locationsOrder.head).sliding(2)
      .map {
        case Array(from: Int, to: Int) => thief.steal(itemsInLocation(from)).getSpeed() * distance(from)(to) * problem.rentingRatio
      }.sum

    thief.stolenValue - rentingCost
  }

  class Thief(problem: Problem) {
    val knapsack = new ListBuffer[Item]()
    var knapsackValue: Double = _
    var knapsackWeight: Double = _

    def steal(items: Iterable[Item]): Thief = {
      knapsackValue += items.map(_.profit).sum
      knapsackWeight += items.map(_.weight).sum
      knapsack ++= items
      this
    }

    def getSpeed(): Double = {
      problem.maxSpeed - (knapsackWeight * (problem.maxSpeed - problem.minSpeed) / problem.capacityOfKnapsack)
    }

    def stolenValue: Double = knapsackValue

  }

}

object Context {


}
