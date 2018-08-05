package se306.a1.scheduler.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import se306.a1.scheduler.data.Edge;
import se306.a1.scheduler.data.Graph;
import se306.a1.scheduler.data.Node;

public class TestGraph implements Graph{
	@Inject protected Map<Node, List<Edge>> children;
	@Inject protected Map<Node, List<Node>> parents;
	@Inject protected Map<String, Node> nodes;
	@Inject protected String name;

	@Inject
	TestGraph() {
	}
	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<Edge> getEdges(Node node) {
		return children.get(node);
	}

	@Override
	public List<Node> getParents(Node node) {
		return parents.get(node);
	}

	@Override
	public List<Node> getEntryNodes() {
		List<Node> entries = new ArrayList<>();

        for (Node n : nodes.values()) {
            if (!parents.containsKey(n) || parents.get(n).isEmpty())
                entries.add(n);
        }

        return entries;
	}

	@Override
	public Integer getCost(Node parent, Node child) {
		 for (Edge e : children.get(parent)) {
	            if (e.getChild().equals(child))
	                return e.getCost();
	        }
	        return null;
	}

}
