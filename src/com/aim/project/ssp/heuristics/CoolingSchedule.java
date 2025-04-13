package com.aim.project.ssp.heuristics;


/** does geometric cooling **\
 *
 */
public class CoolingSchedule {

    private double dCurrentTemperature;
    private double alpha;

    public CoolingSchedule(double initialFitness)
    {
        this.dCurrentTemperature = initialFitness;
        this.alpha = 0.9995;
    }

    /**
     * Gets the current temperature.
     *
     * @return The current temperature.
     */
    public double getCurrentTemperature() {
        return this.dCurrentTemperature;
    }

    /**
     * Advances the temperature in accordance with the cooling schedule.
     */
    public void advanceTemperature() {
        dCurrentTemperature *= alpha;
    }
}
