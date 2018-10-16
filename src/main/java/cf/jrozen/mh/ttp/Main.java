package cf.jrozen.mh.ttp;


import cf.jrozen.mh.ttp.model.Context;
import cf.jrozen.mh.ttp.model.Problem;
import cf.jrozen.mh.ttp.utils.Loader;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Problem trivial_1 = Loader.load("trivial_1");
        String scalatr = trivial_1.toString();

        System.out.println(Arrays.deepToString(new Context(trivial_1, 1.2, 2.2).distance()));

//        System.out.println(scalatr);

        System.out.println(4 + 6 * 6 + 60);
        System.out.println("DUPA");

    }
}
