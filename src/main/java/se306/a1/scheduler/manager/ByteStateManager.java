package se306.a1.scheduler.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se306.a1.scheduler.algorithm.BasicScheduler;
import se306.a1.scheduler.data.graph.Graph;
import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.data.schedule.ByteState;
import se306.a1.scheduler.data.schedule.Processor;
import se306.a1.scheduler.util.exception.ScheduleException;

import java.util.*;

/**
 * This class is used to maintain the state of states and to supply states
 * to the scheduler based on heuristics.
 */
public class ByteStateManager {
    private final Queue<ByteState> states;
    private final List<Node> nodes;
    private final Map<Node, Integer> nodeIndices;
    private final List<Processor> processors;
    private final Map<Processor, Integer> processorIndices;
    private int upperBound = Integer.MAX_VALUE;

    private static Logger logger = LogManager.getLogger(ByteStateManager.class.getSimpleName());

    public ByteStateManager(Graph graph, int numProcessors) throws ScheduleException {
        upperBound = new BasicScheduler().run(graph, numProcessors, 0).getLength();

        states = new PriorityQueue<>();
        nodes = new ArrayList<>(graph.getAllNodes());
        nodeIndices = new HashMap<>();
        processors = new ArrayList<>();
        processorIndices = new HashMap<>();

        for (int i = 0; i < nodes.size(); ++i) {
            nodeIndices.put(nodes.get(i), i);
        }

        for (int i = 0; i < numProcessors; ++i) {
            processors.add(new Processor("" + i));
            processorIndices.put(processors.get(i), i);
        }
    }

    public int indexOf(Node n) {
        return nodeIndices.get(n);
    }

    public int indexOf(Processor p) {
        return processorIndices.get(p);
    }

    public Node getNode(int i) {
        return nodes.get(i);
    }

    public Processor getProcessor(int i) {
        return processors.get(i);
    }

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
        if (state.getLength() > upperBound)
            return;
        states.add(state);
        logger.info("Schedule Queued. Queue Length = " + states.size());
    }

    /**
     * This method returns the best schedule at the current point in execution.
     * The schedule which is returned (the front of the queue) is determined
     * by heuristics which are implemented in the comparable method of schedule.
     *
     * @return the best schedule at current point in time
     */
    public ByteState dequeue() {
        logger.info("Best Schedule Retrieved");
        return states.poll();
    }
}
