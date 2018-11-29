package cf.jrozen.mh.ttp.model

trait MutationStrategy {
  def mutate(locations: Array[Int]): Array[Int]
}

