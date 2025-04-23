package com.aim.project.ssp.hyperheuristics;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import java.util.*;

public class RouletteSelectionHH extends HyperHeuristic {

    private final int m_iDefaultScore, m_iLowerBound, m_iUpperBound;

    public RouletteSelectionHH(long lSeed) {

        super(lSeed);

        this.m_iDefaultScore = 5;
        this.m_iLowerBound = 1;
        this.m_iUpperBound = 10;
    }

    @Override
    protected void solve(ProblemDomain problemDomain) {
        int CURRENT_SOLUTION_INDEX = 0;  int CROSSOVER_INDEX = 1 ; int CANDIDATE_SOLUTION_INDEX = 2;

        int[] heuristics = new int[problemDomain.getNumberOfHeuristics()];

        for(int i = 0 ; i < problemDomain.getNumberOfHeuristics(); i++)
        {
            heuristics[i] = i;
        }

        int[] crossoverHeuristics = problemDomain.getHeuristicsOfType(ProblemDomain.HeuristicType.CROSSOVER);
        Set<Integer> crossoverSet = new HashSet<>();

        for(int h : crossoverHeuristics)
        {
            crossoverSet.add(h);
        }

        problemDomain.initialiseSolution(CURRENT_SOLUTION_INDEX);

        RouletteWheel rws = new RouletteWheel(heuristics,m_iDefaultScore,m_iLowerBound,m_iUpperBound,rng);

        while(!hasTimeExpired()){

            int h = rws.performRouletteWheelSelection();

            if( crossoverSet.contains(h) )
            {
                problemDomain.applyHeuristic(h , CURRENT_SOLUTION_INDEX , CROSSOVER_INDEX , CANDIDATE_SOLUTION_INDEX);
            }
            else{
                problemDomain.applyHeuristic(h, CURRENT_SOLUTION_INDEX, CANDIDATE_SOLUTION_INDEX );
            }

            if (problemDomain.getFunctionValue(CANDIDATE_SOLUTION_INDEX) < problemDomain.getFunctionValue(CURRENT_SOLUTION_INDEX)) {

                rws.incrementScore(h);
                problemDomain.copySolution(CANDIDATE_SOLUTION_INDEX, CURRENT_SOLUTION_INDEX);

            } else if (problemDomain.getFunctionValue(CANDIDATE_SOLUTION_INDEX) > problemDomain.getFunctionValue(CURRENT_SOLUTION_INDEX)) {

                rws.decrementScore(h);
                problemDomain.copySolution(CURRENT_SOLUTION_INDEX , CANDIDATE_SOLUTION_INDEX);
            }

        }
    }

    @Override
    public String toString() {
        return "";
    }
}
