package cf.jrozen.mh.ttp.model

import cf.jrozen.mh.ttp.slover.tabu.{TabuParameters, TabuSearch}

trait FinishingStrategy {

  def finish(locations: Array[Int]): Array[Int]

  def empty = EmptyFinish()
}


case class EmptyFinish() extends FinishingStrategy {
  override def finish(locations: Array[Int]): Array[Int] = locations
}

case class TabuFinish()(implicit tabuParameters: TabuParameters, context: Context) extends FinishingStrategy {
  override def finish(locations: Array[Int]): Array[Int] = {
    TabuSearch.findBest(locations)
  }
}
