package se306.a1.scheduler.algorithm;

import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.data.schedule.Processor;
import se306.a1.scheduler.data.schedule.Schedule;
import se306.a1.scheduler.util.exception.ScheduleException;

public class AStarScheduler extends Scheduler {

    @Override
    protected void createSchedule() throws ScheduleException {

    }

    /**
     * This method computes the cost of scheduling a task on a processor based
     * on its parents and the processor to be scheduled on. The cost returned
     * is the total time after carrying out task.
     *
     * @param node the node we are wanting to schedule
     * @param schedule the current schedule state we are on
     * @param processor the processor to schedule to the task on
     * @return the time after executing the task
     * @throws ScheduleException
     */
    private int getCost(Node node, Schedule schedule, Processor processor) throws ScheduleException {
        int cost = processor.getEarliestStartTime();

        for (Node parent : graph.getParents(node)) {
            if (!schedule.getProcessor(parent).equals(processor)) {
                cost = Math.max(cost, schedule.getStartTime(parent) + parent.getCost() + graph.getCost(parent, node));
            }
        }
        return cost;
    }
}
