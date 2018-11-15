package cf.jrozen.mh.ttp.slover.genetic

import cats.Show
import cf.jrozen.mh.ttp.ScalaMainSA.{params, problemNames, runFor, solve}
import cf.jrozen.mh.ttp.model.{Context, Population}
import cf.jrozen.mh.ttp.slover.CsvFormat
import cf.jrozen.mh.ttp.slover.sa.SimulatedAnnealingParameters
import cf.jrozen.mh.ttp.utils.{Banner, ChartGenerator, Loader}
import com.google.common.math.Stats

object GeneticRun extends App {

  System.out.println(Banner.title)
  val problemNames = List("easy_1", "easy_4", "medium_0", "medium_1", "hard_0")
  val params = GeneticParameters(130, 400, 5, 0.022, 0.119)

//  val stats = solve(problemName, params)
//  System.out.println("Last stats: " + stats.last)
//  chart(params).show(stats)
  private val result: Map[String, Stats] = problemNames.map(pn => runFor(pn, params)).toMap
  println(result)
  CsvFormat.format(result).foreach(println)



  private def chart[T: Show](params: T) = new ChartGenerator("TTP GENETIC STATS", "generation", "profit", implicitly[Show[T]].show(params))


  private def solve(problemName: String, params: GeneticParameters) = {
    val context = new Context(Loader.load(problemName))
    import collection.convert.ImplicitConversionsToScala._
    val evolutionHistory = Population.initRandom(context, params).runEvolution.asJava().toList
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
