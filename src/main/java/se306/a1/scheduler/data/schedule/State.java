package se306.a1.scheduler.data.schedule;

import java.util.List;

public class State {
	private byte[] nodes;
	private List<Processor> processors;
	
	protected State(byte[] nodes, List<Processor> processors) {
		this.nodes = nodes;
		this.processors = processors;
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
	
	/**
	 * This method returns the processors for a given state of a graph for use with a star.
	 * Each processor is a byte array with each 1 bit representing a task having been scheduled on that processor.
	 * @return a List of Processor objects. */
	protected List<Processor> getProcessors() {
		return processors;
	}
	
}
