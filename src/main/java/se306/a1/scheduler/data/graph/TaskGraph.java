package se306.a1.scheduler.data.graph;

import java.util.*;

/**
 * Represents a Directed Acyclic Graph where nodes are tasks and edges
 * represent dependencies.
 *
 * @author Abhinav Behal, Zhi Qiao
 */
public class TaskGraph implements Graph {
    private final String name;
    private final Map<String, Node> nodes;
    private final Map<Node, List<Edge>> children;
    private final Map<Node, List<Node>> parents;
    private final Map<Node, Integer> bottomLevels;
    private final Map<Node, Map<Node, Integer>> edgeCosts;

    /**
     * Creates a TaskGraph with a given name, nodes, and associated edges.
     *
     * @param name  the name of the TaskGraph
     * @param nodes the list of nodes in the TaskGraph
     * @param edges the list of edges connecting nodes in the TaskGraph
     */
    public TaskGraph(String name, Map<String, Node> nodes, List<Edge> edges) {
        children = new HashMap<>();
        parents = new HashMap<>();
        bottomLevels = new HashMap<>();
        edgeCosts = new HashMap<>();
        this.nodes = nodes;
        this.name = name;

        build(edges);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<Node> getAllNodes() {
        return nodes.values();
    }

    @Override
    public List<Edge> getEdges(Node node) {
        return children.get(node);
    }

    @Override
    public List<Node> getParents(Node node) {
        return parents.get(node);
    }

    @Override
    public List<Node> getEntryNodes() {
        List<Node> entries = new ArrayList<>();

        for (Node n : nodes.values()) {
            if (!parents.containsKey(n) || parents.get(n).isEmpty())
                entries.add(n);
        }
        return entries;
    }

    @Override
    public List<Node> getExitNodes() {
        List<Node> exits = new ArrayList<>();

        for (Node n : nodes.values()) {
            if (!children.containsKey(n) || children.get(n).isEmpty())
                exits.add(n);
        }

        return exits;
    }

    @Override
    public Integer getCost(Node parent, Node child) {
        return edgeCosts.get(parent).get(child);
    }

    @Override
    public Integer getBottomLevel(Node node) {
        return bottomLevels.get(node);
    }

    @Override
    public Integer getEdgeCount() {
        List<Edge> edges = new ArrayList<>();
        for (Node n : nodes.values()) {
            edges.addAll(children.get(n));
        }
        return edges.size();
    }

    /**
     * Private helper method used to construct the edges connecting the graph.
     *
     * @param edges edges connecting nodes in the TaskGraph
     */
    private void build(List<Edge> edges) {
        for (Node n : nodes.values()) {
            parents.put(n, new ArrayList<>());
            children.put(n, new ArrayList<>());
        }

        for (Edge e : edges) {
            Node parentNode = nodes.containsValue(e.getParent()) ? e.getParent() : null;
            Node childNode = nodes.containsValue(e.getChild()) ? e.getChild() : null;

            if (parentNode == null || childNode == null) return;

            children.get(parentNode).add(e);
            parents.get(childNode).add(parentNode);

            if (!edgeCosts.containsKey(parentNode))
                edgeCosts.put(parentNode, new HashMap<>());

            edgeCosts.get(parentNode).put(childNode, e.getCost());
        }

        calculateBottomLevels();
    }

    /**
     * Private helper method used to calculate the bottom levels for all nodes in the graph.
     */
    private void calculateBottomLevels() {
        Queue<Node> queue = new ArrayDeque<>();

        // Add all the exit nodes
        for (Node n : nodes.values()) {
            if (!children.containsKey(n) || children.get(n).isEmpty()) {
                queue.add(n);
                bottomLevels.put(n, n.getCost());
            }
        }

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            int cost = bottomLevels.get(node);

            for (Node n : parents.get(node)) {
                int newCost = n.getCost() + cost;
                int currentCost = 0;
                if (bottomLevels.containsKey(n))
                    currentCost = bottomLevels.get(n);

                if (newCost > currentCost) {
                    bottomLevels.put(n, newCost);
                    queue.add(n);
                }
            }
        }
    }
}
