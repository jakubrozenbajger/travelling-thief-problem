package cf.jrozen.mh.ttp;


import cf.jrozen.mh.ttp.model.Context;
import cf.jrozen.mh.ttp.model.Parameters;
import cf.jrozen.mh.ttp.model.Population;
import cf.jrozen.mh.ttp.model.Problem;
import cf.jrozen.mh.ttp.utils.Banner;
import cf.jrozen.mh.ttp.utils.ChartGenerator;
import cf.jrozen.mh.ttp.utils.Loader;
import com.google.common.math.Stats;
import io.vavr.collection.List;


public class Main {

    public static void main(String[] args) {
        System.out.println(Banner.title());

        final String problemName = "medium_2";
        final Parameters params = new Parameters(150, 200, 5, 0.037, 0.219);

        final List<Stats> stats = solve(problemName, params);

        System.out.println("Last stats: " + stats.last());

        chart(params).show(stats);
    }

    private static List<Stats> solve(String problemName, Parameters params) {
        final Problem trivial_1 = Loader.load(problemName);
        final Context context = new Context(trivial_1, params);
        final List<Population> evolutionHistory = Population.initRandom(context).runEvolution();
        return evolutionHistory.map(Population::stats);
    }

    private static ChartGenerator chart(Parameters parameters) {
        return new ChartGenerator("TTP GENETIC STATS", "generation", "profit", parameters);
    }
}
