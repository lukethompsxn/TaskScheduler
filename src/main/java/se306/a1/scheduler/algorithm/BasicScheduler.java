package se306.a1.scheduler.algorithm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se306.a1.scheduler.data.*;
import se306.a1.scheduler.util.ScheduleException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is a basic implementation of a scheduler, and will produce an unoptimised schedule by greedily
 * assigning the tasks to the processors.
 *
 * @author Rodger Gu, Zhi Qiao, Abhinav Behal, Luke Thompson
 */
public class BasicScheduler extends Scheduler {

    // Logger for runtime logging
    private static Logger logger = LogManager.getLogger(BasicScheduler.class.getSimpleName());

    private Schedule schedule;
    private Graph graph;

    @Override
    public Schedule run(Graph graph, int numProcessors, int numCores) throws ScheduleException {
        schedule = new Schedule(numProcessors);
        this.graph = graph;

        createSchedule();
        return schedule;
    }

    /**
     * This method traverses the graph and creates the schedule.
     * @throws ScheduleException if an error occurs when scheduling nodes
     */
    private void createSchedule() throws ScheduleException {
        Set<Node> unscheduledNodes = new HashSet<>(graph.getEntryNodes());
        Set<Node> scheduledNodes = new HashSet<>();
        Node currentNode;

        while (!unscheduledNodes.isEmpty()) {
            currentNode = computeCheapest(unscheduledNodes);
            logger.info(currentNode + " : " + currentNode.getCost());

            for (Edge edge : graph.getEdges(currentNode)) {
                logger.info(edge);
                if (!scheduledNodes.contains(edge.getChild())) {
                    unscheduledNodes.add(edge.getChild());
                }
            }

            scheduledNodes.add(currentNode);
            unscheduledNodes.remove(currentNode);

            logger.info("Scheduled:\t" + scheduledNodes);
            logger.info("Unscheduled:\t" + unscheduledNodes);
        }
        logger.info(schedule.getProcessors());
    }

<<<<<<< 71a5f4f72fe71c4e5379876ac0eb7d96fb7de885
    /**
     * This method is given a list of visible tasks and then computes
     * and schedules the cheapest possible task.
     * @throws ScheduleException if an error occurs when scheduling nodes
     */
    private Node computeCheapest(Collection<Node> nodes) throws ScheduleException {
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
        schedule.addScheduledTask(cheapest, processor, minTime);
        return cheapest;
    }
=======
>>>>>>> Refactored methods from BasicScheduler into Scheduler
}
