package com.aim.project.ssp.heuristics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import AbstractClasses.ProblemDomain;
import com.aim.project.ssp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;
import com.aim.project.ssp.interfaces.XOHeuristicInterface;

/**
 *
 * @author Warren G Jackson
 * @since 17/03/2025
 * 
 * See `COMP2001-Project-2025.docx` for further details.
 *
 */
public class OX implements XOHeuristicInterface {
	
	private final Random random;
	
	private ObjectiveFunctionInterface f;

	public  ProblemDomain.HeuristicType type;

	public OX(Random random) {
		
		this.random = random;

		type = ProblemDomain.HeuristicType.CROSSOVER;
	}

	@Override
	public double apply(SSPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

		// TODO implementation of Order Crossover
		return -1.0d;
	}

	@Override
	public double apply(SSPSolutionInterface p1, SSPSolutionInterface p2, SSPSolutionInterface c, double depthOfSearch, double intensityOfMutation) {

		int n = p1.getNumberOfLocations() - 2;

		int[] parent1 = p1.getSolutionRepresentation().getSolutionRepresentation();
		int[] parent2 = p2.getSolutionRepresentation().getSolutionRepresentation();
		int[] child = c.getSolutionRepresentation().getSolutionRepresentation();

		//create sub tour indexes
		int start = random.nextInt(0 , n );
		int end  = random.nextInt(0 , n );

		// Ensure cut1 <= cut2
		if (start > end) {
			int temp = start; //flip otherwise
			start = end;
			end = temp;
		}

        Arrays.fill(child, -1); //replace all to -1 to indicate empty slot

		//copy subtour to child
        if (end + 1 - start >= 0) System.arraycopy(parent1, start, child, start, end + 1 - start);

		ArrayList<Integer> remaining = new ArrayList<>();

        for (int j : parent2) {

            if (!contains(child, j)) {
                remaining.add(j);
            }
        }

		int position = 0;
		for(int i = 0 ; i<child.length ; i++)
		{
			if(child[i] == -1)
			{
				child[i] = remaining.get(position);
				position++;
			}
		}

		c.getSolutionRepresentation().setSolutionRepresentation(child);

		double fitness = f.getObjectiveFunctionValue(c.getSolutionRepresentation());

		c.setObjectiveFunctionValue(fitness);

		return fitness;
	}

	/*
	 * TODO update the methods below to return the correct boolean value.
	 */

	public boolean contains(int[] arr, int val) {
		for (int x : arr) if (x == val) return true;
		return false;
	}

	@Override
	public boolean isCrossover() {

		return true;
	}

	@Override
	public boolean usesIntensityOfMutation() {

		return false;
	}

	@Override
	public boolean usesDepthOfSearch() {

		return false;
	}

	@Override
	public void setObjectiveFunction(ObjectiveFunctionInterface f) {
		
		this.f = f;
	}
	@Override
	public ProblemDomain.HeuristicType getType()
	{
		return this.type;
	}
}
