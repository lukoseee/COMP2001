package com.aim.project.ssp.hyperheuristics;

import com.aim.hyperheuristics.HeuristicPair;
import com.aim.project.ssp.interfaces.HeuristicInterface;

import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class RouletteWheel {

    public int[] m_aoHeuristicPairs;

    public LinkedHashMap<Integer, Integer> m_oHeuristicScores;

    public final int m_iUpperBound;

    public final int m_iLowerBound;

    public final int m_iDefaultScore;

    public final Random rng;

    /**
     * Constructs a Roulette Wheel Selection method using a LinkedHashMap.
     *
     * @param hs 			An array of heuristic IDs.
     * @param default_score The default score to give each heuristic.
     * @param lower_bound   The lower bound on the heuristic scores.
     * @param upper_bound   The upper bound on the heuristic scores.
     * @param rng           The random number generator.
     */
    public RouletteWheel(int [] hs, int default_score, int lower_bound, int upper_bound, Random rng) {

        this(new LinkedHashMap<Integer, Integer>(), hs, default_score, lower_bound, upper_bound, rng);
    }

    /**
     * Constructs a Roulette Wheel Selection method using the supplied Map.
     *
     * @param heuristic_scores An empty Map for mapping heuristic IDs to heuristic scores.
     * @param hs               The set of heuristic IDs for the (mutation, local_search) operators.
     * @param default_score    The default score to give each heuristic.
     * @param lower_bound      The lower bound on the heuristic scores.
     * @param upper_bound      The upper bound on the heuristic scores.
     * @param rng              The random number generator.
     */
    public RouletteWheel(LinkedHashMap<Integer, Integer> heuristic_scores, int [] hs,
                                  int default_score, int lower_bound, int upper_bound, Random rng) {

        this.m_aoHeuristicPairs = hs;
        this.m_oHeuristicScores = heuristic_scores;
        this.m_iUpperBound = upper_bound;
        this.m_iLowerBound = lower_bound;
        this.m_iDefaultScore = default_score;
        this.rng = rng;

        // initialise scores to the default_score
        for (int i = 0 ; i < m_aoHeuristicPairs.length ; i ++) {

            this.m_oHeuristicScores.put(hs[i], default_score);
        }

    }
    /**
     *
     * @param h The HeuristicPair to get the score of.
     * @return The score for the aforementioned heuristic. If the HeuristicScore
     *         does not exist, then you must return 0.
     */
    public int getScore(int h) {

        if (m_oHeuristicScores.get(h) == null)
        {
            return 0;
        }

        return m_oHeuristicScores.get(h);
    }

    /**
     * Increments the score (by 1) of the specified heuristic whilst respecting the
     * upper and lower bounds.
     *
     * @param h The HeuristicPair whose score should be incremented.
     */
    public void incrementScore(int h) {
        int score = getScore(h);

        if(score < m_iUpperBound)
        {
            score++;
        }
        m_oHeuristicScores.put(h ,score);

    }

    /**
     * Decrements the score (by 1) of the specified heuristic respecting the upper
     * and lower bounds.
     *
     * @param h The HeuristicPair whose score should be decremented.
     */
    public void decrementScore(int h) {

        int score = getScore(h);

        if(score > m_iLowerBound)
        {
            score--;
        }

        m_oHeuristicScores.put(h , score);
    }

    /**
     *
     * @return The sum of scores of all heuristics.
     */
    public int getTotalScore() {

        Set<Integer> pairs = m_oHeuristicScores.keySet();
        int sum = 0;

        for(int e : pairs )
        {
            sum += getScore(e);
        }

        return sum;

    }

    /**
     * See exercise sheet for pseudocode!
     *
     * @return A heuristic based on the RWS method.
     */
    public int performRouletteWheelSelection() {
        int total = getTotalScore();

        int random = rng.nextInt(total);
        int cumulativeScore = 0;
        int index = 0;
        int h = m_aoHeuristicPairs[index];

        while(cumulativeScore < random)
        {
            h = m_aoHeuristicPairs[index];
            cumulativeScore += getScore(h);
            index++;
        }

        return h;
    }

    /****************************************
     * Utility methods useful for debugging
     ****************************************/

    /**
     * Prints the heuristic IDs into the console.
     */
    public void printHeuristicIds() {

        String ids = "["
                + m_oHeuristicScores.entrySet().stream().map(e -> e.getKey().toString()).collect(Collectors.joining(", "))
                + "]";
        System.out.println("IDs    = " + ids);
    }

    /**
     * Prints the heuristic scores into the console.
     */
    public void printHeuristicScores() {

        String scores = "[" + m_oHeuristicScores.entrySet().stream().map(e -> e.getValue().toString())
                .collect(Collectors.joining(", ")) + "]";
        System.out.println("Scores = " + scores);
    }

}
