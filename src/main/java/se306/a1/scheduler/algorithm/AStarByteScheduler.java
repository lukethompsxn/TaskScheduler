package se306.a1.scheduler.algorithm;

import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.data.schedule.ByteState;
import se306.a1.scheduler.data.schedule.Processor;
import se306.a1.scheduler.data.schedule.Schedule;
import se306.a1.scheduler.manager.ByteStateManager;
import se306.a1.scheduler.util.exception.ScheduleException;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This is the A* implementation of the scheduler. It produces an optimal schedule
 * and minimises the seen states using a cost function and pruning.
 */
public class AStarByteScheduler extends Scheduler {

    @Override
    protected Schedule createSchedule() throws ScheduleException {
        ByteStateManager stateManager = new ByteStateManager(graph, processors, isVisualised, cores);
        // Adds a new state for each entry node on a processor
        for (Node node : graph.getEntryNodes()) {
            ByteState s = new ByteState(stateManager, graph);
            stateManager.queue(s.scheduleTask(node, stateManager.getProcessor(0), graph, stateManager));
        }

        ExecutorService executor = Executors.newFixedThreadPool(cores);
        for (int i = 0; i < cores; ++i) {
            executor.execute(() -> runSequential(stateManager));
        }
        executor.shutdown();
        try {
            // Wait for all threads to finish
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return stateManager.getOptimal().toSchedule(graph, stateManager);
    }

    /**
     * This method is the actual A* algorithm which uses the ByteStateManager to
     * manage the different generated schedules.
     *
     * @param manager ByteStateManager which manages and orders the queue of states
     */
    private void runSequential(ByteStateManager manager) {
        while (!manager.hasComplete() && !Thread.currentThread().isInterrupted()) {
            ByteState current = manager.dequeue();
            if (current == null)
                continue;

            // Applies task ordering and get free nodes
            current.order(graph, manager);
            List<Node> freeNodes = current.getFreeNodes(manager);

            // For all free nodes, put them on each processor and add to queue
            for (Node node : freeNodes) {
                for (Processor processor : manager.getProcessors()) {
                    ByteState next = current.scheduleTask(node, processor, graph, manager);
                    manager.queue(next);
                }
            }
        }
    }
}
