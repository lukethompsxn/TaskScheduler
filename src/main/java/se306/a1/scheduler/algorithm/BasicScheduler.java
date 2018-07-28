package se306.a1.scheduler.algorithm;

import java.util.ArrayList;

import se306.a1.scheduler.data.Edge;
import se306.a1.scheduler.data.Graph;
import se306.a1.scheduler.data.Node;
import se306.a1.scheduler.data.Schedule;

/**
 * This class is a basic implementation of a scheduler, and will produce an unoptimised schedule by greedily
 * assigning the tasks to the processors.
 * @author Rodger Gu
 *  */
public class BasicScheduler extends Scheduler{
	private Schedule schedule;
	private Graph g;
	
	@Override
	public void init(Graph g, int numProcessors, int numCores, boolean hasVisualisation, String output) {
		schedule = new Schedule();
		this.g = g;
		
		//instantiate the processors
		for(int i = 0; i < numProcessors; i++) {
			schedule.addProcessor();
		}
		
		createSchedule();
		
	}
	
	/**
	 * This method traverses the graph and actually creates the schedule. This is then
	 * either output to console or output to a GUI.
	 * */
	private void createSchedule() {
		boolean isDone = false;
		ArrayList<Node> visibleNodes = new ArrayList<>();
		
		Node currentNode = g.getRootNode();
		Node previousNode = g.getRootNode();
		while(!visibleNodes.isEmpty() || currentNode.equals(g.getRootNode())) {
			ArrayList<Edge> links = (ArrayList<Edge>) g.getLinks(currentNode);
			
			for(Edge e : links) {
				visibleNodes.add(e.getChild());
			}
			
			previousNode = currentNode;
			currentNode = visibleNodes.get(0);
		}
	}
	
	/**
	 * This method is given a list of visible tasks, and the costs of those tasks
	 * and is meant to compute the cheapest possible task. */
	private void computeCheapest(ArrayList<Node> nodes, ArrayList<Edge> links) {
		
	}
}
