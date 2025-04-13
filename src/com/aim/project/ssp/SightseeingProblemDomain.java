package com.aim.project.ssp;


import com.aim.project.ssp.heuristics.*;
import com.aim.project.ssp.instance.InitialisationMode;
import com.aim.project.ssp.instance.Location;
import com.aim.project.ssp.instance.SSPInstance;
import com.aim.project.ssp.instance.reader.SSPInstanceReader;
import com.aim.project.ssp.interfaces.*;

import AbstractClasses.ProblemDomain;
import com.aim.project.ssp.solution.SSPSolution;
import com.aim.project.ssp.solution.SolutionRepresentation;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Warren G Jackson
 * @since 17/03/2025
 *
 * Ensure that you reference https://people.cs.nott.ac.uk/pszwj1/chesc2011/javadoc/index.html?help-doc.html
 * when implementing each method to be HyFlex API compliant.
 */
public class SightseeingProblemDomain extends ProblemDomain implements Visualisable, InLabPracticalExamInterface {
	public SSPSolutionInterface m_oBestSolution;

	private SSPSolutionInterface[] solutionMemory;

	private final ArrayList<SSPInstanceInterface> loadedInstances;

	private Map<Integer, String> instanceIDs = null; //holds all instance paths

	private SSPInstanceInterface loadedInstance;

	private InitialisationMode mode;

	private SSPSolutionInterface initialSolution;

	private final HeuristicInterface[] heuristics;

	public SightseeingProblemDomain(long seed ) {

        super(seed);

		AdjacentSwap adjacentSwap = new AdjacentSwap(rng , this);
		DavissHillClimbing hillClimbing = new DavissHillClimbing(rng);
		NextDescent nextDescent = new NextDescent(rng,this);
		OX ox = new OX(rng);
		Reinsertion reinsertion = new Reinsertion(rng, this);
		PMX pmx = new PMX(rng);
		Exchange exchange = new Exchange(rng , this);
		SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(rng);

		heuristics = new HeuristicInterface[8];

		heuristics[0] = adjacentSwap;
		heuristics[1] = hillClimbing;
		heuristics[2] = nextDescent;
		heuristics[3] = ox;
		heuristics[4] = reinsertion;
		heuristics[5] = pmx;
		heuristics[6] = exchange;
		heuristics[7] = simulatedAnnealing;

		setMemorySize(8); //number of heuristics;

		loadedInstances = new ArrayList<SSPInstanceInterface>();

        // TODO - set default memory size and create the array of low-level heuristics
		// ...
		instanceIDs = new LinkedHashMap<Integer, String>();

		instanceIDs.put(0 , "instances/ssp/square.ssp");
		instanceIDs.put(1 , "instances/ssp/libraries-15.ssp");
		instanceIDs.put(2 , "instances/ssp/carparks-40.ssp");
		instanceIDs.put(3 , "instances/ssp/tramstops-85.ssp");
		instanceIDs.put(4 , "instances/ssp/grid.ssp");
		instanceIDs.put(5 , "instances/ssp/clustered.ssp");
		instanceIDs.put(6 , "instances/ssp/chatgpt-instance-100.ssp");
	}

	public SSPSolutionInterface getSolution(int index) {

		return solutionMemory[index];
	}
	
	public SSPSolutionInterface getBestSolution() {

		return m_oBestSolution;
	}

	public void setMode(InitialisationMode mode)
	{
		this.mode = mode;
	}


	@Override
	public double applyHeuristic(int hIndex, int currentIndex, int candidateIndex) {

		HeuristicInterface heuristic = heuristics[hIndex];

		if(heuristic.isCrossover())
		{
			copySolution(currentIndex , candidateIndex);
			return solutionMemory[currentIndex].getObjectiveFunctionValue();
		}

		SSPSolutionInterface workingCopy = solutionMemory[currentIndex].clone();

		double fitness = heuristic.apply(workingCopy, depthOfSearch , intensityOfMutation);

		solutionMemory[candidateIndex] = workingCopy;

		if(fitness < getBestSolutionValue())
		{
			m_oBestSolution = workingCopy;
		}

		return fitness;
	}

