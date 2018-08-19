package se306.a1.scheduler.support;

import se306.a1.scheduler.data.graph.Graph;

/**
 * This class is used to store the GXL graph data which is required to run tests.
 */
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

    /**
     * Gets the graph object
     *
     * @return graph object
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Gets the number of processors
     *
     * @return int representing number of processors
     */
    public int getNumProcessors() {
        return numProcessors;
    }

    /**
     * Gets the length of the optimal schedule
     *
     * @return int representing optimal length
     */
    public int getLength() {
        return length;
    }

    /**
     * Gets the length of the optimal sequential schedule
     *
     * @return int representing optimal sequential schedule length
     */
    public int getSequentialLength() {
        return sequentialLength;
    }


}
