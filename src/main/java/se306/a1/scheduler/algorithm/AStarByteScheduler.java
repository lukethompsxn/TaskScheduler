package se306.a1.scheduler.algorithm;

import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.data.schedule.ByteState;
import se306.a1.scheduler.data.schedule.Processor;
import se306.a1.scheduler.manager.ByteStateManager;
import se306.a1.scheduler.util.exception.ScheduleException;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AStarByteScheduler extends Scheduler {

	@Override
	protected void createSchedule() throws ScheduleException {
		ByteStateManager stateManager = new ByteStateManager(graph, processors, isVisualised);
		// Adds a new state for each entry node on a processor
		for (Node node : graph.getEntryNodes()) {
			ByteState s = new ByteState(stateManager, graph);
			stateManager.queue(s.scheduleTask(node, stateManager.getProcessor(0)));
		}

		ExecutorService executor = Executors.newFixedThreadPool(cores);
		for (int i = 0; i < cores; ++i) {
			executor.execute(() -> runSequential(stateManager));
		}
		executor.shutdown();
		try {
			// wait for all threads to finish
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		schedule = stateManager.getOptimal().toSchedule();
	}

	private void runSequential(ByteStateManager manager) {
		while (!manager.hasOptimal() && !Thread.currentThread().isInterrupted()) {
			ByteState current = manager.dequeue();
			if (current == null)
				continue;
			current.order();
			List<Node> freeNodes = current.getFreeNodes();

			for (Node node : freeNodes) {
				for (Processor processor : manager.getProcessors()) {
					ByteState next = current.scheduleTask(node, processor);
					manager.queue(next);
				}
			}
		}
	}
}
