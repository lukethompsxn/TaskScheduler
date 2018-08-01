package se306.a1.scheduler.algorithm;

import se306.a1.scheduler.data.Graph;
import se306.a1.scheduler.data.Schedule;

/**
 * This class represents an abstract scheduler.
 * This contains contracts for implementation specific schedulers and common
 * helper methods.
 *
 * @author Luke Thompson
 */
public interface Scheduler {

    /**
     * Initialises and executes the algorithm.
     * This method will setup config parameters, manage parsing, then executes
     * the corresponding algorithm implementation as parameters supplied.
     *
     * @param graph         graph containing tasks
     * @param numProcessors number of processors to be run on
     * @param numCores      number of cores to be run on (optional)
     */
    Schedule run(Graph graph,
                 int numProcessors,
                 int numCores);
}
