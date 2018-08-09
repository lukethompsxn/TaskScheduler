package se306.a1.scheduler.algorithm;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se306.a1.scheduler.data.graph.Graph;
import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.data.schedule.Processor;
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

    protected Schedule schedule;
    protected Graph graph;

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
    public Schedule run(Graph graph, int numProcessors, int numCores) throws ScheduleException {
        schedule = new Schedule(numProcessors);
        this.graph = graph;

        createSchedule();

        return schedule;
    }

    /**
     * Hook method to build the schedule which subclasses must implement.
     *
     * @throws ScheduleException if there is a scheduling error
     */
    protected abstract void createSchedule() throws ScheduleException;

    /**
     * This method is given a list of visible tasks and then computes
     * and schedules the cheapest possible task.
     */
    protected Node computeCheapest(Collection<Node> nodes) throws ScheduleException {
        Node cheapest = null;
        Processor processor = null;
        int minTime = Integer.MAX_VALUE;

        for (Node node : nodes) {
            if (!schedule.isScheduled(graph.getParents(node)))
                continue;

            for (Processor p : schedule.getProcessors()) {
                int time = p.getEarliestStartTime();

                for (Node parent : graph.getParents(node)) {
                    if (!schedule.getProcessor(parent).equals(p)) {
                        time = Math.max(time, graph.getCost(parent, node) + schedule.getStartTime(parent) + parent.getCost());
                    }
                }

                if (time < minTime) {
                    minTime = time;
                    processor = p;
                    cheapest = node;
                }
            }
        }

        logger.info("node: '" + cheapest + "'\tstarts at " + minTime + "s\ton processor [" + processor.getName() + "] for " + cheapest.getCost() + "s");
        schedule.addScheduledTask(cheapest, graph.getEdges(cheapest), processor, minTime);
        return cheapest;
    }
}
