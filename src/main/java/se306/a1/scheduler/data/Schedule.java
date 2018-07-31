package se306.a1.scheduler.data;

import se306.a1.scheduler.util.ScheduleException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The purpose of this class is to store the scheduling information for each
 * node, i.e. the startTime of the task and the processor which the task is
 * allocated to.
 *
 * @author Luke Thompson
 */
public class Schedule {
    private Map<Node, Processor> scheduledTasks = new HashMap<>();
    private List<Processor> processors;

    /**
     * Constructor for Schedule which generates a specified number of processors
     * when the object is instantiated.
     *
     * @param numProcessors the number of processors as specified in command line
     */
    public Schedule(int numProcessors) {
        for (int i = 1; i <= numProcessors; i++) {
            this.processors.add(new Processor(Integer.toString(i)));
        }
    }

    /**
     * This method create a new node (task) to pair (startTime & processor) entry
     * in the map.
     *
     * @param node      the task which the startTime & processor are for
     * @param processor the processor which the node (task) is carried out on
     */
    public void addScheduledTask(Node node, Processor processor) {
        scheduledTasks.put(node, processor);
    }

    /**
     * This method returns the start time of the node (task) which is passed in.
     *
     * @param node the node (task) that the start time is wanted for
     * @return the start time of the node (task) on a processor
     */
    public Integer getStartTime(Node node) throws ScheduleException {
        if (scheduledTasks.get(node) == null) {
            throw new ScheduleException("Start time not defined for task: " + node);
        }
        return scheduledTasks.get(node).getStartTime(node);
    }

    /**
     * This method returns the processor which the node (task) is allocated to.
     *
     * @param node the node (task) that the processor is wanted for.
     * @return the processor which the node (task) is allocated to
     */
    public Processor getProcessor(Node node) throws ScheduleException {
        if (scheduledTasks.get(node) == null) {
            throw new ScheduleException("Processor not defined for task: " + node);
        }
        return scheduledTasks.get(node);
    }

    /**
     * This method returns the processors which are associated with this
     * schedule.
     *
     * @return list of processors
     */
    public List<Processor> getProcessors() {
        return processors;
    }

    /**
     * This method returns a boolean based on whether the task passed in has
     * already been scheduled on a processor.
     *
     * @param node the node which is being checked for scheduling status
     * @return a boolean which is true if node has been scheduled, otherwise false
     */
    public boolean isScheduled(Node node) {
        return scheduledTasks.keySet().contains(node);
    }

    /**
     * This method returns a boolean based on whether the list of tasks passed
     * in have all been scheduled already on a processor.
     *
     * @param nodes list of nodes which are being checked for scheduling status
     * @return a boolean which is true if all nodes have already been scheduled,
     * otherwise false
     */
    public boolean isScheduled(List<Node> nodes) {
        return scheduledTasks.keySet().containsAll(nodes);
    }
}
