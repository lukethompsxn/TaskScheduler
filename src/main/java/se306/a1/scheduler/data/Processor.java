package se306.a1.scheduler.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a processor, including it's previous tasks and the time at which the previous task
 * on the processor will finish.
 * @author Rodger Gu
 *  */
public class Processor {
	private HashMap<Integer, Node> previousTasks;
	private List<Integer> times;
	private int time = 0;

	protected Processor() {
		previousTasks = new HashMap<>();
		times = new ArrayList<>();
	}
	
	public void process(Node n, int start) {
		times.add(start);
		previousTasks.put(start, n);
		time = start + n.getCost();
	}
	
	public Collection<Node> getTasks() {
		return previousTasks.values();
	}
	
	public Collection<Integer> getTimes() {
		return times;
	}
	
	public Node getNode(int time) {
		return previousTasks.get(time);
	}
	
	public int getTime() {
		return time;
	}
}
