package se306.a1.scheduler.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se306.a1.scheduler.algorithm.BasicScheduler;
import se306.a1.scheduler.data.schedule.Schedule;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * This class is used to maintain the state of schedules and to supply schedules
 * to the scheduler based on heuristics.
 */
public class StateManager {
    private Queue<Schedule> schedules = new PriorityQueue<>();

    private static Logger logger = LogManager.getLogger(StateManager.class.getSimpleName());

    /**
     * This method add a new schedule to the priority queue.
     * Queuing order in the queue is based on the heuristics which are
     * implemented in the overridden comparable method for schedule.
     *
     * @param schedule a schedule instance to add to the queue
     */
    public void queue(Schedule schedule) {
        //Comparable needs overwriting in schedule based on heuristic
        schedules.add(schedule);
        logger.info("Schedule Queued. Queue Length = " + schedules.size());
    }

    /**
     * This method returns the best schedule at the current point in execution.
     * The schedule which is returned (the front of the queue) is determined
     * by heuristics which are implemented in the comparable method of schedule.
     *
     * @return the best schedule at current point in time
     */
    public Schedule dequeue() {
        logger.info("Best Schedule Retrieved");
        return schedules.poll();
    }
}
