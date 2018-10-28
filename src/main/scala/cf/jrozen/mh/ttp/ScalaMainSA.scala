package cf.jrozen.mh.ttp

import cats.Show
import cf.jrozen.mh.ttp.model.{Context, Individual}
import cf.jrozen.mh.ttp.slover.sa.{SimulatedAnnealing, SimulatedAnnealingParameters}
import cf.jrozen.mh.ttp.utils.{Banner, ChartGenerator, Loader}

object ScalaMainSA extends App {

  System.out.println(Banner.title)
  val problemName = "medium_1"
  val params = SimulatedAnnealingParameters()
  val stats: Array[Double] = solve(problemName, params).toArray
  System.out.println("Last stats: " + stats.last)
  chart(params).show(stats)

  private def solve(problemName: String, params: SimulatedAnnealingParameters) = {
    val context = new Context(Loader.load(problemName))
    val simmulatedAnn: SimulatedAnnealing 2= new SimulatedAnnealing(params)(context)
    simmulatedAnn.run.map(_.value)
  }

  private def chart[T: Show](params: T) = new ChartGenerator("TTP SA STATS", "generation", "profit", implicitly[Show[T]].show(params))


}
