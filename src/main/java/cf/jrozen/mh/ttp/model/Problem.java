package cf.jrozen.mh.ttp.model;

import java.util.List;

public class Problem {

    final String problemName;
    final String knapsackDataType;
    final int dimension;
    final int numberOfItems;
    final int capacityOfKnapsack;
    final double minSpeed;
    final double maxSpeed;
    final double rentingRatio;
    final String edgeWeightType;

    public Problem(String problemName,
                   String knapsackDataType,
                   int dimension,
                   int numberOfItems,
                   int capacityOfKnapsack,
                   double minSpeed,
                   double maxSpeed,
                   double rentingRatio,
                   String edgeWeightType,
                   List<Node> nodes,
                   List<Section> section) {

        this.problemName = problemName;
        this.knapsackDataType = knapsackDataType;
        this.dimension = dimension;
        this.numberOfItems = numberOfItems;
        this.capacityOfKnapsack = capacityOfKnapsack;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.rentingRatio = rentingRatio;
        this.edgeWeightType = edgeWeightType;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "problemName='" + problemName + '\'' +
                ", knapsackDataType='" + knapsackDataType + '\'' +
                ", dimension=" + dimension +
                ", numberOfItems=" + numberOfItems +
                ", capacityOfKnapsack=" + capacityOfKnapsack +
                ", minSpeed=" + minSpeed +
                ", maxSpeed=" + maxSpeed +
                ", rentingRatio=" + rentingRatio +
                ", edgeWeightType='" + edgeWeightType + '\'' +
                '}';
    }
}
