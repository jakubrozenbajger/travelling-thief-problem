package cf.jrozen.mh.ttp.model;

import cf.jrozen.mh.ttp.slover.genetic.GeneticParameters;
import com.google.common.math.Stats;
import io.vavr.Lazy;
import io.vavr.collection.Array;
import io.vavr.collection.List;
import io.vavr.collection.Stream;

import java.util.Random;

public class Population {

    private static final Random rnd = new Random();

    private final Context context;
    private final GeneticParameters geneticParameters;
    private final Array<GeneticIndividual> population;
    private final Lazy<Stats> statsLazy = Lazy.of(this::calcStats);

    private final MutationStrategy mutationStrategy;

    private Stats calcStats() {
        return Stats.of(population.map(GeneticIndividual::value));
    }

    public Stats stats() {
        return statsLazy.get();
    }

    private Population(Context context, GeneticParameters geneticParameters, Array<GeneticIndividual> population, MutationStrategy mutationStrategy) {
        this.context = context;
        this.geneticParameters = geneticParameters;
        this.population = population;
        this.mutationStrategy = mutationStrategy;
    }

    public static Population initRandom(Context context, GeneticParameters geneticParameters, MutationStrategy mutationStrategy) {
        return new Population(context, geneticParameters, initRandomPopulation(context, geneticParameters, mutationStrategy), mutationStrategy);
    }

    private static Array<GeneticIndividual> initRandomPopulation(Context context, GeneticParameters geneticParameters, MutationStrategy mutationStrategy) {
        return Array.fill(geneticParameters.populationSize(), () -> new GeneticIndividual(context, geneticParameters, mutationStrategy));
    }

    public List<Population> runEvolution() {
        return evolve(geneticParameters.noOfGenerations());
    }

    private List<Population> evolve(int times) {
        return Stream.range(0, times)
                .peek(generation -> printProgress(generation, times))
                .foldLeft(List.of(this), (acc, i) -> acc.prepend(acc.head().evolve()))
                .reverse();
    }

    private void printProgress(int genNo, int times) {
        System.out.print("\r");
        System.out.flush();
        System.out.printf("%.2f%%", genNo * 100.0 / times);
    }

    private Population evolve() {
        return select()
                .crossover()
                .mutate();
    }

    private Population mutate() {
        return new Population(context, geneticParameters, population.map(GeneticIndividual::mutate), mutationStrategy);
    }

    private Population crossover() {
        return new Population(context, geneticParameters,
                this.population.zipWith(this.population.shuffle(),
                        GeneticIndividual::crossover
                ).toArray(),
                mutationStrategy);
    }

    private Population select() {
        return new Population(context, geneticParameters,
                Array.fill(population.size(),
                        () -> pickRandomTournament()
                                .sortBy(GeneticIndividual::value)
                                .reverse()
                                .head()), mutationStrategy);
    }

    private Array<GeneticIndividual> pickRandomTournament() {
        return Array.fill(geneticParameters.tournamentSize(), () -> population.get(rnd.nextInt(population.length())));
    }

    @Override
    public String toString() {
        return "Population{" +
                "geneticParameters=" + geneticParameters +
                ", population=" + population +
                '}';
    }
}
