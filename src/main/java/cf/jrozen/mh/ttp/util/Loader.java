package cf.jrozen.mh.ttp.util;

import cf.jrozen.mh.ttp.model.Node;
import cf.jrozen.mh.ttp.model.Problem;
import cf.jrozen.mh.ttp.model.Section;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Loader {

    private static final String DIR_NAME = "fixtures/";
    private static final String FILE_EXT = ".ttp";
    public static final String REGEX = ":\\p{javaSpaceChar}{2,}";

    public static Problem load(String filename) {
        final Scanner scanner = new Scanner(Loader.class.getClassLoader().getResourceAsStream(DIR_NAME + filename + FILE_EXT));

        final String problemName = scanner.nextLine().replaceAll(REGEX, ":").split(":")[1];
        final String knapsackDataType = scanner.nextLine().replaceAll(REGEX, ":").split(":")[1];
        final int dimension = Integer.parseInt(scanner.nextLine().replaceAll(REGEX, ":").split(":")[1]);
        final int numberOfItems = Integer.parseInt(scanner.nextLine().replaceAll(REGEX, ":").split(":")[1]);
        final int capacityOfKnapsack = Integer.parseInt(scanner.nextLine().replaceAll(REGEX, ":").split(":")[1]);
        final double minSpeed = Double.parseDouble(scanner.nextLine().replaceAll(REGEX, ":").split(":")[1]);
        final double maxSpeed = Double.parseDouble(scanner.nextLine().replaceAll(REGEX, ":").split(":")[1]);
        final double rentingRatio = Double.parseDouble(scanner.nextLine().replaceAll(REGEX, ":").split(":")[1]);
        final String edgeWeightType = scanner.nextLine().replaceAll(REGEX, ":").split(":")[1];

        final List<Node> nodes = scanner.tokens().takeWhile("ITEMS SECTION"::contains).map(Loader::nodeFromLine).collect(Collectors.toList());
        final List<Section> section = scanner.tokens().map(Loader::sectionFromLine).collect(Collectors.toList());

        return new Problem(
                problemName, knapsackDataType, dimension, numberOfItems, capacityOfKnapsack,
                minSpeed, maxSpeed, rentingRatio, edgeWeightType, nodes, section
        );
    }

    private static Node nodeFromLine(String line) {
        final double[] ints = Arrays.stream(line.split(REGEX)).mapToDouble(Double::parseDouble).toArray();
        return new Node((int) ints[0], ints[1], ints[2]);
    }

    private static Section sectionFromLine(String line) {
        final double[] ints = Arrays.stream(line.split(REGEX)).mapToDouble(Double::parseDouble).toArray();
        return new Section((int) ints[0], ints[1], ints[2], (int) ints[3]);
    }
}
