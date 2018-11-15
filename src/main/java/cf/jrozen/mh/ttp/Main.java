package cf.jrozen.mh.ttp;


import cf.jrozen.mh.ttp.model.Context;
import cf.jrozen.mh.ttp.model.Population;
import cf.jrozen.mh.ttp.model.Problem;
import cf.jrozen.mh.ttp.slover.genetic.GeneticParameters;
import cf.jrozen.mh.ttp.utils.Banner;
import cf.jrozen.mh.ttp.utils.ChartGenerator;
import cf.jrozen.mh.ttp.utils.Loader;
import com.google.common.math.Stats;
import io.vavr.collection.List;


public class Main {

    public static void main(String[] args) {
        System.out.println(Banner.title());

        final String problemName = "medium_1";
        final GeneticParameters params = new GeneticParameters(130, 400, 5, 0.022, 0.119);

        final List<Stats> stats = solve(problemName, params);

        System.out.println("Last stats: " + stats.last());

        chart(params).show(stats);
    }

    private static List<Stats> solve(String problemName, GeneticParameters params) {
        final Problem trivial_1 = Loader.load(problemName);
        final Context context = new Context(trivial_1);
        final List<Population> evolutionHistory = Population.initRandom(context, params).runEvolution();
        return evolutionHistory.map(Population::stats);
    }

    private static ChartGenerator chart(GeneticParameters geneticParameters) {
        return new ChartGenerator("TTP GENETIC STATS", "generation", "profit", geneticParameters);
    }
}
