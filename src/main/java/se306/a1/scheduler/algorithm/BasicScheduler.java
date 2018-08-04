package se306.a1.scheduler.algorithm;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se306.a1.scheduler.data.Edge;
import se306.a1.scheduler.data.Graph;
import se306.a1.scheduler.data.Node;
import se306.a1.scheduler.data.Processor;
import se306.a1.scheduler.data.Schedule;
import se306.a1.scheduler.util.ScheduleException;

/**
 * This class is a basic implementation of a scheduler, and will produce an unoptimised schedule by greedily
 * assigning the tasks to the processors.
 *
 * @author Rodger Gu, Zhi Qiao, Abhinav Behal, Luke Thompson
 */
public class BasicScheduler implements Scheduler {

    private static Logger logger = LogManager.getLogger(BasicScheduler.class.getSimpleName());

    private Schedule schedule;
    private Graph g;

    @Override
    public Schedule run(Graph g, int numProcessors, int numCores) {
        schedule = new Schedule(numProcessors);
        this.g = g;

        createSchedule();
        return schedule;
    }

    /**
     * This method traverses the graph and creates the schedule.
     */
    private void createSchedule() {
        Set<Node> unscheduledNodes = new HashSet<>(g.getEntryNodes());
        Set<Node> scheduledNodes = new HashSet<>();
        Node currentNode;

        while (!unscheduledNodes.isEmpty()) {
            try {
                currentNode = computeCheapest(unscheduledNodes);
                logger.info(currentNode + " : " + currentNode.getCost());

                for (Edge edge : g.getEdges(currentNode)) {
                    logger.info(edge);
                    if (!scheduledNodes.contains(edge.getChild())) {
                        unscheduledNodes.add(edge.getChild());
                    }
                }

                scheduledNodes.add(currentNode);
                unscheduledNodes.remove(currentNode);

                logger.info("Scheduled:\t" + scheduledNodes);
                logger.info("Unscheduled:\t" + unscheduledNodes);
            } catch (ScheduleException e) {
                e.printStackTrace();
            }
        }

        for (Processor processor : schedule.getProcessors()) {
            logger.info(processor);
        }
    }

    /**
     * This method is given a list of visible tasks and then computes
     * and schedules the cheapest possible task.
     */
    private Node computeCheapest(Collection<Node> nodes) throws ScheduleException {
        Node cheapest = null;
        Processor processor = null;
        int minTime = Integer.MAX_VALUE;

        for (Node node : nodes) {
            if (!schedule.isScheduled(g.getParents(node)))
                continue;

            for (Processor p : schedule.getProcessors()) {
                int time = p.getEarliestStartTime();

                for (Node parent : g.getParents(node)) {
                    if (!schedule.getProcessor(parent).equals(p)) {
                        time = Math.max(time, g.getCost(parent, node) + schedule.getStartTime(parent) + parent.getCost());
                    }
                }

                if (time < minTime) {
                    minTime = time;
                    processor = p;
                    cheapest = node;
                }
            }
        }

        logger.info("time:\t" + minTime + "\tnode:\t" + cheapest + "\ton " + processor.getName() + " for " + cheapest.getCost());
        schedule.addScheduledTask(cheapest, processor, minTime);
        return cheapest;
    }

}
