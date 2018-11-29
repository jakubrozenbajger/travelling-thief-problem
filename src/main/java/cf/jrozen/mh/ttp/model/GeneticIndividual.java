package cf.jrozen.mh.ttp.model;

import cf.jrozen.mh.ttp.slover.genetic.GeneticParameters;
import com.google.common.primitives.Ints;
import io.vavr.Lazy;
import io.vavr.collection.List;

public class GeneticIndividual {

    private final Context context;
    private final GeneticParameters geneticParameters;
    private final int[] locations;
    private final Lazy<Double> value = Lazy.of(this::valueInit);

    private final MutationStrategy mutationStrategy;

    GeneticIndividual(Context context, GeneticParameters geneticParameters, MutationStrategy mutationStrategy) {
        this(context, geneticParameters, randomGenes(context), mutationStrategy);
    }

    public Double value() {
        return value.get();
    }

    private GeneticIndividual(Context context, GeneticParameters geneticParameters, final int[] locations, MutationStrategy mutationStrategy) {
        this.context = context;
        this.geneticParameters = geneticParameters;
        this.locations = locations;
        this.mutationStrategy = mutationStrategy;
    }

    private double valueInit() {
        return new Individual(locations, context).value();
    }

    GeneticIndividual mutate() {
        return new GeneticIndividual(context, geneticParameters, mutationStrategy.mutate(locations), mutationStrategy);
    }

    GeneticIndividual crossover(GeneticIndividual that) {
        if (geneticParameters.nextCrossover())
            return this.crossoverOne(that);
        else
            return this;
    }

    private GeneticIndividual crossoverOne(GeneticIndividual that) {
        final List<Integer> genes = List.ofAll(crossArrays(this.locations, that.locations));
        final List<Integer> fstNotUsed = List.range(0, context.problem().dimension()).removeAll(genes);
        return new GeneticIndividual(context, geneticParameters, toArray(genes.distinct().appendAll(fstNotUsed)), mutationStrategy);
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
