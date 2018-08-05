package se306.a1.scheduler.tests;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Edge> getEdges(Node node) {
		return null;
	}

	@Override
	public List<Node> getParents(Node node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Node> getEntryNodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getCost(Node parent, Node child) {
		// TODO Auto-generated method stub
		return null;
	}

}
