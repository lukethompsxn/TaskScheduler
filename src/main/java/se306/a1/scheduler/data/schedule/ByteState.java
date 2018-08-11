package se306.a1.scheduler.data.schedule;

import se306.a1.scheduler.data.graph.Graph;
import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.manager.ByteStateManager;
import se306.a1.scheduler.util.Pair;

import java.util.*;

public class ByteState implements Comparable<ByteState> {
    private final ByteStateManager manager;
    private final Graph graph;
    private int[] startTimes;
    private int[] processorIndices;
    private int[] earliestStartTimes;
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

        Arrays.fill(this.startTimes, -1);
        Arrays.fill(this.processorIndices, -1);
    }

    // Does a deep copy of the passed in ByteState, except for the graph and manager objects
    // which are shallow copied
    private ByteState(ByteState other) {
        startTimes = Arrays.copyOf(other.startTimes, other.startTimes.length);
        earliestStartTimes = Arrays.copyOf(other.earliestStartTimes, other.earliestStartTimes.length);
        processorIndices = Arrays.copyOf(other.processorIndices, other.processorIndices.length);

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

        for (Node parent : graph.getParents(node)) {
            int parentIndex = manager.indexOf(parent);
            if (processorIndices[parentIndex] == -1)
                return null;
            if (processorIndices[parentIndex] != processorIndex) {
                startTime = Math.max(startTime, startTimes[parentIndex] + parent.getCost() + graph.getCost(parent, node));
            }
        }

        ByteState newState = new ByteState(this);

        newState.startTimes[nodeIndex] = startTime;
        newState.processorIndices[nodeIndex] = processorIndex;
        newState.earliestStartTimes[processorIndex] = startTime + node.getCost();
        newState.cost = Math.max(newState.cost, startTime + graph.getBottomLevel(node));
        newState.length = Math.max(newState.length, startTime + node.getCost());

        return newState;
    }

    public List<Node> getUnscheduled() {
        List<Node> out = new ArrayList<>();
        for (int i = 0; i < processorIndices.length; ++i) {
            if (processorIndices[i] == -1)
                out.add(manager.getNode(i));
        }
        return out;
    }

    public List<Node> getScheduled() {
        List<Node> out = new ArrayList<>();
        for (int i = 0; i < processorIndices.length; ++i) {
            if (processorIndices[i] != -1)
                out.add(manager.getNode(i));
        }
        return out;
    }

    public int getCost() {
        return cost;
    }

    public int getLength() {
        return length;
    }

    @Override
    public int compareTo(ByteState other) {
        return Integer.compare(cost, other.cost);
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
}
