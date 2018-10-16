package cf.jrozen.mh.ttp.model

import scala.util.Random

case class Parameters(
                       populationSize: Int,
                       noOfGenerations: Int,
                       tournamentSize: Int,
                       mutationProbability: Double,
                       crossoverProbability: Double
                     ) {

  def `mutate?`: Boolean = nextMutate

  def nextMutate: Boolean = Random.nextDouble() < mutationProbability

  def `crossover?`: Boolean = nextCrossover

  def nextCrossover: Boolean = Random.nextDouble() < crossoverProbability

}