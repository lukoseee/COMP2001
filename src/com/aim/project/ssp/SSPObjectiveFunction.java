package com.aim.project.ssp;

import com.aim.project.ssp.instance.Location;
import com.aim.project.ssp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.ssp.interfaces.SSPInstanceInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;
import com.aim.project.ssp.interfaces.SolutionRepresentationInterface;
import com.aim.project.ssp.solution.SSPSolution;

import java.util.ArrayList;

/**
 * @author Warren G Jackson
 * @since 17/03/2025
 */
public class SSPObjectiveFunction implements ObjectiveFunctionInterface {
	
	private final SSPInstanceInterface oInstance;
	
	public SSPObjectiveFunction(SSPInstanceInterface oInstance) {
		
		this.oInstance = oInstance;
	}

	@Override
	public double getObjectiveFunctionValue(SolutionRepresentationInterface oSolution) {

		int [] tour = oSolution.getSolutionRepresentation();
		double airport = getCostBetweenAirportAnd(tour[tour.length-1]);
		double hotel = getCostBetweenHotelAnd(tour[0]);

		double sum = 0;

		for(int i = 0 ; i < tour.length - 1; i++){

			double a = getCost( tour[i] , tour[i+1] );
			sum += a;
		}
		return sum + airport + hotel;
	}
	
	@Override
	public double getCost(int iLocationA, int iLocationB) {

		//immediate exit if same
		if(iLocationA == iLocationB)
		{
			return 0.0;
		}

		Location a = this.oInstance.getSightseeingLocation(iLocationA);
		Location b = this.oInstance.getSightseeingLocation(iLocationB);

		double yDiff = a.y() - b.y();
		double xDiff = a.x() - b.x();

        return Math.sqrt( (xDiff * xDiff) + (yDiff * yDiff) );
	}

	@Override
	public double getCostBetweenHotelAnd(int iLocation) {

		Location hotel = this.oInstance.getHotelLocation();
		Location a = this.oInstance.getSightseeingLocation(iLocation);

		double yDiff = hotel.y() - a.y();
		double xDiff = hotel.x() - a.x();

		return Math.sqrt( (xDiff * xDiff) + (yDiff * yDiff) );
	}

	@Override
	public double getCostBetweenAirportAnd(int iLocation) {

		Location airport = this.oInstance.getAirportLocation();
		Location a = this.oInstance.getSightseeingLocation(iLocation);

		double yDiff = a.y() - airport.y();
		double xDiff = a.x() - airport.x();

		return Math.sqrt( (xDiff * xDiff) + (yDiff * yDiff) );
	}
}
