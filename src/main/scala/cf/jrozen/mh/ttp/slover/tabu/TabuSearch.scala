package cf.jrozen.mh.ttp.slover.tabu

import cf.jrozen.mh.ttp.model.{Context, Individual}

import scala.collection.mutable.ListBuffer

class TabuSearch()(implicit context: Context, params: TabuParameters) {


  //  def getBestNeighbour(tabuBase: TabuBase, solution: Individual): Individual = {
  //    var best = solution
  //
  //    for {
  //      i <- 1 until context.problem.dimension
  //      j <- 2 until context.problem.dimension
  //      if i != j
  //    } {
  //      val curr = best.swap(i, j)
  //      if (curr != solution && tabuBase.canVisit(curr) && curr > best) {
  //        best = curr
  //      }
  //    }
  //    tabuBase.move(best)
  //    best
  //
  //
  //  }


  private def getBestNeighbour(tabuBase: TabuBase, solution: Individual) = {
    var bestSolution = solution.copy()
    var node1: Int = 0
    var node2: Int = 0
    var firstNeighbour: Boolean = true
    var i: Int = 1
    while ( {
      i < context.problem.dimension
    }) {
      var j: Int = 2
      while ( {
        j < context.problem.dimension
      }) {
        if (i != j) {

          val newBestSolution = bestSolution.swap(i, j)
          if ((newBestSolution > bestSolution || firstNeighbour) && tabuBase.canVisit(i, j)) {
            firstNeighbour = false
            node1 = i
            node2 = j
            bestSolution = newBestSolution.copy()
          }
        }
        j += 1
      }
      i += 1
    }
    if (node1 != 0) {
      tabuBase.move(node1, node2)
    }
    bestSolution
  }

  def run: (List[Individual], List[Individual]) = {
    val tabuBase = TabuBase()

    val individuals = new ListBuffer[Individual]()
    val secondIndividuals = new ListBuffer[Individual]()
    var bestSolution = Individual.random
    var currentSollution = bestSolution
    var lastChange = 0
    for (i <- 0 until params.noOfIterations if lastChange < params.noOfIterations / 3.0) {
      currentSollution = getBestNeighbour(tabuBase, currentSollution)
      individuals += bestSolution
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
