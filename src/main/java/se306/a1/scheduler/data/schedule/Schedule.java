package se306.a1.scheduler.data.schedule;

import se306.a1.scheduler.data.graph.Edge;
import se306.a1.scheduler.data.graph.Graph;
import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.util.exception.ScheduleException;

import java.util.*;

/**
 * The purpose of this class is to store the scheduling information for each
 * node, i.e. the startTime of the task and the processor which the task is
 * allocated to.
 *
 * @author Luke Thompson
 */
public class Schedule implements Comparable<Schedule> {
    private final Map<Node, Processor> scheduledTasks;
    private final Set<Node> unscheduledTasks;
    private final List<Processor> processors;
    private final Graph graph;
    private int length;
    private int cost;

    /**
     * Constructor for Schedule which generates a specified number of processors
     * when the object is instantiated.
     *
     * @param numProcessors the number of processors as specified in command line
     */
    public Schedule(Graph graph, int numProcessors) {
        scheduledTasks = new HashMap<>();
        unscheduledTasks = new HashSet<>();
        processors = new ArrayList<>();
        this.graph = graph;
        length = 0;
        cost = 0;

        for (int i = 1; i <= numProcessors; i++) {
            processors.add(new Processor("" + i));
        }
    }

    /**
     * Constructor for Schedule when creating a schedule as an extension of the
     * parent tasks schedule.
     *
     * @param scheduledTasks map of scheduled tasks and their processor
     * @param unscheduledTasks set of unscheduled tasks
     * @param numProcessors the number of processors as specified in command line
     * @param graph graph object representing nodes and edges
     * @param length total time for schedule to complete
     * @param cost underestimate of schedule time
     */
    public Schedule(Map<Node, Processor> scheduledTasks, Set<Node> unscheduledTasks, int numProcessors, Graph graph, int length, int cost) {
        this.scheduledTasks = scheduledTasks;
        this.unscheduledTasks = unscheduledTasks;
        this.processors = new ArrayList<>();
        this.graph = graph;
        this.length = length;
        this.cost = cost;

        for (int i = 1; i <= numProcessors; i++) {
            processors.add(new Processor("" + i));
        }
    }

    /**
     * Constructor for Schedule when creating a schedule as an extension of the
     * parent tasks schedule.
     *
     * @param scheduledTasks map of scheduled tasks and their processor
     * @param unscheduledTasks set of unscheduled tasks
     * @param processors list of processors
     * @param graph graph object representing nodes and edges
     * @param length total time for schedule to complete
     * @param cost underestimate of schedule time
     */
    public Schedule(Map<Node, Processor> scheduledTasks, Set<Node> unscheduledTasks, List<Processor> processors, Graph graph, int length, int cost) {
        this.scheduledTasks = scheduledTasks;
        this.unscheduledTasks = unscheduledTasks;
        this.processors = processors;
        this.graph = graph;
        this.length = length;
        this.cost = cost;
    }

    /**
     * This method create a new node (task) to pair (startTime & processor) entry
     * in the map. Also adds the task to the specified processor at the given time.
     *
     * @param node      the task to be scheduled
     * @param processor the processor which the node (task) is scheduled on
     * @param startTime the starting time of the task to be added to the processor
     */
    public void addScheduledTask(Node node, Processor processor, int startTime) throws ScheduleException {
        scheduledTasks.put(node, processor);
        processor.schedule(node, startTime);

        length = Math.max(length, startTime + node.getCost());
        cost = Math.max(cost, startTime + graph.getBottomLevel(node));

        // TODO remove 'if' once working
        if (!unscheduledTasks.remove(node)) throw new ScheduleException("Task was not waiting to be scheduled or is already scheduled");

        for (Edge e : graph.getEdges(node)) {
            unscheduledTasks.add(e.getChild());
        }
    }

    public void addScheduledTask(Node node, int startTime) throws ScheduleException {
        addScheduledTask(node, processors.get(0), startTime);
    }

    public Map<Node, Processor> getScheduledTasks() {
        return scheduledTasks;
    }

    /**
     * This method returns the start time of the node (task) which is passed in.
     *
     * @param node the node (task) that the start time is wanted for
     * @return the start time of the node (task) on its processor
     * @throws ScheduleException if node hasn't been scheduled
     */
    public Integer getStartTime(Node node) throws ScheduleException {
        if (!scheduledTasks.containsKey(node)) {
            throw new ScheduleException("Start time not defined for task: " + node);
        }
        return scheduledTasks.get(node).getStartTime(node);
    }

    /**
     * This method returns the processor which the node (task) is allocated to.
     *
     * @param node the node (task) that the processor is wanted for
     * @return the processor which the node (task) is allocated to
     * @throws ScheduleException if node hasn't been scheduled on any processors
     */
    public Processor getProcessor(Node node) throws ScheduleException {
        if (!scheduledTasks.containsKey(node)) {
            throw new ScheduleException("Processor not defined for task: " + node);
        }
        return scheduledTasks.get(node);
    }

    /**
     * This method returns the overall length of the schedule, i.e. the time at which all
     * scheduled tasks have finished running.
     *
     * @return the length of the schedule
     */
    public int getLength() {
        return length;
    }

    public int getCost() {
        return cost;
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
     * This method retrieves the nodes (tasks) scheduled on the given processor.
     *
     * @param p the processor
     * @return the tasks scheduled on the processor
     */
    public Collection<Node> getTasks(Processor p) {
        return p.getScheduledTasks();
    }

    /**
     * This method returns a boolean based on whether the task passed in has
     * already been scheduled on a processor.
     *
     * @param node the node which is being checked for scheduling status
     * @return a boolean which is true if node has been scheduled, otherwise false
     */
    public boolean isScheduled(Node node) {
        return scheduledTasks.containsKey(node);
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

    /**
     * This method returns a set of unscheduled tasks for this schedule state.
     *
     * @return set of edges where the parent scheduled but the child  is not
     * yet scheduled.
     */
    public Set<Node> getUnscheduledTasks() {
        return unscheduledTasks;
    }

    /**
     * This method takes a set of egdes where the child node is an unscheduled
     * task and them to the set of visible tasks which are yet to be scheduled
     *
     * @param visibleTasks list of visible, yet unscheduled tasks
     */
    public void addUnscheduledTasks(List<Edge> visibleTasks) {
        for (Edge e : visibleTasks) {
            unscheduledTasks.add(e.getChild());
        }
    }

    @Override
    public int compareTo(Schedule other) {
        return Integer.compare(cost, other.cost);
    }
}