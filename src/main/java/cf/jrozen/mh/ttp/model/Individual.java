package cf.jrozen.mh.ttp.model;

import cf.jrozen.mh.ttp.GreedyKnapsackSolver;
import com.google.common.primitives.Ints;
import io.vavr.Lazy;
import io.vavr.collection.List;

import java.util.Set;

public class Individual {

    private final Context context;
    private final int[] locations;
    private final Lazy<Double> value = Lazy.of(this::valueInit);

    Individual(Context context) {
        this(context, randomGenes(context));
    }

    Double value() {
        return value.get();
    }

    private Individual(Context context, final int[] locations) {
        this.context = context;
        this.locations = locations;
    }

    private double valueInit() {
        final Set<Item> items = GreedyKnapsackSolver.chooseItems(context, this.locations);
        return context.calculate(this.locations, items);
    }

    Individual mutate() {
        final int[] cloned = locations.clone();
        for (int currInd = 0; currInd < locations.length; currInd++) {
            if (context.nextMutate()) {
                final int randomInd = context.nextIntInDims();
                int tmp = cloned[currInd];
                cloned[currInd] = cloned[randomInd];
                cloned[randomInd] = tmp;
            }
        }
        return new Individual(context, cloned);
    }

    Individual crossover(Individual that) {
        if (context.nextCrossover())
            return this.crossoverOne(that);
        else
            return this;
    }

    private Individual crossoverOne(Individual that) {
        final List<Integer> genes = List.ofAll(crossArrays(this.locations, that.locations));
        final List<Integer> fstNotUsed = List.range(0, context.problem().dimension()).removeAll(genes);
        return new Individual(context, toArray(genes.distinct().appendAll(fstNotUsed)));
    }

    private static int[] randomGenes(Context context) {
        return toArray(List.range(0, context.problem().dimension()).shuffle());
    }

    private int[] crossArrays(int[] thisGenes, int[] thatGenes) {
        final int splitAt = context.nextIntInDims();
        final int[] crossed = new int[context.problem().dimension()];
        System.arraycopy(thisGenes, 0, crossed, 0, splitAt);
        System.arraycopy(thatGenes, splitAt, crossed, splitAt, crossed.length - splitAt);
        return crossed;
    }

    private static int[] toArray(List<Integer> list) {
        return Ints.toArray(list.asJava());
    }
}
