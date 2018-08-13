package se306.a1.scheduler.visualisation;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import se306.a1.scheduler.data.graph.Edge;
import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.data.schedule.ByteState;
import se306.a1.scheduler.manager.ByteStateManager;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Queue;

/**
 * This class is used to manage the visualisation interaction with the algorithm
 */
public class Visualiser {
    private se306.a1.scheduler.data.graph.Graph graphData;
    private Graph visualisedGraph;
    private ByteStateManager manager;
    private ByteState currentState;
    private Map<Node, Integer> nodeLevels;
    private Map<Integer, Integer> levels;
    private Set<String> previousNodes;
    private Set<String> previousEdges;
    private Set<String> currentNodes = new HashSet<>();
    private Set<String> currentEdges = new HashSet<>();

    private JFrame frame;
    private final int WIDTH;
    private final int HEIGHT = 500;

    private Random rand = new Random();
    private Color color = getRandomColor();

    private static final String styleSheet =
                    "node {" +
                    "   fill-color: grey;" +
                    "   size: 20px, 20px;" +
                    "   shape: circle;" +
                    "   stroke-mode: plain;" +
                    "   stroke-color: black;" +
                    "}" +
                    "node.marked {" +
                    "   fill-color: green;" +
                    "}" +
                    "node.seen {" +
                    "   fill-color: orange;" +
                    "}" +
                    "edge {" +
                    "   fill-color: black;" +
                    "   size: 2px;" +
                    "}" +
                    "edge.marked {" +
                    "   fill-color: green;" +
                    "   size: 2px;" +
                    "}" +
                    "edge.seen {" +
                    "   fill-color: orange;" +
                    "   size: 2px;" +
                    "}";

    /**
     * This is the constructor for Visualiser.
     * It takes graph as a parameter as this is required for base in the
     * visualisation and it is not updated throughout the scheduling process.
     *
     * @param graph graph object for the given directed graph
     */
    public Visualiser(ByteStateManager manager, se306.a1.scheduler.data.graph.Graph graph) {
        this.manager = manager;
        this.graphData = graph;
        nodeLevels = new HashMap<>();
        levels = new HashMap<>();

        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        WIDTH = Math.max(200, manager.getProcessors().size() * 50);

        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(WIDTH + 15, HEIGHT);
        frame.setVisible(true);
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
//        frame.add(visualisedGraph.display().addDefaultView(false));
        visualisedGraph.display(false);
    }

    /**
     * This method is called whenever the current schedule is updated to handle
     * the redrawing of the highlighted visualisation.
     */
    private void drawHighlighting() {
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
                visualisedGraph.getNode(s).changeAttribute("ui.class", "seen");
            }
        }

        for (String s : previousEdges) {
            if (!currentEdges.contains(s)) {
                visualisedGraph.getEdge(s).changeAttribute("ui.class", "seen");
            }
        }
    }

    /**
     * This method is called whenever the current schedule is updated to handle
     * the redrawing of the processor map visualisation.
     */
    private void drawProcessorMap() {
        frame.getContentPane().removeAll(); //TODO panel still needs clearing
        frame.add(new ProcessorWindow(manager, currentState, color, WIDTH, HEIGHT));
        frame.setVisible(true);
    }

    /**
     * This method is used to determine the level which is the node is to be
     * placed on in the visualisation.
     * This is done by placing the node one level below its lowest parent.
     */
    private void determineLevels() {
        List<Node> exitNodes = graphData.getExitNodes();

        Queue<Node> queue = new ArrayDeque<>();


        for (Node node : exitNodes) {
            nodeLevels.put(node, 0);
        }

        queue.addAll(exitNodes);

        while (!queue.isEmpty()) {
            Node node = queue.poll();

            for (Node parent : graphData.getParents(node)) {

                if (nodeLevels.containsKey(parent)) {
                    if (nodeLevels.get(parent) < nodeLevels.get(node) + 1) {
                        nodeLevels.put(parent, nodeLevels.get(node) + 1);
                        queue.add(parent);
                    }
                } else {
                    nodeLevels.put(parent, nodeLevels.get(node) + 1);
                    queue.add(parent);
                }

            }
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
        return num / 2;
    }

    /**
     * This method returns a random colour where its RGB values are all between
     * 0.5 and 1, i.e. 'lighter/brighter' colours.
     *
     * @return Color object with randomly initialised RGB values
     */
    private Color getRandomColor() {
        float r = rand.nextFloat() / 2f + 0.5f;
        float g = rand.nextFloat() / 2f + 0.5f;
        float b = rand.nextFloat() / 2f + 0.5f;
        return new Color(r, g, b);
    }
}
