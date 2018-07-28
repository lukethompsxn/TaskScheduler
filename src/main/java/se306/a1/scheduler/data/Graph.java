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
}
