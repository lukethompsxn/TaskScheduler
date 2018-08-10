package se306.a1.scheduler.algorithm;

import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.data.schedule.Processor;
import se306.a1.scheduler.data.schedule.Schedule;
import se306.a1.scheduler.manager.StateManager;
import se306.a1.scheduler.util.exception.ScheduleException;

import java.util.*;

public class AStarScheduler extends Scheduler {
    StateManager stateManager = new StateManager(graph);

    @Override
    protected void createSchedule() throws ScheduleException {
        // Adds a new state for each entry node on a processor
        for (Node node : graph.getEntryNodes()) {
            Schedule s = new Schedule(
                    new HashMap<>(),
                    new HashSet<>(graph.getEntryNodes()),
                    processors,
                    graph,
                    0,
                    0);

            s.addScheduledTask(node, 0);
            stateManager.queue(s);
        }

        //
        while (!stateManager.isDone()) {
            Schedule top = stateManager.dequeue();
            List<Processor> topProcessors = top.getProcessors();

            for (Node node : top.getUnscheduledTasks()) {
                for (Processor processor : topProcessors) {
                    List<Processor> processors = new ArrayList<>(topProcessors);
                    Processor newProcessor = new Processor(processor);

                    processors.remove(processor);
                    processors.add(newProcessor);

                    Schedule schedule = new Schedule(
                            top.getScheduledTasks(),
                            top.getUnscheduledTasks(),
                            processors,
                            graph,
                            top.getLength(),
                            top.getCost()
                    );

                    // Need to fix
                    // schedule.addScheduledTask(node, newProcessor, getStartTime(node, top, newProcessor));

                    stateManager.queue(schedule);
                }
            }
        }

        schedule = stateManager.getOptimalSchedule();
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
