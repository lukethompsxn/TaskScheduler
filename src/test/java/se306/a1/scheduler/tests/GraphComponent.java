package se306.a1.scheduler.tests;

import java.util.List;
import java.util.Map;

import dagger.Component;
import se306.a1.scheduler.data.Edge;
import se306.a1.scheduler.data.Node;

@Component(modules = GraphsModule.class)
public interface GraphComponent {
	Map<Node, List<Edge>> injectChildren();
	Map<Node, List<Node>> injectParents();
	Map<String, Node> injectNodes();
	String injectName();
}
