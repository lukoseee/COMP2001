package com.aim.project.ssp.solution;

import com.aim.project.ssp.interfaces.SolutionRepresentationInterface;

/**
 * @author Warren G Jackson
 * @since 17/03/2025
 *
 */
public class SolutionRepresentation implements SolutionRepresentationInterface {

	private int[] aiSolutionRepresentation;

	public SolutionRepresentation(int[] aiRepresentation) {

		this.aiSolutionRepresentation = aiRepresentation;
	}

	@Override
	public int[] getSolutionRepresentation() {

		return aiSolutionRepresentation;
	}

	@Override
	public void setSolutionRepresentation(int[] aiSolutionRepresentation) {

		this.aiSolutionRepresentation = aiSolutionRepresentation;
	}

	@Override
	public int getNumberOfLocations() {

		return this.aiSolutionRepresentation.length + 2; //offset for airport and hotel;
	}

	@Override
	public SolutionRepresentationInterface clone() {
		try {
			SolutionRepresentation cloned = (SolutionRepresentation) super.clone();
			cloned.aiSolutionRepresentation = this.aiSolutionRepresentation.clone(); // Deep copy array
			return cloned;

		} catch (CloneNotSupportedException e) {

			throw new AssertionError("Clone should always be supported");
		}
	}

}
