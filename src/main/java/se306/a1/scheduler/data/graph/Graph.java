package se306.a1.scheduler.data.graph;

import java.util.Collection;
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
     * Gets the name of the graph.
     *
     * @return the name of the graph
     */
    String getName();

    /**
     * Gets the nodes in the graph.
     *
     * @return a collection of all the nodes in the graph
     */
    Collection<Node> getAllNodes();

    /**
     * Gets the outgoing edges from the given node.
     *
     * @param node the node to get the outgoing edges from
     * @return a list of the outgoing edges from the node
     */
    List<Edge> getEdges(Node node);

    /**
     * Gets the parent nodes of the given node.
     *
     * @param node the node for which to get the parents of
     * @return a list of the parent nodes
     */
    List<Node> getParents(Node node);

    /**
     * Gets the entry nodes of the graph
     *
     * @return a list of entry nodes into the graph
     */
    List<Node> getEntryNodes();

    /**
     * Gets the cost of the link between the two given nodes.
     *
     * @param parent the parent node in the link
     * @param child  the child node in the link
     * @return the cost of link
     */
    Integer getCost(Node parent, Node child);

    /**
     * Determines whether the graph contains an edge between a given
     * parent and child.
     *
     * @param parent parent node in the link
     * @param child child node in the link
     * @return
     */
    boolean containsEdge(Node parent, Node child);

    /**
     * Gets the bottom level of the given node, which is the cost of the
     * longest path from the node to an exit node.
     *
     * @param node the node to get the bottom level of
     * @return the bottom level of the node
     */
    Integer getBottomLevel(Node node);

    List<Node> getExitNodes();
}
