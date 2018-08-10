package se306.a1.scheduler.data.schedule;

import javafx.util.Pair;
import se306.a1.scheduler.data.graph.Graph;
import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.manager.ByteStateManager;

import java.util.*;

public class ByteState implements Comparable<ByteState> {
    private final ByteStateManager manager;
    private final Graph graph;
    private boolean[] scheduled;
    private int[][] startTimes;
    private int[] earliestStartTimes;
    private int cost;
    private int length;

    public ByteState(ByteStateManager manager, Graph graph) {
        this.manager = manager;
        this.graph = graph;

        int numNodes = graph.getAllNodes().size();
        int numProcessors = manager.getProcessors().size();

        this.scheduled = new boolean[numNodes];
        this.earliestStartTimes = new int[numProcessors];
        this.startTimes = new int[numProcessors][numNodes];

        for (int[] row : startTimes) {
            Arrays.fill(row, -1);
        }
    }

    // Does a deep copy of the passed in ByteState, except for the graph and manager objects
    // which are shallow copied
    private ByteState(ByteState other) {
        startTimes = new int[other.startTimes.length][other.startTimes[0].length];
        for (int i = 0; i < other.startTimes.length; ++i) {
            startTimes[i] = Arrays.copyOf(other.startTimes[i], other.startTimes[i].length);
        }

        earliestStartTimes = Arrays.copyOf(other.earliestStartTimes, other.earliestStartTimes.length);
        scheduled = Arrays.copyOf(other.scheduled, other.scheduled.length);

        manager = other.manager;
        graph = other.graph;
        cost = other.cost;
        length = other.length;
    }

    public ByteState scheduleTask(Node node, Processor processor) {
        int nodeIndex = manager.indexOf(node);
        int processorIndex = manager.indexOf(processor);

        int startTime = earliestStartTimes[processorIndex];

        if (scheduled[nodeIndex])
            throw new RuntimeException("Task has already been scheduled");

        for (Node parent : graph.getParents(node)) {
            int parentIndex = manager.indexOf(parent);
            if (!scheduled[parentIndex])
                return null;
            if (getProcessor(parentIndex) != processorIndex) {
                startTime = Math.max(startTime, getStartTime(parentIndex) + parent.getCost() + graph.getCost(parent, node));
            }
        }

        ByteState newState = new ByteState(this);

        newState.startTimes[processorIndex][nodeIndex] = startTime;
        newState.earliestStartTimes[processorIndex] = startTime + node.getCost();
        newState.cost = Math.max(newState.cost, startTime + graph.getBottomLevel(node));
        newState.length = Math.max(newState.length, startTime + node.getCost());
        newState.scheduled[nodeIndex] = true;

        return newState;
    }

    public List<Node> getUnscheduled() {
        List<Node> out = new ArrayList<>();
        for (int i = 0; i < scheduled.length; ++i) {
            if (!scheduled[i])
                out.add(manager.getNode(i));
        }
        return out;
    }

    public List<Node> getScheduled() {
        List<Node> out = new ArrayList<>();
        for (int i = 0; i < scheduled.length; ++i) {
            if (scheduled[i])
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

    private int getProcessor(int nodeIndex) {
        for (int i = 0; i < startTimes.length; ++i) {
            if (startTimes[i][nodeIndex] != -1)
                return i;
        }
        return -1;
    }

    private int getStartTime(int nodeIndex) {
        for (int i = 0; i < startTimes.length; ++i) {
            if (startTimes[i][nodeIndex] != -1)
                return startTimes[i][nodeIndex];
        }
        return -1;
    }

    public Schedule toSchedule() {
        Map<Node, Processor> processors = new HashMap<>();
        List<Pair<Node, Integer>> nodes = new ArrayList<>();
        for (int i = 0; i < startTimes.length; ++i) {
            for (int j = 0; j < startTimes[i].length; ++j) {
                if (startTimes[i][j] == -1)
                    continue;
                Node node = manager.getNode(j);
                nodes.add(new Pair<>(node, startTimes[i][j]));
                processors.put(node, manager.getProcessor(i));
            }
        }
        nodes.sort(Comparator.comparingInt(Pair::getValue));
        for (Pair<Node, Integer> pair : nodes) {
            Node node = pair.getKey();
            int time = pair.getValue();
            processors.get(node).schedule(node, time);
        }

        return new Schedule(processors, new HashSet<>(), manager.getProcessors(), graph, length, cost);
    }
}
