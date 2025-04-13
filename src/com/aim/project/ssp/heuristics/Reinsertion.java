package com.aim.project.ssp.heuristics;

import java.util.Arrays;
import java.util.Random;

import AbstractClasses.ProblemDomain;
import com.aim.project.ssp.SightseeingProblemDomain;
import com.aim.project.ssp.interfaces.HeuristicInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;

import com.google.common.collect.Range;
import com.google.common.collect.TreeRangeMap;

/**
 *
 * @author Warren G Jackson
 * @since 17/03/2025
 * 
 * See `COMP2001-Project-2025.docx` for further details.
 *
 */
public class Reinsertion extends HeuristicOperators implements HeuristicInterface {
	

	public Reinsertion(Random random , SightseeingProblemDomain problemDomain) {

		super(random);

		type = ProblemDomain.HeuristicType.MUTATION;

		map = TreeRangeMap.create();

		map.put(Range.closedOpen(0.0, 0.2), 1);
		map.put(Range.closedOpen(0.2, 0.4), 2);
		map.put(Range.closedOpen(0.4, 0.6), 3);
		map.put(Range.closedOpen(0.6, 0.8), 4);
		map.put(Range.closedOpen(0.8, 1.0), 5);

	}

	@Override
	public double apply(SSPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

		int Nmutation = queryMap(intensityOfMutation);

		for (int i = 0 ; i < Nmutation ; i++)
		{
			int n = solution.getNumberOfLocations() - 2; //remove hotel and airport

			int y = m_oRandom.nextInt(n);
			int j = m_oRandom.nextInt(n);

			Reinsert(solution , y ,j);
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
