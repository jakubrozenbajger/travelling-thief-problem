package cf.jrozen.mh.ttp.model


case class Problem(
                    problemName: String,
                    knapsackDataType: String,
                    dimension: Int,
                    numberOfItem: Int,
                    capacityOfKnapsack: Int,
                    minSpeed: Double,
                    maxSpeed: Double,
                    rentingRatio: Double,
                    edgeWeightType: String,
                    nodes: java.util.List[Node],
                    section: java.util.List[Section]
                  )