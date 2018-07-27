package se306.a1.scheduler.data;

import java.util.List;

public interface Graph {

    String getName();

    void addNode(String label, int cost);

    void addEdge(String parent, String child, int cost);

    Node getRootNode();

    List<Edge> getLinks(Node node);

    List<Node> getParents(Node node);

    void build();
}
