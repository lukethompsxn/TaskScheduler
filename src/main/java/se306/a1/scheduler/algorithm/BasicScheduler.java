package se306.a1.scheduler.algorithm;

import java.util.ArrayList;

import se306.a1.scheduler.data.Edge;
import se306.a1.scheduler.data.Graph;
import se306.a1.scheduler.data.Node;
import se306.a1.scheduler.data.Processor;
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
		
		while(!visibleNodes.isEmpty() || currentNode.equals(g.getRootNode())) {
			ArrayList<Edge> links = (ArrayList<Edge>) g.getLinks(currentNode);
			
			for(Edge e : links) {
				visibleNodes.add(e.getChild());
			}
			
			currentNode = computeCheapest(visibleNodes, links);
			
			visibleNodes.remove(currentNode);
		}
	}
	
	/**
	 * This method is given a list of visible tasks, and the costs of those tasks
	 * and is meant to compute the cheapest possible task. */
	private Node computeCheapest(ArrayList<Node> nodes, ArrayList<Edge> links) {
		Node cheapest = null;
		int time = Integer.MAX_VALUE;
		Processor processor = null;
		ArrayList<Processor> processors = (ArrayList<Processor>) schedule.getProcessors();
		
		//for each given node
		for(int i = 0; i < nodes.size(); i++) {
			Node n = nodes.get(i);
			Edge e = links.get(i);
			//check each processor to see what the most available time is.
			for(Processor p : processors) {
				ArrayList<Node> scheduled = (ArrayList<Node>) p.getTasks();
				int nodeTime = 0;
				//check if the link cost needs to be accounted for
				//TODO this method is currently broken. Add in logic to check if the dependency before it as been run.

                //Determines whether all the parent tasks have already been scheduled to a processor
                //Very inefficient, will be better to store an object of scheduledTasks
				boolean allParentsScheduled = false;
				for (Processor proc : processors) {
					if (proc.getTasks().containsAll(g.getParents(n))) {
						allParentsScheduled = true;
					}
				}

				if(scheduled.contains(e.getParent())) {
					nodeTime = p.getTime() + n.getCost();
				} else {
					nodeTime = p.getTime() + n.getCost() + e.getCost();
				}

				//If all parents are not scheduled, ensure not selected as cheapest
				if (!allParentsScheduled) { nodeTime = Integer.MAX_VALUE; }

				if(nodeTime < time) {
					time = nodeTime;
					cheapest = n;
					processor = p;
				}
			}
		}
		
		processor.process(cheapest, time);
		return cheapest;
	}
}
