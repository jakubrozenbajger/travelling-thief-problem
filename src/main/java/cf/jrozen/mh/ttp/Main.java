package cf.jrozen.mh.ttp;


import cf.jrozen.mh.ttp.model.Context;
import cf.jrozen.mh.ttp.model.Parameters;
import cf.jrozen.mh.ttp.model.Population;
import cf.jrozen.mh.ttp.model.Problem;
import cf.jrozen.mh.ttp.utils.ChartGenerator;
import cf.jrozen.mh.ttp.utils.Loader;
import com.google.common.math.Stats;
import io.vavr.collection.List;

import java.util.Arrays;


public class Main {

    public static void main(String[] args) {

        final Problem trivial_1 = Loader.load("easy_0");
        final Parameters params = new Parameters(300, 30, 7, 0.02, 0.09);
        final Context context = new Context(trivial_1, params);


        final List<Population> evolutionHistory = Population.initRandom(context).evolve(150);
        final List<Stats> stats = evolutionHistory.map(Population::stats);

        System.out.println(stats);
        System.out.println(Arrays.deepToString(context.distance()));
        showChart(stats);
    }

    private static void showChart(List<Stats> evolutionHistory) {
        new ChartGenerator("TTP GENETIC STATS", "generation", "distance")
                .show(evolutionHistory);
    }
}
