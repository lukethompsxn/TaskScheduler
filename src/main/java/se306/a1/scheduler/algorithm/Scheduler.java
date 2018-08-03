package se306.a1.scheduler.algorithm;

import java.util.Collection;

import se306.a1.scheduler.data.Graph;
import se306.a1.scheduler.data.Node;
import se306.a1.scheduler.data.Processor;
import se306.a1.scheduler.data.Schedule;
import se306.a1.scheduler.util.ScheduleException;

/**
 * This class represents an abstract scheduler.
 * This contains contracts for implementation specific schedulers and common
 * helper methods.
 *
 * @author Luke Thompson
 */
public abstract class Scheduler {
	protected Schedule schedule;
	protected Graph g;

	/**
	 * Initialises and executes the algorithm.
	 * This method will setup config parameters, manage parsing, then executes
	 * the corresponding algorithm implementation as parameters supplied.
	 *
	 * @param graph         graph containing tasks
	 * @param numProcessors number of processors to be run on
	 * @param numCores      number of cores to be run on (optional)
	 */
	protected abstract Schedule run(Graph graph,
			int numProcessors,
			int numCores);

	/**
	 * This method is given a list of visible tasks and then computes
	 * and schedules the cheapest possible task.
	 */
	protected Node computeCheapest(Collection<Node> nodes) throws ScheduleException {
		Node cheapest = null;
		Processor processor = null;
		int minTime = Integer.MAX_VALUE;

		for (Node node : nodes) {
			if (!schedule.isScheduled(g.getParents(node)))
				continue;

			for (Processor p : schedule.getProcessors()) {
				int time = p.getEarliestStartTime();

				for (Node parent : g.getParents(node)) {
					if (!schedule.getProcessor(parent).equals(p)) {
						time = Math.max(time, g.getCost(parent, node) + schedule.getStartTime(parent) + parent.getCost());
					}
				}

				if (time < minTime) {
					minTime = time;
					processor = p;
					cheapest = node;
				}
			}
		}

		System.out.println("time:\t" + minTime + "\tnode:\t" + cheapest + "\ton " + processor.getName() + " for " + cheapest.getCost());
		schedule.addScheduledTask(cheapest, processor, minTime);
		return cheapest;
	}
}
