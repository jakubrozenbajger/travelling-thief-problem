package cf.jrozen.mh.ttp;

import cf.jrozen.mh.ttp.model.Context;
import cf.jrozen.mh.ttp.model.Item;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.function.Function.identity;

public class GreedyKnapsackSolver {


    public static Item[] chooseItems(Context context, int[] locations) {
        final int capacity = context.problem().capacityOfKnapsack();
        final List<Item> allItems = Arrays.asList(context.problem().items());
        double[] distanceToEndFromNodeRatio = distanceToEndFromNodeRatio(context, locations);

        final Map<Integer, Double> locationIdToDistanceRatio = IntStream.range(0, locations.length).boxed().collect(Collectors.toMap(i -> i, i -> distanceToEndFromNodeRatio[locations[i]]));
        final Map<Item, Double> itemToDistanceCostRatio = allItems.stream().collect(Collectors.toMap(identity(), i -> locationIdToDistanceRatio.getOrDefault(i.assignedNodeNumber(), Double.MAX_VALUE)));

        final Function<Item, Double> itemToValueIncludingDistance = (Item item) -> 1.0 * item.profit() / item.weight() - (capacity * itemToDistanceCostRatio.getOrDefault(item, Double.MAX_VALUE) * item.weight());


//        final Knapsack knapsack = new Knapsack(capacity);
//        return allItems.stream()
//                .sorted(Comparator.comparing(itemToValueIncludingDistance))
//                .sequential()
//                .takeWhile(knapsack::addWhilePossible)
//                .toArray(Item[]::new);
//
        return allItems.stream()
                .sorted(Comparator.comparing(itemToValueIncludingDistance))
                .sequential()
                .reduce(new Knapsack(capacity), Knapsack::addIfPossible, assertSequential()).getItemsAsArray();
    }


    public static double[] distanceToEndFromNodeRatio(Context context, int[] locations) {
        final double[] distanceToEndFromCity = new double[locations.length];
        double totalDistance = 0;

        for (int i = 0; i < locations.length; i++) {
            int nodeId = locations[i];
            int nextNodeId = locations[(i + 1) % locations.length];

            final double distance = context.distance()[nodeId][nextNodeId];
            updateDistances(distanceToEndFromCity, i, distance);

            totalDistance += distance;
        }

        final double[] locationDistanceToEndRatio = distanceToEndFromCity;
        for (int i = 0; i < distanceToEndFromCity.length; i++) {
            locationDistanceToEndRatio[i] /= totalDistance;
        }

        return locationDistanceToEndRatio;
    }

    private static void updateDistances(double[] distanceToEndFromCity, int indexInclusive, double distance) {
        for (int i = 0; i <= indexInclusive; i++) {
            distanceToEndFromCity[i] += distance;
        }
    }


    static class Knapsack {

        private int capacityLeft;
        private final LinkedList<Item> items = new LinkedList<>();

        Knapsack(int capacity) {
            this.capacityLeft = capacity;
        }

        private Knapsack addIfPossible(Item item) {
            if (capacityLeft - item.weight() > 0) {
                capacityLeft -= item.weight();
                items.addLast(item);
            }
            return this;
        }

        private boolean addWhilePossible(Item item) {
            boolean result;
            if (result = (capacityLeft - item.weight() > 0)) {
                capacityLeft -= item.weight();
                items.addLast(item);
            }
            return result;
        }

        Item[] getItemsAsArray() {
            return items.toArray(new Item[items.size()]);
        }
    }

    private static BinaryOperator<Knapsack> assertSequential() {
        return (k1, k2) -> {
            throw new AssertionError("Not sequential execution");
        };
    }

}
