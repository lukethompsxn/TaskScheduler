package se306.a1.scheduler.algorithm;

import se306.a1.scheduler.data.Graph;

/**
 * This class represents an abstract scheduler.
 * This contains contracts for implementation specific schedulers and common
 * helper methods.
 * @author Luke Thompson
 * */
public abstract class Scheduler {

    /**
     * Initialises and executes the algorithm.
     * This method takes an input graph and other input parameters and outputs a completed schedule.
     *
     * @param the Graph object from Main.
     * @param numProcessors number of processors to be run on
     * @param numCores  number of cores to be run on (optional)
     * @param hasVisualisation  flag for specifying visualisation (optional)
     * @param output filepath of output .dot file (optional)
     */
    public abstract void init(Graph g,
              int numProcessors,
              int numCores,
              boolean hasVisualisation,
              String output);
}
