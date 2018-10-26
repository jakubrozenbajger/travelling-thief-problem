package cf.jrozen.mh.ttp.slover.tabu

import cf.jrozen.mh.ttp.model.{Context, Individual}

import scala.collection.mutable.ListBuffer

class TabuSearch()(implicit context: Context, params: TabuParameters) {


  def getBestNeighbour(tabuBase: TabuBase, solution: Individual): Individual = {
    var best = solution

    for {
      i <- 1 until context.problem.dimension
      j <- 2 until context.problem.dimension
      if i != j
    } {
      val curr = best.swap(i, j)
      if (curr != solution && tabuBase.canVisit(curr) && curr > best) {
        best = curr
      }
    }
    tabuBase.move(best)
    best
  }

  def run: (List[Individual], List[Individual]) = {
    val tabuBase = TabuBase()

    val individuals = new ListBuffer[Individual]()
    val secondIndividuals = new ListBuffer[Individual]()
    var bestSolution = Individual.random

    var lastChange = 0
    for (i <- 0 until params.noOfIterations if lastChange < params.noOfIterations / 3.0) {
      individuals += bestSolution
      val currentSollution = getBestNeighbour(tabuBase, bestSolution)
      secondIndividuals += currentSollution
      if (currentSollution > bestSolution) {
        bestSolution = currentSollution
        lastChange = 0
      }
      printProgress(i, params.noOfIterations)
      lastChange += 1
    }
    (individuals.toList, secondIndividuals.toList)
  }

  private def printProgress(no: Int, total: Int): Unit = {
    print("\r")
    Console.flush()
    printf("%.2f%%", no * 100.0 / total)
  }

}
