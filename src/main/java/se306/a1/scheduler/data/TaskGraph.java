package se306.a1.scheduler.data;

/**
 * Specifies the interface for all graphs representing a task schedule.
 * Classes implementing this interface should store the complete task schedule
 * and provide the implementation for the defined contracts.
 * @author Zhi Qiao
 */
public interface TaskGraph {

    /**
     * Gets the root node task of the task schedule graph.
     * @return the root node of the task schedule
     */
    public Node getRootNode();

    /**
     * Adds a Node object to the task schedule graph.
     * @return if the node was added successfully
     */
    public boolean addNode(String name, int value);

    /**
     * Gets the Node object represented by its name.
     * @return Node object represented by its string value
     */
    public Node getNode(String name);

    /**
     * Adds an edge between two nodes, both nodes must be in the graph already.
     * @return if the edge was added successfully
     */
    public boolean addEdge(String parentName, String childName, int cost);
}
