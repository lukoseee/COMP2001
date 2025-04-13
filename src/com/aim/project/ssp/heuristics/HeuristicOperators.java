package com.aim.project.ssp.heuristics;

import AbstractClasses.ProblemDomain;
import com.aim.project.ssp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;
import com.aim.project.ssp.solution.SSPSolution;
import com.google.common.collect.Range;
import com.google.common.collect.TreeRangeMap;
import uk.ac.nott.cs.aim.helperfunctions.ArrayMethods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Warren G Jackson
 * @since 17/03/2025
 *
 * TODO implement any common functionality here so that your heuristics can reuse them.
 *	E.g.  you may want to implement the swapping of two sight-seeing locations here.
 *
 */
public class HeuristicOperators {

	protected ObjectiveFunctionInterface m_oObjectiveFunction;

	protected Random m_oRandom;

	protected TreeRangeMap<Double , Integer> map;

	public ProblemDomain.HeuristicType type;

	public HeuristicOperators(Random oRandom) {

		m_oRandom = oRandom;
	}

	public ProblemDomain.HeuristicType getType() {
		return type;
	}

	public int queryMap(double key) {

		Integer result = map.get(key);
		return (result == null) ? Integer.MIN_VALUE : result;
	}

	public void Reinsert(SSPSolutionInterface solution , int i , int j)
	{
		int[] tour = solution.getSolutionRepresentation().getSolutionRepresentation();
		int n = tour.length;

		//skip if same index
		if (i == j) return;

		// remove element
		int removed = tour[i];

		// shift elements
		if (i < j) {
			// moving right: shift left portion
			System.arraycopy(tour, i + 1, tour, i,
					j - i);
		} else {
			// moving left: shift right portion
			System.arraycopy(tour, j, tour, j + 1,
					i - j);
		}

		// reinsert
		tour[j] = removed;

	}

	public void doSwap(SSPSolutionInterface solution , int index)
	{
		int [] tour = solution.getSolutionRepresentation().getSolutionRepresentation();
		if (tour.length < 2) return; //remove unnesscary work

		int temp = tour[index]; //stores to be swapped variable
		tour[index] = tour[index + 1]; //move to the right
		tour[index + 1] = temp; // store back into the right
	}



	public void doExchange(SSPSolutionInterface solution, int i , int j)
	{
		if(i == j)
		{
			return;
		}

		int[] tour = solution.getSolutionRepresentation().getSolutionRepresentation();

		int temp = tour[i];
		tour[i] = tour[j];
		tour[j] = temp;
	}

	public SSPSolutionInterface createRandomPerm(SSPSolutionInterface solution , Random rng)
	{
		// Get the current solution's tour
		int[] tour = solution.getSolutionRepresentation().getSolutionRepresentation();

		// Convert the array into an ArrayList of Integer
		ArrayList<Integer> tourList = new ArrayList<>();
        for (int j : tour) {
            tourList.add(j);
        }

		// Shuffle the ArrayList using Collections.shuffle
		Collections.shuffle(tourList, rng);

		// Convert the ArrayList back to an array
		for (int i = 0; i < tour.length; i++) {
			tour[i] = tourList.get(i);
		}

		// Update the solution's tour with the shuffled array
		solution.getSolutionRepresentation().setSolutionRepresentation(tour);

		return solution;
	}

	public void setObjectiveFunction(ObjectiveFunctionInterface f) {

		m_oObjectiveFunction = f;
	}

}
