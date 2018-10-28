package cf.jrozen.mh.ttp.slover.sa


import cf.jrozen.mh.ttp.model.{Context, Individual}

import scala.collection.mutable.ListBuffer

class SimulatedAnnealing(params: SimulatedAnnealingParameters)(implicit context: Context) {


  def run: List[Individual] = {
    var currentSolution: Individual = Individual.random
    var best: Individual = currentSolution.copy()
    val result = new ListBuffer[Individual]()
    var currentTemperature: Double = params.startingTemperature
    for (_ <- 0 until params.iterations) {
      if (currentTemperature > params.stopTemperature) {
        val r1: Int = context.nextIntInDims
        val r2: Int = context.nextIntInDims
        val oldSolution = currentSolution
        val swapped = oldSolution.swap(r1, r2)
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
    result.toList
  }

}
