package cf.jrozen.mh.ttp.slover.tabu

import cf.jrozen.mh.ttp.model.{Context, Individual}

import scala.collection.mutable.ListBuffer

class TabuSearch()(implicit context: Context, params: TabuParameters) {


  def getBestNeighbour(tabuBase: TabuBase, solution: Individual): Individual = {
    var bestSolution = solution.copy()

    var (n1, n2) = (0, 0)
    var firstNeighbour = true

    for {
      i <- 1 until context.problem.dimension
      j <- 2 until context.problem.dimension
      if i != j
    } {
      val newBestSolution = bestSolution.swap(i, j)
      if ((newBestSolution > bestSolution || firstNeighbour) && tabuBase.isEmpty(i, j)) {
        firstNeighbour = false
        n1 = i
        n2 = j
        bestSolution = newBestSolution
      }
    }
    if (n1 != 0) {
      tabuBase.move(n1, n2)
    }
    bestSolution
  }

  def run: List[Individual] = {
    val tabuBase = TabuBase()

    val individuals = new ListBuffer[Individual]()
    var bestSolution = Individual.random
    individuals += bestSolution

    for (_ <- 0 until params.noOfIterations) {
      val currentSollution = getBestNeighbour(tabuBase, bestSolution)
      if (currentSollution > bestSolution) {
        bestSolution = currentSollution.copy()
      }
      individuals += bestSolution
    }
    individuals.toList
  }

}
