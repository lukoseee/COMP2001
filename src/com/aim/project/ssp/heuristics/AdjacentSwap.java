package com.aim.project.ssp.heuristics;

import AbstractClasses.ProblemDomain;
import com.aim.project.ssp.SightseeingProblemDomain;
import com.aim.project.ssp.interfaces.HeuristicInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;
import com.google.common.collect.Range;
import com.google.common.collect.TreeRangeMap;

import java.util.Arrays;
import java.util.Random;


/**
 *
 * @author Warren G Jackson
 * @since 17/03/2025
 * 
 * See `COMP2001-Project-2025.docx` for further details.
 *
 */
public class AdjacentSwap extends HeuristicOperators implements HeuristicInterface {

	public AdjacentSwap(Random random) {

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
	public double apply(SSPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

		int nMutations = queryMap(intensityOfMutation);

		for ( int i = 0 ; i < nMutations ; i++)
		{
			int n = solution.getNumberOfLocations() - 2;
			int index = m_oRandom.nextInt(n - 1);
			doSwap(solution , index);
		}
		double fitness = m_oObjectiveFunction.getObjectiveFunctionValue(solution.getSolutionRepresentation());

		solution.setObjectiveFunctionValue(fitness);

		return fitness;
	}

	/*
	 * TODO update the methods below to return the correct boolean value.
	 */

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