	@Override
	public double applyHeuristic(int hIndex, int parent1Index, int parent2Index, int candidateIndex) {

		HeuristicInterface heuristic = heuristics[hIndex];

		if(heuristic.isCrossover())
		{

			SSPSolutionInterface p1 = solutionMemory[parent1Index].clone();
			SSPSolutionInterface p2 = solutionMemory[parent2Index].clone();
			SSPSolutionInterface c = solutionMemory[candidateIndex];

			double fitness = ( (XOHeuristicInterface) heuristic).apply(p1, p2 , c, depthOfSearch , intensityOfMutation); //cast to crossover type to access other apply method

			solutionMemory[candidateIndex] = c;

			if(fitness < getBestSolutionValue())
			{
				m_oBestSolution = c;
			}

			return fitness;
		}
		else{

			return applyHeuristic(hIndex , parent1Index , candidateIndex);

		}

	}

	@Override
	public String bestSolutionToString() {

		if(m_oBestSolution == null)
		{
			return null;
		}
		SolutionRepresentationInterface solutionRepresentation = this.m_oBestSolution.getSolutionRepresentation();
		int[] solution = solutionRepresentation.getSolutionRepresentation();

		return Arrays.toString(solution);
	}

	@Override
	public boolean compareSolutions(int iIndex1, int iIndex2) {
		if (solutionMemory[iIndex1] == null || solutionMemory[iIndex2] == null) {
			return false;
		}

		int[] A = solutionMemory[iIndex1].getSolutionRepresentation().getSolutionRepresentation();
		int[] B = solutionMemory[iIndex2].getSolutionRepresentation().getSolutionRepresentation();

		return Arrays.equals(A , B);

	}

	@Override
	public void copySolution(int a, int b) {

		SSPSolutionInterface source = solutionMemory[a].clone();
		solutionMemory[b] = source;

		// TODO - BEWARE this should copy the solution, not the reference to it!
		//			That is, that if we apply a heuristic to the solution in index 'b',
		//			then it does not modify the solution in index 'a' or vice-versa.
	}

	@Override
	public double getBestSolutionValue() {

		SSPSolutionInterface solution = getBestSolution();

		if (solution == null) {
			return Double.NaN; // or throw exception if this is unexpected
		}

		return solution.getObjectiveFunctionValue();
	}
	
	@Override
	public double getFunctionValue(int index) {

		if(solutionMemory[index] == null)
		{
			return Double.NaN;
		}
		return solutionMemory[index].getObjectiveFunctionValue();
	}

	// TODO
	@Override
	public int[] getHeuristicsOfType(HeuristicType type) {

		ArrayList<Integer> heuristicsOfType = new ArrayList<Integer>();

		int index = 0;

		for(HeuristicInterface heuristic : heuristics)
		{
			if(heuristic.getType() == type){

				heuristicsOfType.add(index);
				index++;
			}
		}

		if(heuristicsOfType.isEmpty())
		{
			return null;
		}

		return heuristicsOfType.stream().mapToInt(Integer::intValue).toArray();
	}

	@Override
	public int[] getHeuristicsThatUseDepthOfSearch() {
		ArrayList<Integer> heuristicsOfType = new ArrayList<Integer>();

		int index = 0;

		for(HeuristicInterface heuristic : heuristics)
		{
			if(heuristic.usesDepthOfSearch()){

				heuristicsOfType.add(index);
				index++;
			}
		}

		if(heuristicsOfType.isEmpty())
		{
			return null;
		}

		return heuristicsOfType.stream().mapToInt(Integer::intValue).toArray();
	}

	@Override
	public int[] getHeuristicsThatUseIntensityOfMutation() {

		ArrayList<Integer> heuristicsOfType = new ArrayList<Integer>();

		int index = 0;

		for(HeuristicInterface heuristic : heuristics)
		{
			if(heuristic.usesIntensityOfMutation()){

				heuristicsOfType.add(index);
				index++;
			}
		}

		if(heuristicsOfType.isEmpty())
		{
			return null;
		}

		return heuristicsOfType.stream().mapToInt(Integer::intValue).toArray();
	}

	@Override
	public int getNumberOfHeuristics() {

		return 8;
	}

	@Override
	public int getNumberOfInstances() {

		return loadedInstances.size();
	}

	@Override
	public void initialiseSolution(int index) {

		if (index == 0) { // initialize only once

			solutionMemory[index] = loadedInstance.createSolution(mode);

			this.m_oBestSolution = solutionMemory[0].clone(); //update best to the initial solution

			this.initialSolution = solutionMemory[0].clone(); //update initial

		} else {
			// Clone the initial solution for other indices
			solutionMemory[index] = solutionMemory[0].clone();
		}

	}

