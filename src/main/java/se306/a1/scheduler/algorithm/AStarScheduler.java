package se306.a1.scheduler.algorithm;

import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.data.schedule.Processor;
import se306.a1.scheduler.data.schedule.Schedule;
import se306.a1.scheduler.manager.StateManager;
import se306.a1.scheduler.util.exception.ScheduleException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class AStarScheduler extends Scheduler {

    @Override
    protected void createSchedule() throws ScheduleException {
        StateManager stateManager = new StateManager(graph);

        BasicScheduler basicScheduler = new BasicScheduler();

        // Greedy heuristic as an upper-bound
        int greedy = basicScheduler.run(graph, processors, cores).getLength();

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

        Schedule top = stateManager.dequeue();

        // While the best schedule still has something to schedule
        while (top.getUnscheduledTasks().size() != 0) {
            List<Processor> topProcessors = top.getProcessors();

            // For all unscheduled nodes
            for (Node node : top.getUnscheduledTasks()) {
                if (!top.isScheduled(graph.getParents(node))) continue;

                // If parents are scheduled then schedule node on every processor
                for (Processor processor : topProcessors) {
                    List<Processor> processors = new ArrayList<>(topProcessors);
                    Processor newProcessor = new Processor(processor);

                    processors.remove(processor);
                    processors.add(newProcessor);

                    Schedule schedule = new Schedule(
                            new HashMap<>(top.getScheduledTasks()),
                            new HashSet<>(top.getUnscheduledTasks()),
                            processors,
                            graph,
                            top.getLength(),
                            top.getCost()
                    );

                    schedule.addScheduledTask(node, newProcessor, getStartTime(node, top, newProcessor));

                    // If schedule length takes longer than greedy, ignore
                    if (schedule.getLength() > greedy) continue;
                    stateManager.queue(schedule);
                }
            }
            top = stateManager.dequeue();
        }

        // Optimal schedule (theoretically)
        schedule = top;
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
