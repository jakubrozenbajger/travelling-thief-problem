package cf.jrozen.mh.ttp.slover.genetic

import cats.Show
import cf.jrozen.mh.ttp.slover.sa.ScalaMainSA.{params, problemNames, runFor, solve}
import cf.jrozen.mh.ttp.model._
import cf.jrozen.mh.ttp.slover.CsvFormat
import cf.jrozen.mh.ttp.slover.sa.SimulatedAnnealingParameters
import cf.jrozen.mh.ttp.slover.tabu.TabuParameters
import cf.jrozen.mh.ttp.utils.{Banner, ChartGenerator, Loader}
import com.google.common.math.Stats

import scala.util.Random

object GeneticRun extends App {

  System.out.println(Banner.title)
  val params = GeneticParameters(populationSize = 130, noOfGenerations = 400, tournamentSize = 5, mutationProbability = 0.022, crossoverProbability = 0.119)

  val problemName = "medium_1"
  val stats = solve(problemName, params)
  System.out.println("Last stats: " + stats.last)
  chart(params).show(stats)


//  val problemNames = List("easy_1", "easy_4", "medium_0", "medium_1", "hard_0")
//  private val result: Map[String, Stats] = problemNames.map(pn => runFor(pn, params)).toMap
//  println(result)
//  CsvFormat.format(result).foreach(println)



  private def chart[T: Show](params: T) = new ChartGenerator("TTP GENETIC STATS", "generation", "profit", implicitly[Show[T]].show(params))


  private def solve(problemName: String, params: GeneticParameters) = {
    implicit val context = new Context(Loader.load(problemName))
    implicit val saParameters = SimulatedAnnealingParameters(iterations = 3000, startingTemperature = 200.0, coolingRate = 0.001, stopTemperature = 0.0000001)
    implicit val tabuParameters = TabuParameters(noOfIterations = 800, tabuSize = 120)
    val rnd = new Random()
    import collection.convert.ImplicitConversionsToScala._
//    val evolutionHistory = Population.initRandom(context, params, new GAMutationStrategy(context, params), EmptyFinish()).runEvolution.asJava().toList
//    val evolutionHistory = Population.initRandom(context, params, new GAMutationStrategy(context, params), TabuFinish()).runEvolution.asJava().toList
//    val evolutionHistory = Population.initRandom(context, params, new SAMutationStrategy(new GAMutationStrategy(context, params), rnd.nextFloat() < 0.0010 ), EmptyFinish()).runEvolution.asJava().toList
    val evolutionHistory = Population.initRandom(context, params, new SAMutationStrategy(new GAMutationStrategy(context, params), rnd.nextFloat() < 0.0030 ), TabuFinish()).runEvolution.asJava().toList
    evolutionHistory.map(_.stats)
  }

  def runFor(problemName: String, parameters: GeneticParameters, times: Int = 10): (String, Stats) = {
//    val stat: Double  = solve(problemName, params).map(s => s.max()).max
    (problemName, Stats.of(
      (0 until times).map { i =>
        val bestStats = solve(problemName, params).map(s => s.max()).max
        System.out.println(s"$problemName stats $i: $bestStats")
        bestStats
      }: _*
    ))
  }


}
