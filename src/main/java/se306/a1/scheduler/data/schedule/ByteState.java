package se306.a1.scheduler.data.schedule;

import se306.a1.scheduler.data.graph.Edge;
import se306.a1.scheduler.data.graph.Graph;
import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.manager.ByteStateManager;
import se306.a1.scheduler.util.Pair;

import java.lang.reflect.Array;
import java.util.*;

public class ByteState implements Comparable<ByteState> {
    private final ByteStateManager manager;
    private final Graph graph;
    private final int[] startTimes;
    private final int[] processorIndices;
    private final int[] earliestStartTimes;
    private final boolean[] free;
    private int cost;
    private int length;

    public ByteState(ByteStateManager manager, Graph graph) {
        this.manager = manager;
        this.graph = graph;

        int numNodes = graph.getAllNodes().size();
        int numProcessors = manager.getProcessors().size();

        this.earliestStartTimes = new int[numProcessors];
        this.processorIndices = new int[numNodes];
        this.startTimes = new int[numNodes];
        this.free = new boolean[numNodes];
        for (Node n : graph.getEntryNodes()) {
            free[manager.indexOf(n)] = true;
        }

        Arrays.fill(this.startTimes, -1);
        Arrays.fill(this.processorIndices, -1);
    }

    // Does a deep copy of the passed in ByteState, except for the graph and manager objects
    // which are shallow copied
    private ByteState(ByteState other) {
        startTimes = Arrays.copyOf(other.startTimes, other.startTimes.length);
        earliestStartTimes = Arrays.copyOf(other.earliestStartTimes, other.earliestStartTimes.length);
        processorIndices = Arrays.copyOf(other.processorIndices, other.processorIndices.length);
        free = Arrays.copyOf(other.free, other.free.length);

        manager = other.manager;
        graph = other.graph;
        cost = other.cost;
        length = other.length;
    }

    public ByteState scheduleTask(Node node, Processor processor) {
        int nodeIndex = manager.indexOf(node);
        int processorIndex = manager.indexOf(processor);

        int startTime = earliestStartTimes[processorIndex];

        if (processorIndices[nodeIndex] != -1)
            throw new RuntimeException("Task has already been scheduled");
        if (!free[nodeIndex])
            throw new RuntimeException("Task is not free to schedule");

        for (Node parent : graph.getParents(node)) {
            int parentIndex = manager.indexOf(parent);
            if (processorIndices[parentIndex] == -1)
                return null;
            if (processorIndices[parentIndex] != processorIndex) {
                int cost = graph.getCost(parent, node);
                if (cost == -1)
                    continue;
                startTime = Math.max(startTime, startTimes[parentIndex] + parent.getCost() + cost);
            }
        }

        ByteState newState = new ByteState(this);

        newState.startTimes[nodeIndex] = startTime;
        newState.processorIndices[nodeIndex] = processorIndex;
        newState.earliestStartTimes[processorIndex] = startTime + node.getCost();
        newState.length = Math.max(newState.length, startTime + node.getCost());
        newState.cost = Math.max(newState.cost, startTime + graph.getBottomLevel(node));
        newState.free[nodeIndex] = false;

        for (Edge e : graph.getEdges(node)) {
            Node child = e.getChild();
            int childIndex = manager.indexOf(child);

            for (Node parent : graph.getParents(child)) {
                int parentIndex = manager.indexOf(parent);
                if (processorIndices[parentIndex] == -1)
                    break;
            }

            newState.free[childIndex] = true;
        }

        return newState;
    }

    public int getCost() {
        return cost;
    }

    public int getLength() {
        return length;
    }

    public List<Node> getFreeNodes() {
        List<Node> out = new ArrayList<>();
        for (int i = 0; i < free.length; ++i) {
            if (free[i])
                out.add(manager.getNode(i));
        }
        return out;
    }

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

        // TODO check that this actually leads to pruning of equivalent states
        return Arrays.equals(startTimes, o.startTimes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(startTimes);
    }
}
