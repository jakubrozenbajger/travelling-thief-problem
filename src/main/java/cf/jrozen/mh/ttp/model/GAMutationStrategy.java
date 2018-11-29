package cf.jrozen.mh.ttp.model;

import cf.jrozen.mh.ttp.slover.genetic.GeneticParameters;

public class GAMutationStrategy implements MutationStrategy {

    private final Context context;
    private final GeneticParameters geneticParameters;

    public GAMutationStrategy(Context context, GeneticParameters geneticParameters) {
        this.context = context;
        this.geneticParameters = geneticParameters;
    }

    @Override
    public int[] mutate(int[] locations) {
        final int[] cloned = locations.clone();
        for (int currInd = 0; currInd < locations.length; currInd++) {
            if (geneticParameters.nextMutate()) {
                final int randomInd = context.nextIntInDims();
                int tmp = cloned[currInd];
                cloned[currInd] = cloned[randomInd];
                cloned[randomInd] = tmp;
            }
        }
        return cloned;
    }

}
