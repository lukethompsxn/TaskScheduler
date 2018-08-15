package se306.a1.scheduler.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.data.schedule.ByteState;
import se306.a1.scheduler.data.schedule.Processor;
import se306.a1.scheduler.data.schedule.Schedule;
import se306.a1.scheduler.manager.ByteStateManager;
import se306.a1.scheduler.manager.StateManager;
import se306.a1.scheduler.util.exception.ScheduleException;

public class ParallelScheduler extends Scheduler{
	Schedule top;
	ByteStateManager stateManager;
	volatile boolean isDone = false;

	public ParallelScheduler() throws ScheduleException {
		
	}

	@Override
	protected void createSchedule() throws ScheduleException {
		stateManager = new ByteStateManager(graph, processors, isVisualised);
		
		// Adds a new state for each entry node on a processor
		for (Node node : graph.getEntryNodes()) {
			ByteState s = new ByteState(stateManager, graph);
			stateManager.queue(s.scheduleTask(node, stateManager.getProcessor(0)));
		}

		//create a thread pool to manage the threads computing the runtimes.
		ExecutorService p = Executors.newCachedThreadPool();
		ArrayList<Future<List<ByteState>>> futures = new ArrayList<>();

		ExecutorService threadPool = Executors.newFixedThreadPool(cores);

		for(int i = 0; i < cores; i++) {
			Callable<List<ByteState>> r = new StateThread(stateManager);

			Future<List<ByteState>> future = (Future<List<ByteState>>) p.submit(r);
			futures.add(future);

		}

		List<ByteState> endStates = new ArrayList<>();
		
		while (true) {
			//check futures to see if any have returned.
			for(int i = 0; i < futures.size(); i++) {
				Future<List<ByteState>> f = futures.get(i);
				if(f.isDone()) {
					try {
						List<ByteState> s = (List<ByteState>) f.get();

						if(isDone) {
							threadPool.shutdown();
							endStates.addAll(s);
							if(endStates.size() == cores) {
								int min = Integer.MAX_VALUE;
								ByteState minState = null;
								
								for(ByteState b : endStates) {
									if(b.getCost() < min) {
										min = b.getCost();
										minState = b;
									}
								}
								
								schedule = minState.toSchedule();
							}
						} else {
							//re-add new schedules to the state manager.
							for(ByteState b : s) {
								stateManager.queue(b);
							}
							
							//start the new thread up
							futures.add(threadPool.submit(new StateThread(stateManager)));
						}

						futures.remove(f);
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
	
	//callable to run on seperate threads.
	private class StateThread implements Callable<List<ByteState>> {
		private ByteStateManager stateManager;
		
		public StateThread(ByteStateManager manager) {
			this.stateManager = manager;
		}
		
		
		@Override
		public List<ByteState> call() throws Exception {
			List<ByteState> output = new ArrayList<>();

			ByteState current = stateManager.dequeue();
			
			//if the priority queue is empty this thread should immediately terminate
			if(current == null) {
				return output;
			}
			
			current.order();
			List<Node> freeNodes = current.getFreeNodes();

			ByteState optimal;

			if (freeNodes.isEmpty()) {
				optimal = current;
				isDone = true;
				output.add(optimal);
				return output;
			}

			for (Node node : freeNodes) {
				for (Processor processor : stateManager.getProcessors()) {
					ByteState next = current.scheduleTask(node, processor);
					output.add(next);
				}
			}

			return output;
		}					
	}
}
