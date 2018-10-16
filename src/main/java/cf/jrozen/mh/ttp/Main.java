package cf.jrozen.mh.ttp;


import cf.jrozen.mh.ttp.model.Context;
import cf.jrozen.mh.ttp.model.Parameters;
import cf.jrozen.mh.ttp.model.Population;
import cf.jrozen.mh.ttp.model.Problem;
import cf.jrozen.mh.ttp.utils.Loader;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        final Problem trivial_1 = Loader.load("trivial_1");
        final Parameters params = new Parameters(300, 30, 4, 0.2, 0.2);
        final Context context = new Context(trivial_1, params);


        System.out.println(Population.initRandom(context).evolve(50));

        System.out.println(Arrays.deepToString(context.distance()));

    }
}
