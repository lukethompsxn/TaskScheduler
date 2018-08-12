package visualisation;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import se306.a1.scheduler.data.graph.Edge;
import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.data.schedule.ByteState;

/**
 * This class is used to manage the visualisation interaction with the algorithm
 */
public class Visualiser {
    se306.a1.scheduler.data.graph.Graph graphData;
    Graph visualisedGraph;
    ByteState currentState;

    private static final String styleSheet =
            "node {" +
                    "   fill-color: black;" +
                    "}" +
                    "node.marked {" +
                    "   fill-color: red;" +
                    "}";

    /**
     * This is the constructor for Visualiser.
     * It takes graph as a parameter as this is required for base in the
     * visualisation and it is not updated throughout the scheduling process.
     *
     * @param graph graph object for the given directed graph
     */
    public Visualiser(se306.a1.scheduler.data.graph.Graph graph) {
        this.graphData = graph;
        drawTree();
    }

    /**
     * This method is used to update the current schedule which is being
     * displayed.
     * This is called from StateManager every X seconds with the current latest
     * schedule at that point in time.
     *
     * @param state the latest explored state from the top of the queue
     */
    public void updateCurrentState(ByteState state) {
        this.currentState = state;
        drawHighlighting();
        drawProcessorMap();
    }

    /**
     * This method is used to draw the initial tree of nodes and edges which
     * make up the graph.
     */
    private void drawTree() {
        visualisedGraph = new SingleGraph("Graph");
        visualisedGraph.setAttribute("ui.stylesheet", styleSheet);
        for (Node node : graphData.getAllNodes()) {
            if (visualisedGraph.getNode(node.getLabel()) != null) {
                continue;
            }
            visualisedGraph.addNode(node.getLabel());

            for (Edge edge : graphData.getEdges(node)) {
                if (visualisedGraph.getNode(edge.getChild().getLabel()) == null) {
                    visualisedGraph.addNode(edge.getChild().getLabel());
                }
                String id = edge.getParent().getLabel() + "-" + edge.getChild().getLabel();
                if (visualisedGraph.getEdge(id) != null) {
                    continue;
                }
                visualisedGraph.addEdge(id, edge.getParent().getLabel(), edge.getChild().getLabel(), true);
            }
        }
    }

    /**
     * This method is called whenever the current schedule is updated to handle
     * the redrawing of the highlighted visualisation.
     */
    private void drawHighlighting() {
        for (Node parent : currentState.getScheduledNodes()) {
            visualisedGraph.getNode(parent.getLabel()).setAttribute("ui.marked", "marked");
            for (Node child : currentState.getScheduledNodes()) {
                if (graphData.containsEdge(parent, child)) {
                    visualisedGraph.getEdge(parent.getLabel() + "-" + child.getLabel()).setAttribute("ui.class", "marked");
                }
            }
        }
        visualisedGraph.display();
    }

    /**
     * This method is called whenever the current schedule is updated to handle
     * the redrawing of the processor map visualisation.
     */
    private void drawProcessorMap() {

    }
}
