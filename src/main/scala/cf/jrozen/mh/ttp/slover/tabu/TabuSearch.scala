package cf.jrozen.mh.ttp.slover.tabu

import cf.jrozen.mh.ttp.model.{Context, Individual}

import scala.collection.mutable.ListBuffer

class TabuSearch()(implicit context: Context, params: TabuParameters) {


  def getBestNeighbour(tabuBase: TabuBase, solution: Individual): Individual = {
    var best = solution

    var n1 = 0
    var n2 = 0
    var firstNeighbour = true

    for {
      i <- 1 until context.problem.dimension
      j <- 2 until context.problem.dimension
      if i != j
    } {
      val curr = best.swap(i, j)
      if ((curr > best || firstNeighbour) && tabuBase.canVisit(i, j)) {
        firstNeighbour = false
        n1 = i
        n2 = j
        best = curr
      }
    }
    if (n1 != 0) {
      tabuBase.move(n1, n2)
    }
    best
  }

  def run: List[Individual] = {
    val tabuBase = TabuBase()

    val individuals = new ListBuffer[Individual]()
    var bestSolution = Individual.random
    individuals += bestSolution

    for (i <- 0 until params.noOfIterations) {
      individuals += bestSolution
      val currentSollution = getBestNeighbour(tabuBase, bestSolution)
      if (currentSollution > bestSolution) {
        bestSolution = currentSollution
      }
      printProgress(i, params.noOfIterations)
    }
    individuals.toList
  }

  private def printProgress(no: Int, total: Int): Unit = {
    print("\r")
    Console.flush()
    printf("%.2f%%", no * 100.0 / total)
  }

}
