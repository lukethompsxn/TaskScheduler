package se306.a1.scheduler.algorithm;

import se306.a1.scheduler.data.TaskGraph;

/**
 * This class represents an abstract scheduler.
 * This contains contracts for implementation specific schedulers and common
 * helper methods.
 * @author Luke Thompson
 * */
public interface Scheduler {

    /**
     * Initialises and executes the algorithm.
     * This method will setup config parameters, manage parsing, then executes
     * the corresponding algorithm implementation as parameters supplied.
     *
     * @param graph graph containing tasks
     * @param input filepath of the input .dot file
     * @param numProcessors number of processors to be run on
     * @param numCores  number of cores to be run on (optional)
     * @param hasVisualisation  flag for specifying visualisation (optional)
     * @param output filepath of output .dot file (optional)
     */
    void init(TaskGraph graph,
              String input,
              int numProcessors,
              int numCores,
              boolean hasVisualisation,
              String output);
}
