package se306.a1.scheduler.algorithm;

import se306.a1.scheduler.data.Graph;
import se306.a1.scheduler.data.Schedule;
import se306.a1.scheduler.util.ScheduleException;

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
     * This method will take config parameters and execute the corresponding
     * algorithm implementation according to supplied parameters.
     *
     * @param graph         graph containing nodes (tasks)
     * @param numProcessors number of processors to be run on
     * @param numCores      number of cores to be multi-threaded on (optional)
     * @return the calculated schedule of nodes (tasks) on processors
     * @throws ScheduleException if an error occurs when scheduling nodes
     */
    Schedule run(Graph graph,
                 int numProcessors,
                 int numCores) throws ScheduleException;
}
