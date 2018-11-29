package cf.jrozen.mh.ttp.model

import cf.jrozen.mh.ttp.slover.sa.{SimulatedAnnealing, SimulatedAnnealingParameters}

class SAMutationStrategy(
                          gaMutationStrategy: GAMutationStrategy,
                          triggerIndicator: => Boolean
                        )(
                          implicit context: Context,
                          simulatedAnnealingParameters: SimulatedAnnealingParameters,
                        ) extends MutationStrategy {


  override def mutate(locations: Array[Int]): Array[Int] = {
    if (triggerIndicator)
      SimulatedAnnealing.findBest(locations, simulatedAnnealingParameters)
    else
      gaMutationStrategy.mutate(locations)
  }


}
