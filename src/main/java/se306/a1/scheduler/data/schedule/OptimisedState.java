package se306.a1.scheduler.data.schedule;

import se306.a1.scheduler.data.graph.Node;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * NOT CURRENTLY USED DURING DEV, WILL BE IMPLEMENTED TO OPTIMISE A-STAR
 * AFTER WE GET IT RUNNING PROPERLY.
 */
public class OptimisedState implements State {
	private byte[] nodes;
	private List<Processor> processors;
	private int[] startTimes;
	
	protected OptimisedState(byte[] nodes, List<Processor> processors, int[] startTimes) {
		this.nodes = nodes;
		this.processors = processors;
		this.startTimes = startTimes;
	}
	
	/**
	 * This method returns a given state of a graph for use with a star.
	 * This is the set of visible nodes to the algorithm, stored as a byte array.
	 * @return the byte array representing the state of the graph for a star. */
	protected byte[] getState() {
		return nodes;
	}
	
	/**
	 * This method stores returns the state of a graph for use with a star in the form of
	 * a string.
	 * @return a String representing the state of the graph for a star. */
	protected String getStateString() {
		String output = "";
		for(int i = 0; i < nodes.length; i++) {
			output += Byte.toString(nodes[i]);
		}
		
		return output;
	}

	@Override
	public Map<Node, Integer> getStartTimes() {
		return null;
	}

	@Override
	public Set<Node> getUnscheduledTasks() {
		return null;
	}

	@Override
	public Set<Node> getScheduledTasks() {
		return null;
	}

	/**
	 * This method returns the processors for a given state of a graph for use with a star.
	 * Each processor is a byte array with each 1 bit representing a task having been scheduled on that processor.
	 * @return a List of Processor objects. */
	@Override
	public List<Processor> getProcessors() {
		return processors;
	}
	
}
