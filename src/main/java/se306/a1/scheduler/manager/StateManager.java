package se306.a1.scheduler.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se306.a1.scheduler.data.graph.Graph;
import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.data.schedule.Schedule;
import se306.a1.scheduler.data.schedule.State;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * This class is used to maintain the state of states and to supply states
 * to the scheduler based on heuristics.
 */
public class StateManager {
    private Node[] nodeLookUp;
    private Queue<State> states = new PriorityQueue<>();

    private boolean done = false;
    private Schedule optimalSchedule;

    private static Logger logger = LogManager.getLogger(StateManager.class.getSimpleName());

    public StateManager(Graph graph) {
        nodeLookUp = graph.getAllNodes().toArray(new Node[0]);
    }

    private Schedule translateState(State state) {
        //TODO
        return null;
    }

    private State translateSchedule(Schedule schedule) {
        //TODO
        return null;
    }

    /**
     * This method add a new schedule to the priority queue.
     * Queuing order in the queue is based on the heuristics which are
     * implemented in the overridden comparable method for schedule.
     *
     * @param schedule a schedule instance to add to the queue
     */
    public void queue(Schedule schedule) {
        //Comparable needs overwriting in schedule based on heuristic

        if (schedule.getUnscheduledTasks().size() == 0) {
            done = true;
            optimalSchedule = schedule;
            return;
        }

        states.add(translateSchedule(schedule));
        logger.info("Schedule Queued. Queue Length = " + states.size());
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
        return translateState(states.poll());
    }

    public boolean isDone() {
        return done;
    }

    public Schedule getOptimalSchedule() {
        return optimalSchedule;
    }
}
