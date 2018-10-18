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

        final Problem trivial_1 = Loader.load("medium_4");
        final Parameters params = new Parameters(200, 30, 7, 1.037, 0.19);
        final Context context = new Context(trivial_1, params);

        final List<Population> evolutionHistory = Population.initRandom(context).evolve(180);
        final List<Stats> stats = evolutionHistory.map(Population::stats);

        System.out.println("Last stats: " + stats.last());

        chart().show(stats);
    }

    private static ChartGenerator chart() {
        return new ChartGenerator("TTP GENETIC STATS", "generation", "profit");
    }
}
