package se306.a1.scheduler.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.data.schedule.Processor;
import se306.a1.scheduler.data.schedule.Schedule;
import se306.a1.scheduler.manager.StateManager;
import se306.a1.scheduler.util.exception.ScheduleException;

public class ParallelScheduler extends Scheduler{
	Schedule top;
	StateManager stateManager = new StateManager(graph);

	@Override
	protected void createSchedule() throws ScheduleException {
		// Greedy heuristic as an upper-bound
		int greedy = new BasicScheduler().run(graph, processors, cores, isVisualised).getLength();

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

		top = stateManager.dequeue();

		//create a thread pool to manage the threads computing the runtimes.
		ExecutorService p = Executors.newCachedThreadPool();
		ArrayList<Future<Schedule>> futures = new ArrayList<>();

		while (true) {
			//schedule all the threads.
			for(int i = 0; i < cores; i++) {
				Runnable thread = new Runnable() {

					@Override
					public void run() {
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

			                    try {
									schedule.addScheduledTask(node, newProcessor, getStartTime(node, top, newProcessor));
								} catch (ScheduleException e) {
									e.printStackTrace();
								}

			                    // If schedule length takes longer than greedy, ignore
			                    if (schedule.getLength() > greedy) continue;
			                    stateManager.queue(schedule);
			                }
			            }
			            top = stateManager.dequeue();
					}

				};

				Future<Schedule> future = (Future<Schedule>) p.submit(thread);
				futures.add(future);
			}
			
			//check futures to see if any have returned.
			for(Future<Schedule> f : futures) {
				if(f.isDone()) {
					try {
						Schedule s = (Schedule) f.get();
						
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

}
