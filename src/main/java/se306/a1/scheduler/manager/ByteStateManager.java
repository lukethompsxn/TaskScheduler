package se306.a1.scheduler.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se306.a1.scheduler.algorithm.BasicScheduler;
import se306.a1.scheduler.data.graph.Graph;
import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.data.schedule.ByteState;
import se306.a1.scheduler.data.schedule.Processor;
import se306.a1.scheduler.util.exception.ScheduleException;
import se306.a1.scheduler.visualisation.Visualiser;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * This class is used to maintain the state of states and to supply states
 * to the scheduler based on heuristics.
 */
public class ByteStateManager {
    private final Queue<ByteState> states;
    private final Set<ByteState> seenStates;
    private final List<Node> nodes;
    private final Map<Node, Integer> nodeIndices;
    private final List<Processor> processors;
    private final Map<Processor, Integer> processorIndices;
    private int upperBound = Integer.MAX_VALUE;
    protected final Graph graph;
    private ScheduledFuture task;

    private ByteState latestState;

    private static Logger logger = LogManager.getLogger(ByteStateManager.class.getSimpleName());

    public ByteStateManager(Graph graph, int numProcessors, boolean isVisualised) throws ScheduleException {
        upperBound = new BasicScheduler().run(graph, numProcessors, 1, false).getLength();

        states = new PriorityQueue<>();
        seenStates = new HashSet<>();
        nodes = new ArrayList<>(graph.getAllNodes());
        nodeIndices = new HashMap<>();
        processors = new ArrayList<>();
        processorIndices = new HashMap<>();
        this.graph = graph;

        for (int i = 0; i < nodes.size(); ++i) {
            nodeIndices.put(nodes.get(i), i);
        }

        for (int i = 0; i < numProcessors; ++i) {
            processors.add(new Processor("" + i));
            processorIndices.put(processors.get(i), i);
        }

        if (isVisualised) {
            updaterService();
        }
    }

    /**
     * Get the array index of a specified node.
     *
     * @param n node to get index of
     * @return index of specified node
     */
    public int indexOf(Node n) {
        return nodeIndices.get(n);
    }

    /**
     * Get the array index of a specified processor.
     *
     * @param p processor to get index of
     * @return index of specified processor
     */
    public Integer indexOf(Processor p) {
        return processorIndices.get(p);
    }

    /**
     * Get the Node object stored at the specified index.
     *
     * @param i index position of node to get
     * @return node at specified index location
     */
    public Node getNode(int i) {
        return nodes.get(i);
    }

    /**
     * Get the Processor object stored at the specified index.
     *
     * @param i index position of processor to get
     * @return processor at specified index location
     */
    public Processor getProcessor(int i) {
        return processors.get(i);
    }

    /**
     * Get a list of all processors available to this manager.
     *
     * @return list of all processors
     */
    public List<Processor> getProcessors() {
        return processors;
    }

    /**
     * This method add a new schedule to the priority queue.
     * Queuing order in the queue is based on the heuristics which are
     * implemented in the overridden comparable method for schedule.
     *
     * @param state a schedule instance to add to the queue
     */
    public void queue(ByteState state) {
        if (state.getLength() > upperBound || seenStates.contains(state))
            return;

        states.add(state);
        seenStates.add(state);

        logger.info("Schedule Queued. Queue Length = " + states.size());
    }

    /**
     * This method returns the best schedule at the current point in execution.
     * The schedule which is returned (the front of the queue) is determined
     * by heuristics which are implemented in the comparable method of schedule.
     *
     * @return the best schedule at current point in execution
     */
    public ByteState dequeue() {
        latestState = states.peek();
        logger.info("Best Schedule Retrieved");
        return states.poll();
    }

    /**
     * This method is called if visualisation is chosen at command-line.
     * Once called, this method will periodically update the GUI window which
     * visualises the state space search done by the scheduler.
     */
    private void updaterService() {
        final Visualiser visualiser = new Visualiser(this, graph);

        Runnable updateSchedule = () -> {
            if (latestState != null) {
                visualiser.updateCurrentState(latestState);
            }

            if (latestState.getFreeNodes().isEmpty()) {
                visualiser.updateCurrentState(latestState);
                System.out.println("cancelled");
                task.cancel(true);
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        task = executor.scheduleAtFixedRate(updateSchedule, 0, 50, TimeUnit.MILLISECONDS);
    }
}

