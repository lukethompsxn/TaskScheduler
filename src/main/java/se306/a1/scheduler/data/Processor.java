package se306.a1.scheduler.data;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a processor and manages the tasks allocated to it.
 *
 * @author Rodger Gu, Abhinav Behal
 */
public class Processor {
    private final Map<Node, Integer> startTimes;
    private final String name;
    private int earliestStartTime;

    /**
     * Creates a new processor with a given name.
     *
     * @param name the name of the processor
     */
    Processor(String name) {
        startTimes = new HashMap<>();
        this.name = name;
        this.earliestStartTime = 0;
    }

    /**
     * Schedules a task (node) to be run on the processor.
     *
     * @param node the task to be scheduled
     * @param time the time at which to schedule the task
     */
    void schedule(Node node, int time) {
        if (startTimes.containsKey(node))
            throw new RuntimeException("Node " + node + " has already been scheduled");
        else if (time < earliestStartTime)
            throw new RuntimeException("Cannot schedule node " + node + " at time " + time);

        startTimes.put(node, time);
        earliestStartTime = node.getCost() + time;
    }

    /**
     * Gets the scheduled start time of the given node.
     *
     * @param node the node to get the start time of
     * @return the start time of the node
     * @throws RuntimeException if the node has not been scheduled on this processor
     */
    public int getStartTime(Node node) {
        if (!startTimes.containsKey(node))
            throw new RuntimeException("Node " + node + " has not been scheduled");

        return startTimes.get(node);
    }

    /**
     * Gets the earliest possible time at which another task can be started on this processor
     *
     * @return earliest time for another task to start
     */
    public int getEarliestStartTime() {
        return earliestStartTime;
    }

    /**
     * Gets the name of this processor.
     *
     * @return the name of this processor
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof Processor)) return false;
        return name.equals(((Processor) o).name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
