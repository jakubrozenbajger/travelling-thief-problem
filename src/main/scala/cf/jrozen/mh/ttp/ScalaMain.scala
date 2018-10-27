package cf.jrozen.mh.ttp

import cats.Show
import cf.jrozen.mh.ttp.model.Context
import cf.jrozen.mh.ttp.slover.tabu.{TabuParameters, TabuSearch}
import cf.jrozen.mh.ttp.utils.{Banner, ChartGenerator, Loader}

object ScalaMain extends App {

  System.out.println(Banner.title)
  val problemName = "medium_1"
  val params = TabuParameters(80, 570, 60)
  val stats = solve(problemName, params)
  System.out.println("Last stats: " + stats._1.last)
  chart(params).show(stats)

  private def solve(problemName: String, params: TabuParameters) = {
    val context = new Context(Loader.load(problemName))
    val tabuSearch: TabuSearch = new TabuSearch()(context, params)
    val res = tabuSearch.run
    (res._1.map(_.value).toArray, res._2.map(_.value).toArray)
  }

  private def chart[T: Show](params: T) = new ChartGenerator("TTP TABU STATS", "generation", "profit", implicitly[Show[T]].show(params))


}
