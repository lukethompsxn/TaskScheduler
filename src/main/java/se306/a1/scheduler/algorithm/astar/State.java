package se306.a1.scheduler.algorithm.astar;

public class State {
	private byte[] nodes;
	
	protected State(byte[] nodes) {
		this.nodes = nodes;
	}
	
	protected byte[] getState() {
		return nodes;
	}
	
	protected String getStateString() {
		String output = "";
		for(int i = 0; i < nodes.length; i++) {
			output += Byte.toString(nodes[i]);
		}
		
		return output;
	}
}
