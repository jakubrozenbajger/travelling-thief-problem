package cf.jrozen.mh.ttp.slover.sa

import cats.Show
import cf.jrozen.mh.ttp.model.Context
import cf.jrozen.mh.ttp.slover.CsvFormat
import cf.jrozen.mh.ttp.utils.{Banner, ChartGenerator, Loader}
import com.google.common.math.Stats

object ScalaMainSA extends App {

  System.out.println(Banner.title)
  val problemNames = List("easy_1", "easy_4", "medium_0", "medium_1", "hard_0")
  //  val problemName = "medium_1"
  val params = SimulatedAnnealingParameters(iterations = 75000, startingTemperature = 200.0, coolingRate = 0.0001, stopTemperature = 0.0000001)
  //  val stats: (Array[Double], Array[Double]) = solve(problemName, params).swap
  //  System.out.println("Last stats: " + stats._1.last)
  //  chart(params).show(stats)
  private val result: Map[String, Stats] = problemNames.map(pn => runFor(pn, params)).toMap
  println(result)
  CsvFormat.format(result).foreach(println)


  private def solve(problemName: String, params: SimulatedAnnealingParameters) = {
    val context = new Context(Loader.load(problemName))
    val simulatedAnn: SimulatedAnnealing = new SimulatedAnnealing(params)(context)
    val res = simulatedAnn.run
    (res._1.map(_.value).toArray, res._2.map(_.value).toArray)
  }

  private def chart[T: Show](params: T) = new ChartGenerator("TTP SA STATS", "generation", "profit", implicitly[Show[T]].show(params))


  def runFor(problemName: String, parameters: SimulatedAnnealingParameters, times: Int = 10): (String, Stats) = {
    (problemName, Stats.of(
      (0 until times).map { i =>
        val bestStats = solve(problemName, params).swap._1.last
        System.out.println(s"$problemName stats $i: $bestStats")
        bestStats
      }: _*
    ))
  }

}
