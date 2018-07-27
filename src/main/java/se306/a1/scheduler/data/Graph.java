package se306.a1.scheduler.data;

import java.util.List;

/**
 * Specifies the interface for all graphs representing a task schedule.
 * Classes implementing this interface should store the complete task schedule
 * and provide the implementation for the defined contracts.
 *
 * @author Zhi Qiao, Abhinav Behal
 */
public interface Graph {

    /**
     * Gets the name of the task graph.
     *
     * @return the name of the graph
     */
    String getName();

    /**
     * Adds a Node object to the task schedule graph.
     *
     * @param label the label of the node
     * @param cost  the cost of the node
     */
    void addNode(String label, int cost);

    /**
     * Adds an edge between two nodes, both nodes must be in the graph already.
     *
     * @param parent the label of the parent node
     * @param child  the label of child node
     * @param cost   the cost of edge
     */
    void addEdge(String parent, String child, int cost);

    /**
     * Gets the root node task of the task schedule graph.
     *
     * @return the root node of the task schedule
     */
    Node getRootNode();

    /**
     * Gets the outgoing edges from the given node.
     *
     * @param node the node to get the outgoing edges from
     * @return a list of the outgoing edges from the node
     */
    List<Edge> getLinks(Node node);

    /**
     * Gets the parent nodes of the given node.
     *
     * @param node the node for which to get the parents of
     * @return a list of the parent nodes
     */
    List<Node> getParents(Node node);

    /**
     * Method that should be hooked onto the end of building a task graph.
     * (Necessity TBD)
     */
    void build();
}
