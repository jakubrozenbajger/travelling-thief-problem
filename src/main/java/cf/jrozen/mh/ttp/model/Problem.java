package cf.jrozen.mh.ttp.model;

import java.util.List;

public class Problem {

    public final String problemName;
    public final String knapsackDataType;
    public final int dimension;
    public final int numberOfItems;
    public final int capacityOfKnapsack;
    public final double minSpeed;
    public final double maxSpeed;
    public final double rentingRatio;
    public final String edgeWeightType;
    public final List<Node> nodes;
    public final List<Section> section;

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
        this.nodes = nodes;
        this.section = section;
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
                ", nodes=" + nodes +
                ", section=" + section +
                '}';
    }
}
