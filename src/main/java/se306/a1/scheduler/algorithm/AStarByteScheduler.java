package se306.a1.scheduler.algorithm;

import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.data.schedule.ByteState;
import se306.a1.scheduler.data.schedule.Processor;
import se306.a1.scheduler.manager.ByteStateManager;
import se306.a1.scheduler.util.exception.ScheduleException;

import java.util.List;

public class AStarByteScheduler extends Scheduler {

    @Override
    protected void createSchedule() throws ScheduleException {
        ByteStateManager stateManager = new ByteStateManager(graph, processors);
        // Adds a new state for each entry node on a processor
        for (Node node : graph.getEntryNodes()) {
            ByteState s = new ByteState(stateManager, graph);
            stateManager.queue(s.scheduleTask(node, stateManager.getProcessor(0)));
        }

        ByteState optimal;
        while (true) {
            ByteState current = stateManager.dequeue();
            List<Node> freeNodes = current.getFreeNodes();
            if (freeNodes.isEmpty()) {
                optimal = current;
                break;
            }
            for (Node node : freeNodes) {
                for (Processor processor : stateManager.getProcessors()) {
                    ByteState next = current.scheduleTask(node, processor);
                    stateManager.queue(next);
                }
            }
        }

        schedule = optimal.toSchedule();
    }
}
