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
public class BasicScheduler extends Scheduler {

    private static Logger logger = LogManager.getLogger(BasicScheduler.class);

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
                System.out.println(currentNode + " : " + currentNode.getCost());

                for (Edge edge : g.getEdges(currentNode)) {
                    System.out.println(edge);
                    if (!scheduledNodes.contains(edge.getChild())) {
                        unscheduledNodes.add(edge.getChild());
                    }
                }

                scheduledNodes.add(currentNode);
                unscheduledNodes.remove(currentNode);

                System.out.println("Scheduled:\t" + scheduledNodes);
                System.out.println("Unscheduled:\t" + unscheduledNodes);
            } catch (ScheduleException e) {
                e.printStackTrace();
            }
        }

        for (Processor processor : schedule.getProcessors()) {
            System.out.println(processor);
        }
    }

}
