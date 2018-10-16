package cf.jrozen.mh.ttp.model;

import io.vavr.collection.Array;
import io.vavr.collection.Iterator;
import io.vavr.collection.List;
import io.vavr.collection.Stream;

public class Population {

    private final Context context;
    private final Array<Individual> population;

    private Population(Context context, Array<Individual> population) {
        this.context = context;
        this.population = population;
    }

    public static Population initRandom(Context context) {
        return new Population(context, initRandomPopulation(context));
    }

    private static Array<Individual> initRandomPopulation(Context context) {
        return Stream.range(0, context.parameters().populationSize())
                .map(i -> Individual.initRandom(context))
                .toArray();
    }

    public List<Population> evolve(int times) {
        return Stream.range(0, times)
                .foldLeft(List.of(this), (acc, list) -> acc.prepend(acc.head().evolve()))
                .reverse();
    }

    private Population evolve() {
        return select()
                .crossover()
                .mutate();
    }

    private Population mutate() {
        return new Population(context, population.map(Individual::mutate));
    }

    private Population crossover() {
        return new Population(context, crossover(this.population));
    }

    private Array<Individual> crossover(Array<Individual> population) {
        return Iterator.range(0, population.size() / 2)
                .flatMap(i -> population.get(i).crossover(population.get(population.size() - 1 - i)))
                .toArray();
    }

    private Population select() {
        return new Population(context,
                Array.fill(population.size(),
                        () -> pickTournament()
                                .sortBy(Individual::value)
                                .head()));
    }

    private Array<Individual> pickTournament() {
        return Array.fill(context.parameters().tournamentSize(), () -> population.get(context.nextInt(population.length())));
    }

    @Override
    public String toString() {
        return "Population{" +
                "context=" + context +
                ", population=" + population +
                '}';
    }
}
