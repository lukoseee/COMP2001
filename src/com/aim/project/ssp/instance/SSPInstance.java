package com.aim.project.ssp.instance;


import java.util.*;

import com.aim.project.ssp.SSPObjectiveFunction;
import com.aim.project.ssp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.ssp.interfaces.SSPInstanceInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;
import com.aim.project.ssp.interfaces.SolutionRepresentationInterface;
import com.aim.project.ssp.solution.SSPSolution;
import com.aim.project.ssp.solution.SolutionRepresentation;
import uk.ac.nott.cs.aim.helperfunctions.ArrayMethods;

/**
 * @author Warren G. Jackson
 * @since 17/03/2025
 *
 */
public class SSPInstance implements SSPInstanceInterface {
	
	private final Location[] aoLocations;
	
	private final Location oHotelLocation;
	
	private final Location oAirportLocation;
	
	private final int iNumberOfLocations;
	
	private final Random oRandom;
	
	private ObjectiveFunctionInterface f = null;
	
	public SSPInstance(int iNumberOfLocations, Location[] aoLocations, Location oHotelLocation, Location oAirportLocation, Random random) {
		
		this.iNumberOfLocations = iNumberOfLocations;
		this.oRandom = random;
		this.aoLocations = aoLocations;
		this.oHotelLocation = oHotelLocation;
		this.oAirportLocation = oAirportLocation;
		this.f = new SSPObjectiveFunction(this);
	}

	@Override
	public SSPSolution createSolution(InitialisationMode mode) {

		int[] tour = new int[iNumberOfLocations];

		if (mode == InitialisationMode.RANDOM)
		{
			// Step 1: Fill ArrayList with location indices
			ArrayList<Integer> temp = new ArrayList<>();
			for (int i = 0; i < iNumberOfLocations; i++) {
				temp.add(i);
			}

			// Step 2: Shuffle using instance's Random object
			Collections.shuffle(temp, oRandom);

			// Step 3: Copy shuffled list back to tour array
			for (int i = 0; i < tour.length; i++) {
				tour[i] = temp.get(i);
			}

		} else if (mode == InitialisationMode.CONSTRUCTIVE) {

			ArrayList<Integer> unvisited = new ArrayList<>();

			//add all ss locations to unvisisted array
			for(int i = 0  ; i < tour.length ; i++)
			{
				unvisited.add(i);
			}

			int current = findNearestToHotel(unvisited);
			tour[0] = current;

			unvisited.remove(Integer.valueOf(current)); //remove the nearest to hotel

			for (int i = 1; i < iNumberOfLocations; i++) {
				current = findNearestUnvisited(current, unvisited); //find next nearest
				tour[i] = current; //add to tour
				unvisited.remove(Integer.valueOf(current)); //remove visited one ( current )
			}

		}

		SolutionRepresentation solutionRepresentation = new SolutionRepresentation(tour);
		double obj = f.getObjectiveFunctionValue(solutionRepresentation);

		return new SSPSolution(solutionRepresentation, obj);

	}

	private int findNearestToHotel(List<Integer> unvisited) {
		double minDistance = Double.MAX_VALUE;
		int nearest = -1;

		for (int loc : unvisited) {
			double distance = f.getCostBetweenHotelAnd(loc);
			if (distance < minDistance) {
				minDistance = distance;
				nearest = loc;
			}
		}
		return nearest;
	}

	@Override
	public ObjectiveFunctionInterface getSSPObjectiveFunction() {

		return this.f;
	}

	@Override
	public int getNumberOfLocations() {

		return this.iNumberOfLocations;
	}

	@Override
	public Location getSightseeingLocation(int iLocationId) {

		return this.aoLocations[iLocationId];
	}

	@Override
	public Location getHotelLocation() {

		return this.oHotelLocation;
	}

	@Override
	public Location getAirportLocation() {
		return this.oAirportLocation;
	}

	@Override
	public ArrayList<Location> getSolutionAsListOfLocations(SSPSolutionInterface oSolution) { //previously took in SSPSolutionInterface oSolution
		ArrayList<Location> locations = new ArrayList<>();

		SolutionRepresentationInterface solutionRepresentation = oSolution.getSolutionRepresentation();
		int[] locationIDs = solutionRepresentation.getSolutionRepresentation();

		for(Integer i : locationIDs)
		{
			locations.add(getSightseeingLocation(i));
		}

		return locations;
	}


	private int findNearestUnvisited(int location , ArrayList<Integer> unvisited)
	{
		double lowestDistance = Double.MAX_VALUE;
		int nearestUnvisited = 0;

		for(int i = 0 ; i < unvisited.size() ; i++)
		{
			double distance = f.getCost(location , unvisited.get(i));
			if ( distance <= lowestDistance) {
				lowestDistance = distance;
				nearestUnvisited = i;
			}
		}

		return unvisited.get(nearestUnvisited);

	}

}
