package se306.a1.scheduler.algorithm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se306.a1.scheduler.data.graph.Graph;
import se306.a1.scheduler.data.schedule.Schedule;
import se306.a1.scheduler.util.exception.ScheduleException;

/**
 * This class represents an abstract scheduler.
 * This contains contracts for implementation specific schedulers and common
 * helper methods.
 *
 * @author Luke Thompson
 */
public abstract class Scheduler {

    // Logger for runtime logging
    protected static Logger logger = LogManager.getLogger(BasicScheduler.class.getSimpleName());

    protected Graph graph;
    protected int processors;
    protected int cores;
    protected boolean isVisualised;

    /**
     * Initialises and executes the algorithm.
     * This method will setup config parameters, manage parsing, then executes
     * the corresponding algorithm implementation as parameters supplied.
     * The hook method createSchedule is called before returning  the schedule.
     *
     * @param graph         graph containing tasks
     * @param numProcessors number of processors to be run on
     * @param numCores      number of cores to be run on (optional)
     * @throws ScheduleException if there is a scheduling error
     */
    public Schedule run(Graph graph, int numProcessors, int numCores, boolean isVisualised) throws ScheduleException {
        this.graph = graph;
        this.processors = numProcessors;
        this.cores = numCores;
        this.isVisualised = isVisualised;

        return createSchedule();
    }

    /**
     * Hook method to build the schedule which subclasses must implement.
     *
     * @throws ScheduleException if there is a scheduling error
     */
    protected abstract Schedule createSchedule() throws ScheduleException;
}
