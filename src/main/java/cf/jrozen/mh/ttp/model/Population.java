package cf.jrozen.mh.ttp.model;

import io.vavr.collection.List;
import io.vavr.collection.Stream;

public class Population {

    private final Context context;
    private final List<Individual> population;

    private Population(Context context, List<Individual> population) {
        this.context = context;
        this.population = population;
    }

    public static Population initRandom(Context context) {
        return new Population(context, initRandomPopulation(context));
    }

    private static List<Individual> initRandomPopulation(Context context) {
        return Stream.range(0, context.parameters().populationSize())
                .map(i -> Individual.initRandom(context))
                .toList();
    }

    public List<Population> evolve(int times) {
        return Stream.range(0, times)
                .foldLeft(List.of(this), (acc, list) -> acc.prepend(acc.head().evolve()))
                .reverse();
    }

    private Population evolve() {
        return select().crossover(select())
                .mutate();
    }

    private Population mutate() {
        return new Population(context, population.map(Individual::mutate));
    }

    private Population crossover(Population that) {
        return new Population(context, population.zip(that.population).flatMap(Individual::crossover));
    }

    private Population select() {
        return new Population(context, selectHalf());
    }

    private List<Individual> selectHalf() {
        final int tournamentSize = context.parameters().tournamentSize();
        final Stream<Individual> populationStream = population.toArray().toStream();
        return Stream
                .continually(() ->
                        populationStream
                                .shuffle()
                                .take(tournamentSize)
                                .sortBy(Individual::value)
                                .head())
                .take(population.size() / 2)
                .toList();
    }

    @Override
    public String toString() {
        return "Population{" +
                "context=" + context +
                ", population=" + population +
                '}';
    }
}
