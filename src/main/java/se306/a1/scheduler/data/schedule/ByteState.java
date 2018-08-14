package se306.a1.scheduler.data.schedule;

import se306.a1.scheduler.data.graph.Edge;
import se306.a1.scheduler.data.graph.Graph;
import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.manager.ByteStateManager;
import se306.a1.scheduler.util.Pair;

import java.util.*;

/**
 * Class representing a partial (or complete) state in the search space,
 * uses compact representations for task start times, processor information and
 * task availability to save on memory.
 */
public class ByteState implements Comparable<ByteState> {
    private final ByteStateManager manager;
    private final Graph graph;

    private final int[] virtualEdges;
    private final int[] startTimes;
    private final int[] processorIndices;
    private final int[] earliestStartTimes;
    private final byte[] free;

    private int cost;
    private int length;
    private int idleTime;
    private int bottomLevel;

    /**
     * Creates a new ByteState with a given manager and graph.
     *
     * @param manager the ByteStateManager responsible for this ByteState
     * @param graph   the graph associated with this ByteState
     */
    public ByteState(ByteStateManager manager, Graph graph) {
        this.manager = manager;
        this.graph = graph;
        int numNodes = graph.getAllNodes().size();
        int numProcessors = manager.getProcessors().size();

        this.virtualEdges = new int[numNodes];
        this.earliestStartTimes = new int[numProcessors];
        this.processorIndices = new int[numNodes];
        this.startTimes = new int[numNodes];
        this.free = new byte[numNodes / 8 + 1];

        for (Node n : graph.getEntryNodes()) {
            setFree(manager.indexOf(n), true);
        }

        // initialise the cost and idle time values according to the heuristic equations
        cost = 0;
        for (Node n : graph.getAllNodes()) {
            cost += n.getCost();
        }
        cost /= manager.getProcessors().size();
        idleTime = cost;

        int criticalPathLength = 0;
        for (Node n : graph.getEntryNodes())
            criticalPathLength = Math.max(criticalPathLength, graph.getBottomLevel(n));

        cost = Math.max(cost, criticalPathLength);

        Arrays.fill(this.virtualEdges, -1);
        Arrays.fill(this.startTimes, -1);
        Arrays.fill(this.processorIndices, -1);
    }

    /**
     * Creates a deep copy of the given ByteState (excluding the graph and manager objects).
     *
     * @param other the ByteState to copy
     */
    private ByteState(ByteState other) {
        virtualEdges = Arrays.copyOf(other.virtualEdges, other.virtualEdges.length);
        startTimes = Arrays.copyOf(other.startTimes, other.startTimes.length);
        earliestStartTimes = Arrays.copyOf(other.earliestStartTimes, other.earliestStartTimes.length);
        processorIndices = Arrays.copyOf(other.processorIndices, other.processorIndices.length);
        free = Arrays.copyOf(other.free, other.free.length);

        manager = other.manager;
        graph = other.graph;
        cost = other.cost;
        length = other.length;
        idleTime = other.idleTime;
        bottomLevel = other.bottomLevel;
    }

    /**
     * Schedule a task on the current state.
     *
     * @param node      the task to schedule
     * @param processor the processor to schedule on
     * @return a new ByteState with the task scheduled
     */
    public ByteState scheduleTask(Node node, Processor processor) {
        int nodeIndex = manager.indexOf(node);
        int processorIndex = manager.indexOf(processor);

        if (processorIndices[nodeIndex] != -1)
            throw new RuntimeException("Task has already been scheduled");
        if (!isFree(nodeIndex))
            throw new RuntimeException("Task is not free to schedule");

        int startTime = earliestStartTimes[processorIndex];

        for (Node parent : graph.getParents(node)) {
            int parentIndex = manager.indexOf(parent);

            if (processorIndices[parentIndex] != processorIndex) {
                startTime = Math.max(startTime,
                        startTimes[parentIndex] + parent.getCost() + graph.getCost(parent, node));
            }
        }

        ByteState newState = new ByteState(this);

        // update the new state
        newState.bottomLevel = Math.max(newState.bottomLevel, startTime + graph.getBottomLevel(node));
        newState.idleTime = (newState.idleTime * manager.getProcessors().size() + startTime - earliestStartTimes[processorIndex]) / manager.getProcessors().size();
        newState.cost = Math.max(newState.bottomLevel, newState.idleTime);

        newState.startTimes[nodeIndex] = startTime;
        newState.processorIndices[nodeIndex] = processorIndex;
        newState.earliestStartTimes[processorIndex] = startTime + node.getCost();
        newState.length = Math.max(newState.length, startTime + node.getCost());

        newState.setFree(nodeIndex, false);

        // recalculate the free nodes
        for (Edge e : graph.getEdges(node)) {
            Node child = e.getChild();
            int childIndex = manager.indexOf(child);
            boolean canSchedule = true;

            for (Node parent : graph.getParents(child)) {
                int parentIndex = manager.indexOf(parent);

                if (newState.processorIndices[parentIndex] == -1) {
                    canSchedule = false;
                    break;
                }
            }

            if (canSchedule)
                newState.setFree(childIndex, true);
        }

        // TODO: figure out a way to speed up the DRT calculation
        newState.cost = Math.max(newState.cost, newState.calculateDRT(newState.getFreeNodes()));

        return newState;
    }

