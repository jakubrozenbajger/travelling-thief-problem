package cf.jrozen.mh.ttp.slover.sa

import cats.Show

case class SimulatedAnnealingParameters(
                                         iterations: Int = 100,
                                         startingTemperature: Double = 100,
                                         coolingRate: Double = 0.1,
                                         stopTemperature: Double = 0.001
                                       )

object SimulatedAnnealingParameters {
  implicit val tabuParamsShow = new Show[SimulatedAnnealingParameters] {
    override def show(t: SimulatedAnnealingParameters): String = t.toString
  }
}