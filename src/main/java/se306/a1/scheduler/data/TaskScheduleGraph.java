package se306.a1.scheduler.data;

import java.util.HashMap;

/**
 * The class used to represent a task schedule.
 *
 * @author Zhi Qiao
 */
public class TaskScheduleGraph implements TaskGraph {
    private String name;
    private HashMap<String, Node> nodes;

    public TaskScheduleGraph(String name) {
        this.name = name;
        nodes = new HashMap<>();
        nodes.put("root", new TaskNode("root", 0));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Node getRootNode() {
        return nodes.get("root");
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
            nodes.get(parentName).addChild(nodes.get(childName), cost);
            return true;
        } else
            return false;
    }
}
