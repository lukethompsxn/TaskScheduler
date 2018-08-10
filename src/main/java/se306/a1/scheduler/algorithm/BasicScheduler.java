package se306.a1.scheduler.algorithm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se306.a1.scheduler.data.graph.Edge;
import se306.a1.scheduler.data.graph.Graph;
import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.data.schedule.Processor;
import se306.a1.scheduler.data.schedule.Schedule;
import se306.a1.scheduler.util.exception.ScheduleException;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is a basic implementation of a scheduler, and will produce an unoptimised schedule by greedily
 * assigning the tasks to the processors.
 *
 * @author Rodger Gu, Zhi Qiao, Abhinav Behal, Luke Thompson
 */
public class BasicScheduler extends Scheduler {

    /**
     * This method traverses the graph and creates the schedule.
     *
     * @throws ScheduleException if an error occurs when scheduling nodes
     */
    @Override
    protected void createSchedule() throws ScheduleException {
        schedule = new Schedule(graph, processors);

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
}
