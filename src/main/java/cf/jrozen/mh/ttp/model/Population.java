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
    private final FinishingStrategy finishingStrategy;

    private Stats calcStats() {
        return Stats.of(population.map(GeneticIndividual::value));
    }

    public Stats stats() {
        return statsLazy.get();
    }

    private Population(Context context, GeneticParameters geneticParameters, Array<GeneticIndividual> population, MutationStrategy mutationStrategy, FinishingStrategy finishingStrategy) {
        this.context = context;
        this.geneticParameters = geneticParameters;
        this.population = population;
        this.mutationStrategy = mutationStrategy;
        this.finishingStrategy = finishingStrategy;
    }

    public static Population initRandom(Context context, GeneticParameters geneticParameters, MutationStrategy mutationStrategy, FinishingStrategy finishingStrategy) {
        return new Population(context, geneticParameters, initRandomPopulation(context, geneticParameters, mutationStrategy), mutationStrategy, finishingStrategy);
    }

    private static Array<GeneticIndividual> initRandomPopulation(Context context, GeneticParameters geneticParameters, MutationStrategy mutationStrategy) {
        return Array.fill(geneticParameters.populationSize(), () -> new GeneticIndividual(context, geneticParameters, mutationStrategy));
    }

    public List<Population> runEvolution() {
        final List<Population> evolution = evolve(geneticParameters.noOfGenerations());
        return evolution.last().population.maxBy(GeneticIndividual::value)
                .map(GeneticIndividual::locations)
                .map(finishingStrategy::finish)
                .map(l -> new GeneticIndividual(context, geneticParameters, l, mutationStrategy))
                .map(e -> evolution.append(new Population(context, geneticParameters, Array.of(e), mutationStrategy, finishingStrategy)))
                .getOrElse(evolution);
//        return evolution;
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
        return new Population(context, geneticParameters, population.map(GeneticIndividual::mutate), mutationStrategy, finishingStrategy);
    }

    private Population crossover() {
        return new Population(context, geneticParameters,
                this.population.zipWith(this.population.shuffle(),
                        GeneticIndividual::crossover
                ).toArray(),
                mutationStrategy,
                finishingStrategy
        );
    }

    private Population select() {
        return new Population(context, geneticParameters,
                Array.fill(population.size(),
                        () -> pickRandomTournament()
                                .sortBy(GeneticIndividual::value)
                                .reverse()
                                .head()), mutationStrategy, finishingStrategy);
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
