package cf.jrozen.mh.ttp.slover.tabu

import cf.jrozen.mh.ttp.model.{Context, Individual}
import cf.jrozen.mh.ttp.mutable

import scala.collection.mutable

case class TabuBase()(implicit context: Context, params: TabuParameters) {

  val base = new mutable.LinkedHashSet[Individual]()

  @mutable
  def move(indv: Individual): Unit = {
    base.add(indv)
    if (base.size >= params.tabuSize) {
      base.remove(base.head)
    }
  }

  def canVisit(indv: Individual): Boolean = {
    !base.contains(indv)
  }

}
