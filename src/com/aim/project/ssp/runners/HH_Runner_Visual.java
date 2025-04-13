package com.aim.project.ssp.runners;


import java.awt.Color;
import java.util.Random;

import com.aim.project.ssp.SightseeingProblemDomain;
import com.aim.project.ssp.instance.InitialisationMode;
import com.aim.project.ssp.instance.Location;
import com.aim.project.ssp.solution.SSPSolution;
import com.aim.project.ssp.visualiser.SSPView;

import AbstractClasses.HyperHeuristic;

/**
 * @author Warren G. Jackson
 * @since 17/03/2025
 * 
 * Runs a hyper-heuristic using a default configuration then displays the best solution found.
 */
public abstract class HH_Runner_Visual {

	private final long competitionSeconds = 30;
	private final long machineMilliseconds = 300_000;

	private long[] SEEDS;

	private final int TOTAL_RUNS = 5;

    protected HH_Runner_Visual() {
        SEEDS = generateSeeds();
    }

    public long[] generateSeeds()
	{
		Random random = new Random(21032025L);
		SEEDS = new long[TOTAL_RUNS];

		for(int i = 0; i < TOTAL_RUNS; i++)
		{
			SEEDS[i] = random.nextLong();
		}

		return SEEDS;
	}

	public void run() {
		
		//long seed = 17032008L;
		long timeLimit = getRuntime();
		System.out.println(timeLimit);

		for(int instance = 0; instance < 7 ; instance++) //run all instances
		{
			SightseeingProblemDomain problem = null;
			HyperHeuristic hh = null;

			for(int run = 0; run < TOTAL_RUNS; run++)
			{
				long seeds = SEEDS[run];
				problem = new SightseeingProblemDomain(seeds); //use same seed

				problem.loadInstance(instance); //load first instance
				problem.setMode(InitialisationMode.RANDOM);

				hh = getHyperHeuristic(seeds);
				hh.setTimeLimit(timeLimit);
				hh.loadProblemDomain(problem);
				hh.run();
			}

			System.out.println("Initial: ");
			problem.printInitialSolution();

			System.out.println("f(s_best) = " + hh.getBestSolutionValue());
			problem.printBestSolutionFound();

			new SSPView(problem.getLoadedInstance(), problem, Color.RED, Color.GREEN);
		}

	}
	/** 
	 * Transforms the best solution found, represented as an SSPSolution, into an ordering of Location's
	 * which the visualiser tool uses to draw the tour.
	 */
	protected Location[] transformSolution(SSPSolution solution, SightseeingProblemDomain problem) {
		
		return problem.getRouteOrderedByLocations();
	}

	public long getRuntime()
	{
		return (machineMilliseconds * competitionSeconds) / 600;
	}

	/**
	 * Allows a general visualiser runner by making the HyperHeuristic abstract.
	 * You can sub-class this class to run any hyper-heuristic that you want.
	 */
	protected abstract HyperHeuristic getHyperHeuristic(long seed);
}
