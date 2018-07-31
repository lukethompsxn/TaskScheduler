package se306.a1.scheduler.algorithm;

import java.util.*;

import se306.a1.scheduler.data.*;
import se306.a1.scheduler.util.ScheduleException;

/**
 * This class is a basic implementation of a scheduler, and will produce an unoptimised schedule by greedily
 * assigning the tasks to the processors.
 *
 * @author Rodger Gu
 */
public class BasicScheduler implements Scheduler {
    private Schedule schedule;
    private Graph g;

    @Override
    public void init(Graph g, int numProcessors, int numCores) {
        schedule = new Schedule(numProcessors);
        this.g = g;

        createSchedule();
    }

    /**
     * This method traverses the graph and actually creates the schedule. This is then
     * either output to console or output to a GUI.
     */
    private void createSchedule() {
        Set<Node> unscheduledNodes = new HashSet<>(g.getEntryNodes());
        Set<Node> scheduledNodes = new HashSet<>();
        Node currentNode;

        while (!unscheduledNodes.isEmpty()) {
            try {
                currentNode = computeCheapest(unscheduledNodes);
                System.out.println(currentNode + " : " + currentNode.getCost());

                for (Edge edge : g.getEdges(currentNode)){
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
    }

    /**
     * This method is given a list of visible tasks, and the costs of those tasks
     * and is meant to compute the cheapest possible task.
     */
    private Node computeCheapest(Collection<Node> nodes) throws ScheduleException {
        int time = Integer.MAX_VALUE;
        Node cheapest = null;
        Processor processor = null;
        ArrayList<Processor> processors = new ArrayList<>(schedule.getProcessors());

        //for each given node
        for (Node node : nodes) {
            System.out.println("N: " + node);

            //Determines whether all the parent tasks have already been scheduled to a processor
            //If not then skip this node
            if (!schedule.isScheduled(g.getParents(node))) continue;

            int earliestStart = Integer.MAX_VALUE;

            for (Node parent : g.getParents(node)) {
                int startTime = schedule.getStartTime(parent) + parent.getCost();

                for (Node otherParent : g.getParents(node)) {
                    if (!schedule.getProcessor(otherParent).equals(schedule.getProcessor(parent))) {
                        int transferTime = schedule.getStartTime(otherParent) + otherParent.getCost() + g.getCost(otherParent, node);
                        startTime = Math.max(transferTime, startTime);
                    } else {
                        startTime = Math.max(schedule.getProcessor(parent).getEarliestStartTime(), startTime);
                    }
                }

                if (startTime < time) {
                    time = earliestStart = startTime;
                    cheapest = node;
                    processor = schedule.getProcessor(parent);
                }
            }

            System.out.println("Earliest: " + earliestStart);
            if (earliestStart < Integer.MAX_VALUE) continue;

            //check each processor to see what the most available time is.
            for (Processor p : processors) {
                System.out.println("P: " + p);
                System.out.println("\t" + time);

                if (p.getEarliestStartTime() < time) {
                    time = p.getEarliestStartTime();
                    System.out.println("Earliest" + time);
                    cheapest = node;
                    processor = p;
                }
            }
        }

        schedule.addScheduledTask(cheapest, processor, time);
        return cheapest;
    }

}
