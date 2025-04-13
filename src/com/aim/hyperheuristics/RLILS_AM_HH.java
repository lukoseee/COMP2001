package com.aim.hyperheuristics;



import com.aim.HyFlexUtilities;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import AbstractClasses.ProblemDomain.HeuristicType;

/**
 * @author Warren G Jackson
 * @since 20/03/2025
 */
public class RLILS_AM_HH extends HyperHeuristic {
	
	private final int m_iDefaultScore, m_iLowerBound, m_iUpperBound;
	/**
	 * 
	 * @param seed The experimental seed.
	 * @param defaultScore The default score to give each heuristic in RWS.
	 * @param lowerBound The lower bound for each heuristic's score in RWS.
	 * @param upperBound The upper bound for each heursitic's score in RWS.
	 */
	public RLILS_AM_HH(long seed, int defaultScore, int lowerBound, int upperBound) {
		
		super(seed);
		
		this.m_iDefaultScore = defaultScore;
		this.m_iLowerBound = lowerBound;
		this.m_iUpperBound = upperBound;
	}

	/**
	 * TODO - Implement a selection hyper-heuristic using a reinforcement learning based
	 * roulette wheel selection (RWS) heuristic selection method accepting all moves (AM).
	 * 
	 * PSEUDOCODE:
	 * 
	 * INPUT: problem_instance, default_score, lower_bound, upper_bound
	 * mtns <- { MUTATION } 
	 * lss <- { LOCAL_SEARCH } 
	 * hs <- { (a, b) | a <- mtns, b <- lss }
	 * s <- initialiseSolution();
	 * rws <- initialiseNewRouletteWheelSelectionMethod();
	 * 
	 * WHILE termination criterion is not met DO
	 * 
	 *     h <- rws.performRouletteWheelSelection();
	 *     s' <- h(s);
	 *     
	 *     updateHeuristicScore(h_i, f(s), f(s'));
	 *     
	 *     accept(); // all moves
	 * END_WHILE
	 * 
	 * return s_{best};
	 */
	
	// remember to update the roulette wheel selection based on feedback
	public void solve(ProblemDomain oProblem) {

		int[] mtn = oProblem.getHeuristicsOfType(HeuristicType.MUTATION);
		int[] lss = oProblem.getHeuristicsOfType(HeuristicType.LOCAL_SEARCH);

		HeuristicPair[] pairs = new HeuristicPair[mtn.length * lss.length];

		int index = 0;
		for ( int mutation : mtn)
		{
			for (int localSearch : lss)
			{
				pairs[index] = new HeuristicPair(mutation,localSearch);
				index++;
			}
		}
		int CURRENT_SOLUTION_INDEX = 0; int CANDIDATE_SOLUTION_INDEX = 1;

		oProblem.initialiseSolution(CURRENT_SOLUTION_INDEX);

		RouletteWheelSelection rws = new RouletteWheelSelection(pairs,m_iDefaultScore,m_iLowerBound,m_iUpperBound,rng);
		while(!hasTimeExpired()){

			HeuristicPair h = rws.performRouletteWheelSelection();
			oProblem.applyHeuristic(h.h1() , CURRENT_SOLUTION_INDEX, CANDIDATE_SOLUTION_INDEX );
			oProblem.applyHeuristic(h.h2() , CANDIDATE_SOLUTION_INDEX, CANDIDATE_SOLUTION_INDEX );

			if (oProblem.getFunctionValue(CANDIDATE_SOLUTION_INDEX) < oProblem.getFunctionValue(CURRENT_SOLUTION_INDEX)) {
				rws.incrementScore(h);
			} else if (oProblem.getFunctionValue(CANDIDATE_SOLUTION_INDEX) > oProblem.getFunctionValue(CURRENT_SOLUTION_INDEX)) {
				rws.decrementScore(h);
			}

			oProblem.copySolution(CANDIDATE_SOLUTION_INDEX, CURRENT_SOLUTION_INDEX);

			rws.printHeuristicIds();
			rws.printHeuristicScores();
		}

	}
	
	public String toString() {

		return "RL-ILS_AM_HH";
	}

}