    /**
     * Retrieve a list of nodes that are free to be
     * scheduled, i.e. all of their parents have been scheduled.
     *
     * @return a list of nodes that can be scheduled
     */
    public List<Node> getFreeNodes() {
        List<Node> out = new ArrayList<>();
        for (int i = 0; i < processorIndices.length; ++i) {
            if (isFree(i))
                out.add(manager.getNode(i));
        }
        return out;
    }

    /**
     * Retrieve a map of nodes to their scheduled start time.
     *
     * @return a map of node to start time
     */
    public Map<Node, Integer> getStartTimes() {
        Map<Node, Integer> out = new HashMap<>();
        for (int i = 0; i < startTimes.length; ++i) {
            int time = startTimes[i];
            if (time == -1)
                continue;
            out.put(manager.getNode(i), time);
        }
        return out;
    }

    /**
     * Retrieve a map of nodes to the processor they are scheduled on.
     *
     * @return a map of node to processor
     */
    public Map<Node, Processor> getProcessors() {
        Map<Node, Processor> out = new HashMap<>();
        for (int i = 0; i < processorIndices.length; ++i) {
            int index = processorIndices[i];
            if (index == -1)
                continue;
            out.put(manager.getNode(i), manager.getProcessor(index));
        }
        return out;
    }

    /**
     * Retrieve the heuristic cost of the state.
     *
     * @return the cost of the state
     */
    public int getCost() {
        return cost;
    }

    /**
     * Retrieve the length of the state, i.e. the time at which all tasks have finished processing.
     *
     * @return the length of the state
     */
    public int getLength() {
        return length;
    }

    /**
     * Converts the current ByteState to a Schedule object.
     *
     * @return a Schedule representing the ByteState
     */
    public Schedule toSchedule() {
        Map<Node, Processor> processors = new HashMap<>();
        List<Pair<Node, Integer>> nodes = new ArrayList<>();
        for (int i = 0; i < startTimes.length; ++i) {
            if (startTimes[i] == -1)
                continue;
            Node node = manager.getNode(i);
            nodes.add(new Pair<>(node, startTimes[i]));
            processors.put(node, manager.getProcessor(processorIndices[i]));
        }
        nodes.sort(Comparator.comparingInt(Pair::second));
        for (Pair<Node, Integer> pair : nodes) {
            Node node = pair.first();
            int time = pair.second();
            processors.get(node).schedule(node, time);
        }

        return new Schedule(processors, new HashSet<>(), manager.getProcessors(), graph, length, cost);
    }

    /**
     * Helper method used to check if a node is free to be scheduled.
     *
     * @param index the index of the node
     * @return true if the node is free to be scheduled, false otherwise
     */
    private boolean isFree(int index) {
        int byteIndex = index / 8;
        int bitIndex = index % 8;
        return ((free[byteIndex] >> bitIndex) & 1) == 1 &&
                virtualEdges[index] == -1;
    }

    /**
     * Helper method used to mark a node as free/not free to be scheduled.
     *
     * @param index  the index of the node
     * @param isFree true if the node should be marked as free, false otherwise
     */
    private void setFree(int index, boolean isFree) {
        int byteIndex = index / 8;
        int bitIndex = index % 8;
        if (isFree) {
            free[byteIndex] |= 1 << bitIndex;
        } else {
            free[byteIndex] &= ~(1 << bitIndex);
            for (int i = 0; i < virtualEdges.length; i++) {
                if (virtualEdges[i] == index)
                    virtualEdges[i] = -1;
            }
        }
    }

