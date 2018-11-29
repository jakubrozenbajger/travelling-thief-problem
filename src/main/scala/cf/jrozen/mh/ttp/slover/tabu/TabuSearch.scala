package cf.jrozen.mh.ttp.slover.tabu

import cf.jrozen.mh.ttp.model.{Context, Individual}

import scala.collection.mutable.ListBuffer
import scala.util.Random

class TabuSearch()(implicit context: Context, params: TabuParameters) {

  val rnd = new Random()

  def getBestNeighbour(tabuBase: TabuBase, solution: Individual): Individual = {
    val dim = context.problem.dimension
    var best = solution.copy()
    var first = true

    val loglog = Math.log(Math.log(dim))

    for {
      i <- 1 until dim
      j <- 2 until dim
      if i != j
      if loglog > rnd.nextInt(dim)
    } {
      val curr = best.swap(i, j)
      if (tabuBase.canVisit(curr) && (first || curr > best)) {
        best = curr
        first = false
      }
    }
    tabuBase.move(best)
    best
  }

  def run: (List[Individual], List[Individual]) = {
    val tabuBase = TabuBase()
    val bestIndv = new ListBuffer[Individual]()
    val bestButOne = new ListBuffer[Individual]()

    var bestSolution = Individual.random
    var currentSolution = bestSolution.copy()

    for (i <- 0 until params.noOfIterations) {

      val bestNeighbour = getBestNeighbour(tabuBase, currentSolution)

      bestIndv += bestSolution
      bestButOne += bestNeighbour

      if (bestNeighbour > bestSolution) {
        bestSolution = bestNeighbour
      }
      currentSolution = bestNeighbour
      printProgress(i, params.noOfIterations)
    }

    (bestIndv.toList, bestButOne.toList)
  }

  private def printProgress(no: Int, total: Int): Unit = {
    print("\r")
    Console.flush()
    printf("%.2f%%", no * 100.0 / total)
  }

}

object TabuSearch {

  def findBest(indv: Array[Int])(implicit params: TabuParameters, context: Context): Array[Int] = {
    val tabuBase = TabuBase()
    var bestSolution = Individual(indv)
    var currentSolution = bestSolution.copy()
    for (i <- 0 until params.noOfIterations) {
      val bestNeighbour = getBestNeighbour(tabuBase, currentSolution, context)
      if (bestNeighbour > bestSolution) {
        bestSolution = bestNeighbour
      }
      currentSolution = bestNeighbour
    }
    bestSolution.locations
  }

  val rnd = new Random()

  def getBestNeighbour(tabuBase: TabuBase, solution: Individual, context: Context): Individual = {
    val dim = context.problem.dimension
    var best = solution.copy()
    var first = true

    val loglog = Math.log(Math.log(dim))

    for {
      i <- 1 until dim
      j <- 2 until dim
      if i != j
      if loglog > rnd.nextInt(dim)
    } {
      val curr = best.swap(i, j)
      if (tabuBase.canVisit(curr) && (first || curr > best)) {
        best = curr
        first = false
      }
    }
    tabuBase.move(best)
    best
  }

}