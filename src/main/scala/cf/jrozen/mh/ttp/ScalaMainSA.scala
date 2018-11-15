package cf.jrozen.mh.ttp

import cats.Show
import cf.jrozen.mh.ttp.model.Context
import cf.jrozen.mh.ttp.slover.sa.{SimulatedAnnealing, SimulatedAnnealingParameters}
import cf.jrozen.mh.ttp.utils.{Banner, ChartGenerator, Loader}

object ScalaMainSA extends App {

  System.out.println(Banner.title)
  val problemName = "hard_1"
  val params = SimulatedAnnealingParameters(iterations = 10000, startingTemperature = 200.0, coolingRate = 0.0001, stopTemperature = 0.0000001)
  val stats: (Array[Double], Array[Double]) = solve(problemName, params).swap
  System.out.println("Last stats: " + stats._1.last)
  chart(params).show(stats)

  private def solve(problemName: String, params: SimulatedAnnealingParameters) = {
    val context = new Context(Loader.load(problemName))
    val simulatedAnn: SimulatedAnnealing = new SimulatedAnnealing(params)(context)
    val res = simulatedAnn.run
    (res._1.map(_.value).toArray, res._2.map(_.value).toArray)
  }

  private def chart[T: Show](params: T) = new ChartGenerator("TTP SA STATS", "generation", "profit", implicitly[Show[T]].show(params))


}
