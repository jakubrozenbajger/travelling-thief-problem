package cf.jrozen.mh.ttp.model;

import com.google.common.math.Stats;
import io.vavr.Lazy;
import io.vavr.collection.Array;
import io.vavr.collection.List;
import io.vavr.collection.Stream;

public class Population {

    private final Context context;
    private final Array<Individual> population;
    private final Lazy<Stats> statsLazy = Lazy.of(this::calcStats);

    private Stats calcStats() {
        return Stats.of(population.map(Individual::value));
    }

    public Stats stats() {
        return statsLazy.get();
    }

    private Population(Context context, Array<Individual> population) {
        this.context = context;
        this.population = population;
    }

    public static Population initRandom(Context context) {
        return new Population(context, initRandomPopulation(context));
    }

    private static Array<Individual> initRandomPopulation(Context context) {
        return Array.fill(context.parameters().populationSize(), () -> new Individual(context));
    }

    public List<Population> evolve(int times) {
        return Stream.range(0, times)
                .foldLeft(List.of(this), (acc, i) -> acc.prepend(acc.head().evolve()))
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
        return new Population(context,
                this.population.zipWith(this.population.shuffle(),
                        Individual::crossover
                ).toArray()
        );
    }

    private Population select() {
        return new Population(context,
                Array.fill(population.size(),
                        () -> pickRandomTournament()
                                .sortBy(Individual::value)
                                .head()));
    }

    private Array<Individual> pickRandomTournament() {
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
