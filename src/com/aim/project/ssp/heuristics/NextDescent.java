package com.aim.project.ssp.heuristics;


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
public class NextDescent extends HeuristicOperators implements HeuristicInterface {


	public NextDescent(Random random) {
	
		super(random);

		type = ProblemDomain.HeuristicType.LOCAL_SEARCH;

		map = TreeRangeMap.create();

		map.put(Range.closedOpen(0.0, 0.2), 1);
		map.put(Range.closedOpen(0.2, 0.4), 2);
		map.put(Range.closedOpen(0.4, 0.6), 3);
		map.put(Range.closedOpen(0.6, 0.8), 4);
		map.put(Range.closedOpen(0.8, 1.0), 5);

	}

	@Override
	public double apply(SSPSolutionInterface solution, double dos, double iom) {

		int iterations = queryMap(iom);

		double fitness = solution.getObjectiveFunctionValue(); //initial fitness
		boolean improved = false;

		for(int i = 0 ; i<iterations ; i++)
		{
			int n = solution.getNumberOfLocations() - 2;
			int index = m_oRandom.nextInt(n-1);

			doSwap(solution , index);

			double newFitness = m_oObjectiveFunction.getObjectiveFunctionValue(solution.getSolutionRepresentation());//calculate new fitness after swaps
			if (newFitness < fitness)
			{
				fitness = newFitness;
				improved = true;
				break; //found first improvement so break
			}
			else{
				doSwap(solution, index); //revert swap
			}
		}

		if(improved)
		{
			solution.setObjectiveFunctionValue(fitness); //set new fitness
		}

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

		return false;
	}

	@Override
	public boolean usesDepthOfSearch() {

		return false;
	}
}
