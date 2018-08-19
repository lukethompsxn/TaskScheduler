package se306.a1.scheduler.support;

import se306.a1.scheduler.data.graph.Graph;

public class GXLGraph {
    private Graph graph;
    private int numProcessors;
    private int length;
    private int sequentialLength;

    public GXLGraph(Graph graph, int numProcessors, int length, int sequentialLength) {
        this.graph = graph;
        this.numProcessors = numProcessors;
        this.length = length;
        this.sequentialLength = sequentialLength;
    }

    public Graph getGraph() {
        return graph;
    }

    public int getNumProcessors() {
        return numProcessors;
    }

    public int getLength() {
        return length;
    }

    public int getSequentialLength() {
        return sequentialLength;
    }


}
