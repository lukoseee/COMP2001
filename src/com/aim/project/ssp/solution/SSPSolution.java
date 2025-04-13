package com.aim.project.ssp.solution;

import com.aim.project.ssp.interfaces.SSPSolutionInterface;
import com.aim.project.ssp.interfaces.SolutionRepresentationInterface;

/**
 * @author Warren G Jackson
 * @since 17/03/2025
 *
 */
public class SSPSolution implements SSPSolutionInterface {

	private SolutionRepresentationInterface oRepresentation;
	
	private double iObjectiveFunctionValue;
	
	public SSPSolution(SolutionRepresentationInterface oRepresentation, double iObjectiveFunctionValue) {
		
		this.oRepresentation = oRepresentation;
		this.iObjectiveFunctionValue = iObjectiveFunctionValue;
	}

	@Override
	public double getObjectiveFunctionValue() {

		return iObjectiveFunctionValue;
	}

	@Override
	public void setObjectiveFunctionValue(double objectiveFunctionValue) {
		
		this.iObjectiveFunctionValue = objectiveFunctionValue;
	}

	@Override
	public SolutionRepresentationInterface getSolutionRepresentation() {
		
		return this.oRepresentation;
	}
	
	@Override
	public SSPSolutionInterface clone() {

		return new SSPSolution(this.oRepresentation.clone() , this.iObjectiveFunctionValue);

	}

	@Override
	public int getNumberOfLocations() {

		return this.oRepresentation.getNumberOfLocations();
	}
}
