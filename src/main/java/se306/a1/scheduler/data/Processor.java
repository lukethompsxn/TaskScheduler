package se306.a1.scheduler.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class represents a processor, including it's previous tasks and the time at which the previous task
 * on the processor will finish.
 * @author Rodger Gu
 *  */
public class Processor {
	private List<Node> previousTasks;
	private int time = 0;

	public Processor() {
		previousTasks = new ArrayList<>();
	}
	
	public void process(Node n, int start) {
		previousTasks.add(n);
		time = start + n.getCost();
	}
	
	public Collection<Node> getTasks() {
		return previousTasks;
	}
	
	public int getTime() {
		return time;
	}
}
