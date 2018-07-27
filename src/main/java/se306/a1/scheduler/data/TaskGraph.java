package se306.a1.scheduler.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskGraph implements Graph {
    private final Map<Node, List<Edge>> children;
    private final Map<Node, List<Node>> parents;
    private final Map<String, Node> nodes;
    private final Node root;
    private final String name;

    /**
     * Creates a TaskGraph with a given name.
     *
     * @param name the name of the TaskGraph
     */
    public TaskGraph(String name) {
        children = new HashMap<>();
        parents = new HashMap<>();
        nodes = new HashMap<>();
        root = new Node("root", 0);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addNode(String label, int cost) {
        Node node = new Node(label, cost);
        nodes.put(label, node);
        children.put(node, new ArrayList<>());
        parents.put(node, new ArrayList<>());
    }

    @Override
    public void addEdge(String parent, String child, int cost) {
        Node parentNode = nodes.get(parent);
        Node childNode = nodes.get(child);
        if (parentNode == null || childNode == null) return;
        children.get(parentNode).add(new Edge(parentNode, childNode, cost));
        parents.get(childNode).add(parentNode);
    }

    @Override
    public Node getRootNode() {
        return root;
    }

    @Override
    public List<Edge> getLinks(Node node) {
        return children.get(node);
    }

    @Override
    public List<Node> getParents(Node node) {
        return parents.get(node);
    }

    @Override
    public void build() {
        children.put(root, new ArrayList<>());
        parents.put(root, new ArrayList<>());
        for (Node n : nodes.values()) {
            if (parents.get(n).isEmpty()) {
                children.get(root).add(new Edge(root, n, 0));
                parents.get(n).add(root);
            }
        }
        nodes.put(root.getLabel(), root);
    }
}
