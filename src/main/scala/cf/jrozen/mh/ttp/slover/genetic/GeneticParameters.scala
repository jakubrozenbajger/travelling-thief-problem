package cf.jrozen.mh.ttp.slover.genetic

import cats.Show

import scala.util.Random

case class GeneticParameters(
                              populationSize: Int,
                              noOfGenerations: Int,
                              tournamentSize: Int,
                              mutationProbability: Double,
                              crossoverProbability: Double
                            ) {

  def `mutate?`: Boolean = nextMutate

  def nextMutate: Boolean = Random.nextDouble() < this.mutationProbability

  def `crossover?`: Boolean = nextCrossover

  def nextCrossover: Boolean = Random.nextDouble() < this.crossoverProbability

}

object GeneticParameters {
  implicit val tabuParamsShow = new Show[GeneticParameters] {
    override def show(t: GeneticParameters): String = t.toString
  }
}