package se306.a1.scheduler.tests;

import java.util.List;
import java.util.Map;

import dagger.Module;
import dagger.Provides;
import se306.a1.scheduler.data.Edge;
import se306.a1.scheduler.data.Graph;
import se306.a1.scheduler.data.Node;

@Module
public class GraphsModule {
	@Provides Graph provideGraph(TestGraph g) {
		return g;
	}
	
	@Provides Map<Node, List<Edge>> provideChildren() {
		return null;
	}
	
	@Provides Map<Node, List<Node>> provideParents() {
		return null;
	}
	
	@Provides Map<String, Node> provideNodes() {
		return null;
	}
	
	@Provides String provideName() {
		return null;
	}
}
