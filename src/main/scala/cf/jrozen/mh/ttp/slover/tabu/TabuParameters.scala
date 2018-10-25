package cf.jrozen.mh.ttp.slover.tabu

import cats.Show

case class TabuParameters(
                           tabuDuration: Int,
                           noOfIterations: Integer,
                         )

object TabuParameters {

  implicit val tabuParamsShow = new Show[TabuParameters] {
    override def show(t: TabuParameters): String = t.toString
  }

}
