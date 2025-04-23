package com.aim.project.ssp.hyperheuristics;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;

import java.util.HashSet;
import java.util.Set;

public class ChoiceFunctionHH extends HyperHeuristic {

    private double[][] pairwiseScores;
    private double[] individualScores;
    private long[] lastUsedTimestamps;
    private int lastHeuristicUsed = -1;
    private long currentIteration = 0;

    private final double alpha = 0.4; // weight for individual performance
    private final double beta = 0.4;  // weight for pairwise score
    private final double gamma = 0.2; // weight for age

    public ChoiceFunctionHH(long lSeed) {

        super(lSeed);
    }

    @Override
    protected void solve(ProblemDomain problemDomain) {

        int CURRENT_SOLUTION_INDEX = 0;  int CROSSOVER_INDEX = 1 ; int CANDIDATE_SOLUTION_INDEX = 2;

        int heuristicCount = problemDomain.getNumberOfHeuristics();

        this.individualScores = new double[heuristicCount];
        this.pairwiseScores = new double[heuristicCount][heuristicCount];
        this.lastUsedTimestamps = new long[heuristicCount];

        int[] crossovers = problemDomain.getHeuristicsOfType(ProblemDomain.HeuristicType.CROSSOVER);
        Set<Integer> crossoverSet = new HashSet<>();

        for(int h : crossovers)
        {
            crossoverSet.add(h);
        }

        problemDomain.initialiseSolution(CURRENT_SOLUTION_INDEX);

        while(!hasTimeExpired())
        {
            currentIteration++;

            int selected = selectHeuristic(heuristicCount);
            double prevFitness = problemDomain.getFunctionValue(CURRENT_SOLUTION_INDEX);

            double newFitness = 0;

            if(crossoverSet.contains(selected))
            {
                newFitness = problemDomain.applyHeuristic(selected , CURRENT_SOLUTION_INDEX , CROSSOVER_INDEX , CANDIDATE_SOLUTION_INDEX);

            }else {

                newFitness = problemDomain.applyHeuristic(selected , CURRENT_SOLUTION_INDEX , CANDIDATE_SOLUTION_INDEX);
            }

            if(newFitness <= prevFitness)
            {
                problemDomain.copySolution(CANDIDATE_SOLUTION_INDEX , CURRENT_SOLUTION_INDEX);
            }else{

                problemDomain.copySolution(CURRENT_SOLUTION_INDEX , CANDIDATE_SOLUTION_INDEX);
            }


            updatePerformance(selected , lastHeuristicUsed , prevFitness , newFitness);
            lastHeuristicUsed = selected;
            lastUsedTimestamps[selected] = currentIteration;

        }

    }

    private int selectHeuristic(int heuristicCount) {

        double bestScore = Double.NEGATIVE_INFINITY;
        int bestIndex = -1;

        for (int i = 0; i < heuristicCount; i++) {
            double individual = individualScores[i];
            double pairwise = (lastHeuristicUsed != -1) ? pairwiseScores[lastHeuristicUsed][i] : 0;
            double age = currentIteration - lastUsedTimestamps[i];

            double choiceScore = alpha * individual + beta * pairwise + gamma * age;

            if (choiceScore > bestScore) {
                bestScore = choiceScore;
                bestIndex = i;
            }
        }

        // Fallback if something went wrong
        return bestIndex != -1 ? bestIndex : rng.nextInt(heuristicCount);
    }

    private void updatePerformance(int current, int previous, double oldFitness, double newFitness) {
        double delta = oldFitness - newFitness;

        // Reward improvement
        if (delta > 0) {
            individualScores[current] += delta;

            if (previous != -1) {
                pairwiseScores[previous][current] += delta;
            }
        }
    }

    @Override
    public String toString() {
        return "";
    }
}
