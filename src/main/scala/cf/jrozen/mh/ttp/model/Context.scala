package cf.jrozen.mh.ttp.model

import cf.jrozen.mh.ttp.model.Context.Matrix

case class Context(
                    problem: Problem,
                    parameters: Parameters
                  ) {


  lazy val distance: Matrix = {
    {
      for {
        n1 <- problem.nodes
        n2 <- problem.nodes
      } yield Math.hypot(n1.x - n2.x, n1.y - n2.y)
    }
      .sliding(problem.nodes.length, problem.nodes.length)
      .map(_.toArray)
      .toArray
  }
}

object Context {

  type Matrix = Array[Array[Double]]

}
