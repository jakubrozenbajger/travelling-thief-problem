package cf.jrozen.mh.ttp.model

case class Parameters(
                       populationSize: Int,
                       noOfGenerations: Int,
                       tournamentSize: Int,
                       mutationProbability: Double,
                       crossoverProbability: Double
                     )