    /**
     * Used to calculate the DRT (data ready time) of a list of free nodes.
     */
    private int calculateDRT(List<Node> freeNodes) {
        int drt = 0;
        for (Node n : freeNodes) {
            drt = Math.max(drt, calculateDRT(n) + graph.getBottomLevel(n));
        }
        return drt;
    }

    /**
     * Used to calculate the DRT (data ready time) of a single free node.
     */
    private int calculateDRT(Node node) {
        int drt = 0;
        for (Processor processor : manager.getProcessors()) {
            int processorDRT = 0;
            int processorIndex = manager.indexOf(processor);

            for (Node parent : graph.getParents(node)) {
                int parentIndex = manager.indexOf(parent);
                int finish = startTimes[parentIndex] + parent.getCost();
                if (processorIndices[parentIndex] != processorIndex)
                    finish += graph.getCost(parent, node);
                processorDRT = Math.max(processorDRT, finish);
            }

            drt = Math.min(drt, processorDRT);
        }
        return drt;
    }

    /**
     * Method used to apply task ordering of free nodes if possible.
     * All free nodes must have at most 1 parent node where the node is scheduled
     * on the same processor (parent nodes do not have to be the same node).
     * If any free nodes have a child node, the child node must be the same for all
     * other free nodes.
     *
     * Sorts based on increasing DRT 0->..., then decreasing out-going edge costs inf->...
     * Ordering is applied if the resulting sorted nodes have decreasing out-going edge
     * costs. Ordering is enforced via virtual edges.
     */
    public void order() {
        if (getFreeNodes().size() < 2) return;

        List<Node> canOrder = new ArrayList<>();
        Map<Node, Integer> outCosts = new HashMap<>();
        int commonParentProc = -1;
        Node commonChild = null;

        // Check if all free nodes have common parent processor and child node (if they have one)
        for (Node node : getFreeNodes()) {
            if (graph.getParents(node).size() > 1) return;
            if (graph.getEdges(node).size() > 1) return;

            // Check parent processor
            List<Node> parents = graph.getParents(node);
            int thisProc = parents.size() == 1 ? processorIndices[manager.indexOf(parents.get(0))] : -1;
            if (commonParentProc == -1 && parents.size() == 1)
                commonParentProc = thisProc;

            if (thisProc != -1 && thisProc != commonParentProc) return;

            // Check child
            List<Edge> children = graph.getEdges(node);
            Node thisChild = children.size() == 1 ? children.get(0).getChild() : null;
            if (commonChild == null && children.size() == 1)
                commonChild = thisChild;

            if (thisChild != null && thisChild != commonChild) return;

            canOrder.add(node);
            outCosts.put(node, thisChild == null ? 0 : graph.getCost(node, thisChild));
        }

        //Sort and build virtual edges
        if (canOrder.containsAll(getFreeNodes())) {
            canOrder.sort((a, b) -> {
                // DRT
                List<Node> aParents = graph.getParents(a);
                Node aParent = aParents.size() == 1 ? aParents.get(0) : null;
                int incomingA = aParent == null ? 0 : startTimes[manager.indexOf(aParent)] + aParent.getCost() + graph.getCost(aParent, a);

                List<Node> bParents = graph.getParents(b);
                Node bParent = bParents.size() == 1 ? bParents.get(0) : null;
                int incomingB = bParent == null ? 0 : startTimes[manager.indexOf(bParent)] + bParent.getCost() + graph.getCost(bParent, b);

                int comparison = Integer.compare(incomingA, incomingB);

                // Decreasing outgoing costs if tied DRT
                if (comparison == 0) {
                    int outgoingA = outCosts.get(a);
                    int outgoingB = outCosts.get(b);

                    return Integer.compare(outgoingB, outgoingA);
                }
                return comparison;
            });

            // Verify if decreasing outgoing
            int max = Integer.MAX_VALUE;
            for (Node node : canOrder) {
                if (outCosts.get(node) <= max)
                    max = outCosts.get(node);
                else
                    return;
            }

//            System.out.println(canOrder);

            // Add virtual edges to ordered 'parents'
            for (int i = 1; i < canOrder.size(); i++) {
                int virtualParent = manager.indexOf(canOrder.get(i - 1));
                int virtualChild = manager.indexOf(canOrder.get(i));

                virtualEdges[virtualChild] = virtualParent;
            }
        }
    }

    @Override
    public int compareTo(ByteState other) {
        return Integer.compare(cost, other.cost);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof ByteState))
            return false;
        ByteState o = (ByteState) other;

        return Arrays.equals(startTimes, o.startTimes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(startTimes);
    }
}
