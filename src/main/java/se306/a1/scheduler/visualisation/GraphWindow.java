package se306.a1.scheduler.visualisation;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import se306.a1.scheduler.data.graph.Edge;
import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.data.schedule.ByteState;

import java.util.*;

public class GraphWindow {
    private se306.a1.scheduler.data.graph.Graph graphData;
    private Graph visualisedGraph;
    private Map<Node, Integer> nodeLevels;
    private Map<Integer, Integer> levels;
    private Set<String> previousNodes;
    private Set<String> previousEdges;
    private Set<String> currentNodes = new HashSet<>();
    private Set<String> currentEdges = new HashSet<>();

    public GraphWindow(se306.a1.scheduler.data.graph.Graph graphData) {
        this.graphData = graphData;
        nodeLevels = new HashMap<>();
        levels = new HashMap<>();

        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        drawTree();
    }

    private static final String styleSheet =
            "node {" +
                    " fill-color: #839192;" +
                    " size: 20px, 20px;" +
                    " shape: circle;" +
                    " stroke-mode: plain;" +
                    " stroke-color: black;" +
                    "}" +
                    "node.marked {" +
                    "   fill-color: #7FE7FF;" +
                    "}" +
                    "node.seen {" +
                    "   fill-color: #FE2A97;" +
                    "}" +
                    "edge {" +
                    "   fill-color: #839192;" +
                    "   size: 2px;" +
                    "}" +
                    "edge.marked {" +
                    "   fill-color: #7FE7FF;" +
                    "   size: 2px;" +
                    "}" +
                    "edge.seen {" +
                    "   fill-color: #FE2A97;" +
                    "   size: 2px;" +
                    "}" +
                    "graph {" +
                            "fill-color: #2A2E37;" +
            "}";

    /**
     * This method is used to draw the initial tree of nodes and edges which
     * make up the graph.
     */
    private void drawTree() {
        int z = 0;

        visualisedGraph = new MultiGraph("Graph");
        visualisedGraph.setAttribute("ui.stylesheet", styleSheet);

        determineLevels();

        for (Node node : graphData.getAllNodes()) {
            if (visualisedGraph.getNode(node.getLabel()) == null) {
                visualisedGraph.addNode(node.getLabel()).setAttribute("ui.label", node.getLabel());
                visualisedGraph.getNode(node.getLabel()).setAttribute("xyz", getXCoordinate(node), nodeLevels.get(node), z);
            }

            for (Edge edge : graphData.getEdges(node)) {
                if (visualisedGraph.getNode(edge.getChild().getLabel()) == null) {
                    visualisedGraph.addNode(edge.getChild().getLabel()).setAttribute("ui.label", edge.getChild().getLabel());
                    visualisedGraph.getNode(edge.getChild().getLabel()).setAttribute("xyz", getXCoordinate(edge.getChild()), nodeLevels.get(edge.getChild()), z);
                }
                String id = edge.getParent().getLabel() + "-" + edge.getChild().getLabel();
                if (visualisedGraph.getEdge(id) == null) {
                    visualisedGraph.addEdge(id, edge.getParent().getLabel(), edge.getChild().getLabel(), true);
                }
            }
        }
    }

    /**
     * This method is called whenever the current schedule is updated to handle
     * the redrawing of the highlighted visualisation.
     */
    public void drawHighlighting(ByteState currentState) {
        previousNodes = new HashSet<>(currentNodes);
        previousEdges = new HashSet<>(currentEdges);
        currentNodes = new HashSet<>();
        currentEdges = new HashSet<>();

        Set<Node> scheduled = currentState.getStartTimes().keySet();
        for (Node parent : scheduled) {
            visualisedGraph.getNode(parent.getLabel()).setAttribute("ui.class", "marked");
            currentNodes.add(parent.getLabel());
            for (Edge e : graphData.getEdges(parent)) {
                if (scheduled.contains(e.getChild())) {
                    String label = parent.getLabel() + "-" + e.getChild().getLabel();
                    visualisedGraph.getEdge(label).setAttribute("ui.class", "marked");
                    currentEdges.add(label);
                }
            }
        }

        for (String s : previousNodes) {
            if (!currentNodes.contains(s)) {
                visualisedGraph.getNode(s).setAttribute("ui.class", "seen");
            }
        }

        for (String s : previousEdges) {
            if (!currentEdges.contains(s)) {
                visualisedGraph.getEdge(s).setAttribute("ui.class", "seen");
            }
        }
    }

    /**
     * This method is used to return the view panel which contains the graph
     * object.
     *
     * @return fxViewPanel containing graph
     */
    public FxViewPanel getViewPanel() {
        FxViewer fxViewer = new FxViewer(visualisedGraph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        return (FxViewPanel) fxViewer.addDefaultView(false);
    }

    /**
     * This method is used to determine the level which is the node is to be
     * placed on in the visualisation.
     * This is done by placing the node one level below its lowest parent.
     */
    private void determineLevels() {
        List<Node> exitNodes = graphData.getExitNodes();
        int maxLevel = 0;

        Queue<Node> queue = new ArrayDeque<>();

        for (Node node : exitNodes) {
            nodeLevels.put(node, 0);
        }

        queue.addAll(exitNodes);

        while (!queue.isEmpty()) {
            Node node = queue.poll();

            for (Node parent : graphData.getParents(node)) {

                if (nodeLevels.containsKey(parent)) {
                    if (nodeLevels.get(parent) < nodeLevels.get(node) + 2) {
                        nodeLevels.put(parent, nodeLevels.get(node) + 2);
                        queue.add(parent);

                        if (nodeLevels.get(parent) > maxLevel) {
                            maxLevel = nodeLevels.get(parent);
                        }
                    }
                } else {
                    nodeLevels.put(parent, nodeLevels.get(node) + 2);
                    queue.add(parent);

                    if (nodeLevels.get(parent) > maxLevel) {
                        maxLevel = nodeLevels.get(parent);
                    }
                }

            }
        }

        for (Node node : graphData.getEntryNodes()) {
            nodeLevels.put(node, maxLevel);
        }

        for (Integer i : nodeLevels.values()) {
            if (levels.containsKey(i)) {
                levels.put(i, levels.get(i) + 1);
            } else {
                levels.put(i, 1);
            }
        }
    }

    /**
     * This method is used to retrieve the X coordinate for the given node.
     * This is done by using the number of nodes at that level in the display
     * hierarchy to create an even distribution at each level.
     *
     * @param node the node to retrieve the x coordinate for
     * @return the x coordinate for the given node
     */
    private double getXCoordinate(Node node) {
        int level = nodeLevels.get(node);
        int num = levels.get(level);

        if (num % 2 == 0) {
            if (num - 2 == 0) {
                levels.put(level, num - 4);
            } else {
                levels.put(level, num - 2);
            }
        } else {
            levels.put(level, num - 2);
            num = num - 1;
        }
        return (0 - num);
    }
}
