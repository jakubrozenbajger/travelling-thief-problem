package cf.jrozen.mh.ttp.model;

import com.google.common.primitives.Ints;
import io.vavr.Lazy;
import io.vavr.Tuple2;
import io.vavr.collection.List;

public class Individual {

    private final Context context;
    private final int[] genes;
    private final Lazy<Integer> value = Lazy.of(this::valueInit);

    public Integer value() {
        return value.get();
    }

    public Individual(Context context, final int[] genes) {
        this.context = context;
        this.genes = genes;
    }

    public static Individual initRandom(Context context) {
        return new Individual(context, randomGenes(context));
    }

    private static int[] randomGenes(Context context) {
        return toArray(List.range(0, context.problem().dimension()).shuffle());
    }

    public Individual mutate() {
        final int[] cloned = genes.clone();
        for (int currInd = 0; currInd < genes.length; currInd++) {
            if (context.nextMutate()) {
                final int randomInd = context.nextIntInDims();
                int tmp = cloned[currInd];
                cloned[currInd] = cloned[randomInd];
                cloned[randomInd] = tmp;

            }
        }
        return new Individual(context, cloned);
    }


    public static List<Individual> crossover(Tuple2<Individual, Individual> tuple) {
        return List.of(tuple._1.crossoverOne(tuple._2), tuple._2.crossoverOne(tuple._1));
    }

    private Individual crossoverOne(Individual that) {
        final List<Integer> genes = List.ofAll(crossArrays(this.genes, that.genes));
        final List<Integer> fstNotUsed = List.range(0, context.problem().dimension()).removeAll(genes);
        return new Individual(context, toArray(genes.distinct().appendAll(fstNotUsed)));
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


    public int valueInit() {
        return 0;
    }
}
