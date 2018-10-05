package cf.jrozen.mh.ttp;

import cf.jrozen.mh.ttp.util.Loader;

public class Main {

    public static void main(String[] args) {
//        var adsfasdf = "adsfasdf";

        String javaTr = Loader.load("trivial_1").toString();
        System.out.println(javaTr);
        String scalatr = cf.jrozen.mh.ttp.utils.Loader.load("trivial_1").toString();
        System.out.println(scalatr);
        System.out.println(javaTr.equals(scalatr));
    }
}
