package se306.a1.scheduler.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import se306.a1.scheduler.data.Edge;
import se306.a1.scheduler.data.Node;

public class BasicSchedulerConfig {
	protected static final int NO_OF_TEST_GRAPHS = 8;
	
	private static final ArrayList<Map<Node, List<Edge>>> children = new ArrayList<>();
	private static final ArrayList<Map<Node, List<Node>>> parents = new ArrayList<>();
	private static final ArrayList<Map<String, Node>> nodes = new ArrayList<>();
	private static final ArrayList<String> names = new ArrayList<>();
	
	//the children mocks
	
	
	//the parent mocks
	
	
	//the node mocks
	
	
	//the name mocks
	
	
	protected static Map<Node, List<Edge>> getChild(int i) {
		return children.get(i);
	}
	
	protected static Map<Node, List<Node>> getParent(int i) {
		return parents.get(i);
	}
	
	protected static Map<String, Node> getNodes(int i) {
		return nodes.get(i);
	}
	
	protected static String getName(int i) {
		return names.get(i);
	}
	
	private static boolean isPopulated = false;
	
	protected static void populate() {
		if(!isPopulated) {
			
		}
		
		isPopulated = true;
	}
}
