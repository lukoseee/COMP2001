package com.aim.project.ssp.heuristics;

import AbstractClasses.ProblemDomain;
import com.aim.project.ssp.SightseeingProblemDomain;
import com.aim.project.ssp.interfaces.HeuristicInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;
import com.google.common.collect.Range;
import com.google.common.collect.TreeRangeMap;

import java.util.Arrays;
import java.util.Random;

public class Exchange extends HeuristicOperators implements HeuristicInterface {

    public Exchange(Random random ) {

        super(random);

        type = ProblemDomain.HeuristicType.MUTATION;
        map = TreeRangeMap.create();

        map.put(Range.closedOpen(0.0, 0.2), 1);
        map.put(Range.closedOpen(0.2, 0.4), 2);
        map.put(Range.closedOpen(0.4, 0.6), 4);
        map.put(Range.closedOpen(0.6, 0.8), 8);
        map.put(Range.closedOpen(0.8, 1.0), 16);
        map.put(Range.closed(1.0, 1.0), 32);

    }

    @Override
    public double apply(SSPSolutionInterface oSolution, double dDepthOfSearch, double dIntensityOfMutation) {
        int nMutations = queryMap(dIntensityOfMutation);

        for ( int i = 0 ; i < nMutations ; i++)
        {
            int n = oSolution.getNumberOfLocations() - 2;
            int y = m_oRandom.nextInt(n -1 );
            int j = m_oRandom.nextInt(n -1 );
            doExchange(oSolution, y , j);
        }

        double fitness = m_oObjectiveFunction.getObjectiveFunctionValue(oSolution.getSolutionRepresentation());
        oSolution.setObjectiveFunctionValue(fitness);

        return fitness;
    }

    @Override
    public boolean isCrossover() {
        return false;
    }

    @Override
    public boolean usesIntensityOfMutation() {
        return true;
    }

    @Override
    public boolean usesDepthOfSearch() {
        return false;
    }
}