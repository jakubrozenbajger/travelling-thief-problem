package cf.jrozen.mh.ttp.slover.tabu

import cats.Show
import cf.jrozen.mh.ttp.model.Context
import cf.jrozen.mh.ttp.slover.CsvFormat
import cf.jrozen.mh.ttp.utils.{Banner, ChartGenerator, Loader}
import com.google.common.math.Stats

object ScalaMainTabu extends App {

  System.out.println(Banner.title)
  val problemName = "medium_1"
  //  val problemName = "hard_1"
  val params = TabuParameters(noOfIterations = 800, tabuSize = 120)
  //  val stats = solve(problemName, params)
  //  System.out.println("Last stats: " + stats._1.last)
  //  chart(params).show(stats)

  val problemNames = List("easy_1", "easy_4", "medium_0", "medium_1", "hard_0")
  private val result: Map[String, Stats] = problemNames.map(pn => runFor(pn, params)).toMap
  println(result)
  CsvFormat.format(result).foreach(println)


  private def solve(problemName: String, params: TabuParameters) = {
    val context = new Context(Loader.load(problemName))
    val tabuSearch: TabuSearch = new TabuSearch()(context, params)
    val res = tabuSearch.run
    (res._1.map(_.value).toArray, res._2.map(_.value).toArray)
  }

  private def chart[T: Show](params: T) = new ChartGenerator("TTP TABU STATS", "generation", "profit", implicitly[Show[T]].show(params))

  def runFor(problemName: String, parameters: TabuParameters, times: Int = 10): (String, Stats) = {
    (problemName, Stats.of(
      (0 until times).map { i =>
        val bestStats = solve(problemName, params).swap._1.last
        System.out.println(s"$problemName stats $i: $bestStats")
        bestStats
      }: _*
    ))
  }

}
