package se306.a1.scheduler.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a Directed Acyclic Graph where nodes are tasks and edges
 * represent dependencies.
 *
 * @author Abhinav Behal, Zhi Qiao
 */
public class TaskGraph implements Graph {
    private final Map<Node, List<Edge>> children;
    private final Map<Node, List<Node>> parents;
    private final Map<String, Node> nodes;
    private final String name;

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
        this.nodes = nodes;
        this.name = name;

        build(edges);
    }

    @Override
    public String getName() {
        return name;
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
    public Integer getCost(Node parent, Node child) {
        for (Edge e : children.get(parent)) {
            if (e.getChild().equals(child))
                return e.getCost();
        }
        return null;
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
        }
    }
}