	@Override
	public void loadInstance(int instanceId) {

		SSPInstanceReader reader = new SSPInstanceReader();
		Path path = Paths.get(instanceIDs.get(instanceId));

		if (!Files.exists(path)) {
            try {
                throw new IllegalAccessException("Instance file not found: " + path.toString());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

		SSPInstanceInterface instance = reader.readSSPInstance(path, this.rng); //create instance

		this.loadedInstance = instance; //update current

		loadedInstances.add(instance);

		for (int i = 0; i < solutionMemory.length; i++) {

			initialiseSolution(i); //initialise solutions for each
		}

		// TODO create instance reader and problem instance

		// TODO set the objective function in each of the heuristics

		for(HeuristicInterface heuristic : heuristics)
		{
			heuristic.setObjectiveFunction(loadedInstance.getSSPObjectiveFunction());
		}

	}

	@Override
	public void setMemorySize(int size) {

		solutionMemory = new SSPSolutionInterface[size];
	}

	@Override
	public String solutionToString(int index) {

		SSPSolutionInterface solutionInterface = solutionMemory[index];

		if (solutionInterface == null) {
			return "No solution stored at index " + index;
		}

		SolutionRepresentationInterface solutionRepresentation = solutionInterface.getSolutionRepresentation();

		int[] solution = solutionRepresentation.getSolutionRepresentation();

		return Arrays.toString(solution);
	}

	@Override
	public String toString() {
		return "psylc15 SSP Domain";
	}
	
	@Override
	public SSPInstanceInterface getLoadedInstance() {

		return this.loadedInstance;
	}

	@Override
	public Location[] getRouteOrderedByLocations() {
		ArrayList<Location> locations = this.loadedInstance.getSolutionAsListOfLocations(m_oBestSolution);

		return locations.toArray(new Location[0]);
	}

	/**
	 * Should print the best solution found in the form:
	 * (h_x,h_y) - (l_x0,l_y0) - ... - (l_x{n-1},l_y{n-1}) - (a_x,a_y)
	 * where:
	 * `h` is the hotel
	 * `a` is the airport
	 * `l_xi` is the x-coordinate of the location in the i^th index in the solution.
	 * `l_yi` is the y-coordinate of the location in the i^th index in the solution.
	 * <p>
	 * For example:
	 * (0,0) - (1,1) - (2,2) - (3,3) - (4,4)
	 */

	@Override
	public void printBestSolutionFound() {

		Location[] locations = getRouteOrderedByLocations();
		Location hotel = loadedInstance.getHotelLocation();
		Location airport = loadedInstance.getAirportLocation();

		if (locations == null || locations.length == 0 || hotel == null || airport == null) {
			System.out.println("No solution found.");
			return;
		}

		StringBuilder full = new StringBuilder();
		full.append("(").append(hotel.x()).append(",").append(hotel.y()).append(")");

        for (Location location : locations) {
            full.append(" - (").append(location.x()).append(",").append(location.y()).append(")");
        }

		full.append(" - (").append(airport.x()).append(",").append(airport.y()).append(")");

		System.out.println(full);
	}

	/**
	 * Prints the objective value of the best solution found.
	 */
	@Override
	public void printObjectiveValueOfTheSolutionFound() {
		double value = getBestSolutionValue();

		System.out.println(value);
	}

	/**
	 * Should print the initial solution:
	 * (h_x,h_y) - (l_x0,l_y0) - ... - (l_x{n-1},l_y{n-1}) - (a_x,a_y)
	 * where:
	 * `h` is the hotel
	 * `a` is the airport
	 * `l_xi` is the x-coordinate of the location in the i^th index in the solution.
	 * `l_yi` is the y-coordinate of the location in the i^th index in the solution.
	 * <p>
	 * For example:
	 * (0,0) - (2,2) - (1,1) - (3,3) - (4,4)
	 */
	@Override
	public void printInitialSolution() {

		// Get list of locations
		ArrayList<Location> locations = this.loadedInstance.getSolutionAsListOfLocations(this.initialSolution);
		Location hotel = loadedInstance.getHotelLocation();
		Location airport = loadedInstance.getAirportLocation();


		// Check if the list is empty
		if (locations.isEmpty()) {
			System.out.println("No solution found.");
			return;
		}

		StringBuilder full = new StringBuilder();
		full.append("(").append(hotel.x()).append(",").append(hotel.y()).append(")");

        for (Location location : locations) {
            full.append(" - (").append(location.x()).append(",").append(location.y()).append(")");
        }

		full.append(" - (").append(airport.x()).append(",").append(airport.y()).append(")");

		System.out.println(full);
	}

	/**
	 * Prints the objective value of the (first) initial solution generated.
	 */
	@Override
	public void printObjectiveValueOfTheInitialSolution() {

		System.out.println(this.initialSolution.getObjectiveFunctionValue());
	}
}
