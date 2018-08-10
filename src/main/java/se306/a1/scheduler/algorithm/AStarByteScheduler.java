package se306.a1.scheduler.algorithm;

import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.data.schedule.ByteState;
import se306.a1.scheduler.data.schedule.Processor;
import se306.a1.scheduler.data.schedule.Schedule;
import se306.a1.scheduler.manager.ByteStateManager;
import se306.a1.scheduler.util.exception.ScheduleException;

public class AStarByteScheduler extends Scheduler {

    @Override
    protected void createSchedule() throws ScheduleException {
        ByteStateManager stateManager = new ByteStateManager(graph, processors);
        // Adds a new state for each entry node on a processor
        for (Node node : graph.getEntryNodes()) {
            ByteState s = new ByteState(stateManager, graph);
            s.scheduleTask(node, stateManager.getProcessor(0));
            stateManager.queue(s);
        }

        ByteState optimal = null;
        while (true) {
            ByteState top = stateManager.dequeue();
            if (top.getUnscheduled().size() == 0) {
                optimal = top;
                break;
            }
            for (Node node : top.getUnscheduled()) {
                for (Processor processor : stateManager.getProcessors()) {
                    ByteState next = top.scheduleTask(node, processor);
                    if (next == null)
                        break;
                    stateManager.queue(next);
                }
            }
        }
        System.out.println(optimal.getLength());
    }

    /**
     * This method computes the cost of scheduling a task on a processor based
     * on its parents and the processor to be scheduled on. The cost returned
     * is the total time after carrying out task.
     *
     * @param node      the node we are wanting to schedule
     * @param schedule  the current schedule state we are on
     * @param processor the processor to schedule to the task on
     * @return the time after executing the task
     * @throws ScheduleException if there is a scheduling error
     */
    private int getStartTime(Node node, Schedule schedule, Processor processor) throws ScheduleException {
        int cost = processor.getEarliestStartTime();

        for (Node parent : graph.getParents(node)) {
            if (!schedule.getProcessor(parent).equals(processor)) {
                cost = Math.max(cost, schedule.getStartTime(parent) + parent.getCost() + graph.getCost(parent, node));
            }
        }
        return cost;
    }
}
