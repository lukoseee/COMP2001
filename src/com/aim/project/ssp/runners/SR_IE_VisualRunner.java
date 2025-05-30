package com.aim.project.ssp.runners;


import com.aim.hyperheuristics.RLILS_AM_HH;
import com.aim.project.ssp.hyperheuristics.ChoiceFunctionHH;
import com.aim.project.ssp.hyperheuristics.RouletteSelectionHH;
import com.aim.project.ssp.hyperheuristics.SR_IE_HH;

import AbstractClasses.HyperHeuristic;

/**
 * @author Warren G Jackson
 * @since 17/03/2025
 *
 * Runs a simple random IE hyper-heuristic then displays the best solution found.
 */
public class SR_IE_VisualRunner extends HH_Runner_Visual {

	@Override
	protected HyperHeuristic getHyperHeuristic(long seed) {

		return new ChoiceFunctionHH(seed);
	}
	
	public static void main(String [] args) {

		HH_Runner_Visual runner = new SR_IE_VisualRunner();
		runner.run();
	}

}
