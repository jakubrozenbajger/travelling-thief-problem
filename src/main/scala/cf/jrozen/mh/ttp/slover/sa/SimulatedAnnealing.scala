package cf.jrozen.mh.ttp.slover.sa


import cf.jrozen.mh.ttp.model.{Context, Individual}

import scala.collection.mutable.ListBuffer

class SimulatedAnnealing(params: SimulatedAnnealingParameters)(implicit context: Context) {

  def run: (List[Individual], List[Individual]) = {
    var currentSolution: Individual = Individual.random
    var best: Individual = currentSolution.copy()
    val result = new ListBuffer[Individual]()
    val others = new ListBuffer[Individual]()
    var currentTemperature: Double = params.startingTemperature
    (0 until params.iterations) foreach { _ =>
      if (currentTemperature > params.stopTemperature) {
        val ind1: Int = context.nextIntInDims
        val ind2: Int = context.nextIntInDims
        val oldSolution = currentSolution
        val swapped = oldSolution.swap(ind1, ind2)
        others += swapped
        if (swapped > best) {
          best = swapped
          currentSolution = swapped
        }
        else if (Math.exp((swapped.value - best.value) / currentTemperature) < Math.random)
          currentSolution = oldSolution

        result += currentSolution
        currentTemperature -= currentTemperature * params.coolingRate
      }
    }
    (result.toList, others.toList)
  }

}
