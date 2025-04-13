package com.aim.project.ssp.heuristics;

import AbstractClasses.ProblemDomain;
import com.aim.project.ssp.interfaces.HeuristicInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;
import com.google.common.collect.Range;
import com.google.common.collect.TreeRangeMap;

import java.util.Random;

public class SimulatedAnnealing  extends HeuristicOperators implements HeuristicInterface {

    public SimulatedAnnealing(Random oRandom) {
        super(oRandom);

        type = ProblemDomain.HeuristicType.LOCAL_SEARCH;

        map = TreeRangeMap.create();

        //uses the same map as davis hill climbing
        map.put(Range.closedOpen(0.0, 0.2), 1);
        map.put(Range.closedOpen(0.2, 0.4), 2);
        map.put(Range.closedOpen(0.4, 0.6), 3);
        map.put(Range.closedOpen(0.6, 0.8), 4);
        map.put(Range.closedOpen(0.8, 1.0), 5);
    }

    @Override
    public double apply(SSPSolutionInterface oSolution, double dDepthOfSearch, double dIntensityOfMutation) {

        int n = oSolution.getNumberOfLocations() - 2;
        double fitness = oSolution.getObjectiveFunctionValue();
        int depth = queryMap(dDepthOfSearch);

        CoolingSchedule coolingSchedule = new CoolingSchedule(fitness);

        for(int i = 0; i<depth ; i++)
        {
            double p = m_oRandom.nextDouble();
            double oldFitness = m_oObjectiveFunction.getObjectiveFunctionValue(oSolution.getSolutionRepresentation());

            int y = m_oRandom.nextInt(n);
            int j = m_oRandom.nextInt(n);
            doExchange(oSolution, y, j);

            double newFitness = m_oObjectiveFunction.getObjectiveFunctionValue(oSolution.getSolutionRepresentation());

            double delta = newFitness - oldFitness;

            if (delta < 0 || p < Math.exp(-delta / coolingSchedule.getCurrentTemperature())) {
                fitness = newFitness; // accepted
            } else {
                doExchange(oSolution, y, j); // revert
            }

            coolingSchedule.advanceTemperature();

        }

        oSolution.setObjectiveFunctionValue(fitness);
        return fitness;
    }

    @Override
    public boolean isCrossover() {
        return false;
    }

    @Override
    public boolean usesIntensityOfMutation() {
        return false;
    }

    @Override
    public boolean usesDepthOfSearch() {
        return true;
    }
}
