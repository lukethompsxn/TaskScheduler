package se306.a1.scheduler.data;

import java.util.HashMap;

/**
 * The class used to represent a task schedule.
 *
 * @author Zhi Qiao
 */
public class TaskScheduleGraph implements TaskGraph {
    private String name;
    private Node rootNode = new TaskNode("root", 0);
    private HashMap<String, Node> nodes;

    public TaskScheduleGraph(String name) {
        this.name = name;
        nodes = new HashMap<>();
        nodes.put("root", rootNode);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Node getRootNode() {
        return rootNode;
    }

    @Override
    public boolean addNode(String name, int value) {
        if (nodes.containsKey(name)) return false;
        else {
            nodes.put(name, new TaskNode(name, value));
            return true;
        }
    }

    @Override
    public Node getNode(String name) {
        return nodes.get(name);
    }

    @Override
    public boolean addEdge(String parentName, String childName, int cost) {
        if (nodes.containsKey(parentName) && nodes.containsKey(childName)) {
            nodes.get(parentName).addChild(nodes.get(childName));
            nodes.get(parentName).addLink(cost);
            return true;
        } else
            return false;
    }

    /**
     * Method that needs to be hooked onto the end of building the graph object.
     * Attaches the entry nodes of the task schedule to the root node of the
     * TaskScheduleGraph representation.
     */
    public void build() {
        for (Node node : nodes.values()) {
            if (node.getParents().size() == 0) {
                rootNode.addChild(node);
                rootNode.addLink(0);

                node.addParent(rootNode);
            }
        }
    }
}
