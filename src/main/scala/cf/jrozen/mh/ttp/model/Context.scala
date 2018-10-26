package cf.jrozen.mh.ttp.model

import cats.Monoid

import scala.collection.mutable.ListBuffer
import scala.util.Random

case class Context(problem: Problem) {

  def nextIntInDims: Int = Random.nextInt(problem.dimension)

  lazy val distance: Matrix[Double] = {
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

  private val locationToItemsMap: Map[Int, Set[Item]] = problem.items.groupBy(_.assignedNodeNumber).mapValues(_.toSet)

  private val locationToAllItems: Int => Set[Item] = locationId => locationToItemsMap.getOrElse(locationId, Set.empty)

  private val locationToItemsArr: Array[Set[Item]] = Array.range(0, problem.dimension).map(locationToAllItems)

 private val locationToAllItemsOptimized: Int => Set[Item] = locationToItemsArr

  def calculate(locationsOrder: Array[Int], items: Set[Item]): Double = {
    var profit = 0.0
    var weight = 0.0
    var totalTime = 0.0
    locationsOrder.indices foreach { i =>
      val nodeId = locationsOrder(i)
      val nextNodeId = locationsOrder((i + 1) % locationsOrder.length)

      for (choosenItem <- locationToAllItemsOptimized(nodeId) if items.contains(choosenItem)) {
        profit += choosenItem.profit
        weight += choosenItem.weight
      }
      totalTime += distance(nodeId)(nextNodeId) / (problem.maxSpeed - weight * (problem.maxSpeed - problem.minSpeed) / problem.capacityOfKnapsack)
    }
    profit - totalTime * problem.rentingRatio
  }

  /*  def calculate(locationsOrder: Array[Int], items: Set[Item]): Double = {

      import cats.instances.set._
      val locationToItems = (l: Int) => withDefault(items.groupBy(_.assignedNodeNumber))(l)

      val thief = new Thief()

      val rentingCost = locationsOrder.zip(locationsOrder.tail :+ locationsOrder.head)
        .map {
          case (from: Int, to: Int) => thief.steal(locationToItems(from)).getSpeed() * distance(from)(to) * problem.rentingRatio
        }.sum

      thief.stolenValue - rentingCost
    }*/

  private def withDefault[K, V](map: Map[K, V])(k: K)(implicit V: Monoid[V]) = {
    map.getOrElse(k, V.empty)
  }

  class Thief {
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
