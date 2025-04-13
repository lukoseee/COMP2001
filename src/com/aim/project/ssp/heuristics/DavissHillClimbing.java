package com.aim.project.ssp.heuristics;

import java.util.Random;
import java.util.stream.IntStream;

import AbstractClasses.ProblemDomain;
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
public class DavissHillClimbing extends HeuristicOperators implements HeuristicInterface {

	public DavissHillClimbing(Random random) {
	
		super(random);

		type = ProblemDomain.HeuristicType.LOCAL_SEARCH;

		map = TreeRangeMap.create();

		map.put(Range.closedOpen(0.0, 0.2), 1);
		map.put(Range.closedOpen(0.2, 0.4), 2);
		map.put(Range.closedOpen(0.4, 0.6), 3);
		map.put(Range.closedOpen(0.6, 0.8), 4);
		map.put(Range.closedOpen(0.8, 1.0), 5);

	}

	/**
	 * DAVIS's BIT HILL CLIMBING LECTURE SLIDE PSEUDO-CODE
	 *
	 *  bestEval = evaluate(currentSolution)
	 *  perm = createRandomPermutation(length(currentSolution))
	 *
	 *   for (j = 0; j < length(currentSolution); j++) { // performs a single pass of the solution
	 *
	 *       bitFlip(currentSolution, perm[j]) // flip the bit referenced to in perm's j^th index
	 *       tempEval = evaluate(solution)
	 *
	 *       if(tempEval < bestEval) {
	 *           bestEval = tempEval // accept the bit flip
	 *       } else {
	 *           bitFlip(currentSolution, j) // otherwise reject the bit flip
	 *       }
	 *   }
	 *
	 * @param problem The problem to be solved.
	 */

	@Override
	public double apply(SSPSolutionInterface solution, double dos, double iom) {

		int n = solution.getNumberOfLocations() - 2;
		double fitness = solution.getObjectiveFunctionValue();
		int iteration = queryMap(iom);
		solution = createRandomPerm(solution , m_oRandom);

		for (int i = 0 ; i < iteration ; i++)
		{
			int index = m_oRandom.nextInt(n - 1);
			doSwap(solution,index);

			double newFitness = m_oObjectiveFunction.getObjectiveFunctionValue(solution.getSolutionRepresentation());

			if(newFitness < fitness) {
				fitness = newFitness;

			}else{

				doSwap(solution,index);
			}
		}

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

		return false;
	}

	@Override
	public boolean usesDepthOfSearch() {

		return true;
	}
}
