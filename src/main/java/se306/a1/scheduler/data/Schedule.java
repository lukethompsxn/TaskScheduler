package se306.a1.scheduler.data;

import javafx.util.Pair;
import se306.a1.scheduler.util.ScheduleException;

import java.util.HashMap;
import java.util.Map;

/**
 * The purpose of this class is to store the scheduleling information for each
 * node, i.e. the startTime of the task and the processor which the task is
 * allocated to.
 * @author Luke Thompson
 */
public class Schedule {
    private static Map<Node, Pair> nodeData = new HashMap<>();

    /**
     * This method create a new node (task) to pair (startTime & processor) entry
     * in the map.
     * @param node the task which the startTime & processor are for
     * @param startTime the start time of the node (task)
     * @param processor the processor which the node (task) is carried out on
     */
    public static void addNodeData(Node node, int startTime, int processor) {
        nodeData.put(node, new Pair(startTime, processor));
    }

    /**
     * This method returns the start time of the node (task) which is passed in.
     * @param node the node (task) that the start time is wanted for
     * @return the start time of the node (task) on a processor
     */
    public static int getStartTime(Node node) throws ScheduleException {
        if (nodeData.get(node) != null && nodeData.get(node).getKey() != null) {
            return (int) nodeData.get(node).getKey();
        }
        return -1;

        //This is the actual code, requires schedule to be produced though
//        if (nodeData.get(node) == null || nodeData.get(node).getKey() == null) {
//            throw new ScheduleException("Start time not defined for node: " + node);
//        }
//        return (int) nodeData.get(node).getKey();
    }

        /**
         * This method returns the processor which the node (task) is allocated to.
         * @param node the node (task) that the processor is wanted for.
         * @return the processor which the node (task) is allocated to
         */
    public static int getProcessor(Node node) throws ScheduleException {
        if (nodeData.get(node) != null && nodeData.get(node).getValue() != null) {
            return (int) nodeData.get(node).getValue();
        }
        return -1;

        //This is the actual code, requires schedule to be produced though
//        if (nodeData.get(node) == null || nodeData.get(node).getValue() == null) {
//            throw new ScheduleException("Processor not defined for task: " + node);
//        }
//        return (int) nodeData.get(node).getValue();
    }
}
