package com.aim.project.ssp.heuristics;

import AbstractClasses.ProblemDomain;
import com.aim.project.ssp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;
import com.aim.project.ssp.interfaces.XOHeuristicInterface;

import java.util.Arrays;
import java.util.Random;

public class PMX implements XOHeuristicInterface {

    private final Random random;

    private ObjectiveFunctionInterface f;

    public  ProblemDomain.HeuristicType type;

    public PMX(Random random) {

        this.random = random;

        type = ProblemDomain.HeuristicType.CROSSOVER;

    }
    @Override
    public double apply(SSPSolutionInterface oParent1, SSPSolutionInterface oParent2, SSPSolutionInterface oChild, double dDepthOfSearch, double dIntensityOfMutation) {
        int n = oParent1.getNumberOfLocations() - 2;

        int[] parent1 = oParent1.getSolutionRepresentation().getSolutionRepresentation();
        int[] parent2 = oParent2.getSolutionRepresentation().getSolutionRepresentation();
        int[] child = oChild.getSolutionRepresentation().getSolutionRepresentation();

        //create sub tour indexes
        int start = random.nextInt(0 , n );
        int end  = random.nextInt(0 , n );

        // Ensure cut1 <= cut2
        if (start > end) {
            int temp = start; //flip otherwise
            start = end;
            end = temp;
        }

        Arrays.fill(child , -1);

        //copy subtour to child
        if (end + 1 - start >= 0) System.arraycopy(parent1, start, child, start, end + 1 - start);

        for (int i = start; i <= end; i++) {
            int element = parent2[i];

            // If the child doesn't already contain this element
            if (!contains(child, element)) {
                int position = i;
                boolean placed = false;

                while (!placed) {
                    // Find the element in parent2 that is in the same position in parent1
                    int elementInP1 = parent1[position];
                    // Find where this element is in parent2
                    int newPosition = findIndex(parent2, elementInP1);

                    if (child[newPosition] == -1) {
                        child[newPosition] = element;
                        placed = true;
                    } else {
                        position = newPosition;
                    }
                }
            }
        }

        for (int i = 0; i < n; i++) {
            if (child[i] == -1) {
                child[i] = parent2[i];
            }
        }


        oChild.getSolutionRepresentation().setSolutionRepresentation(child);

        double fitness = f.getObjectiveFunctionValue(oChild.getSolutionRepresentation());
        oChild.setObjectiveFunctionValue(fitness);

        return fitness;
    }

    public boolean contains(int[] arr, int val) {
        for (int x : arr) if (x == val) return true;
        return false;
    }

    private static int findIndex(int[] array, int element) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == element) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public double apply(SSPSolutionInterface oSolution, double dDepthOfSearch, double dIntensityOfMutation) {
        return 0;
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
    public void setObjectiveFunction(ObjectiveFunctionInterface oObjectiveFunction) {
        this.f = oObjectiveFunction;
    }



    @Override
    public ProblemDomain.HeuristicType getType() {
        return this.type;
    }
}
