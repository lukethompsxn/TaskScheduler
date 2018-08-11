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
	protected static int counter = 0;
	
	@Provides Graph provideGraph(TestGraph g) {
		counter++;
		return g;
	}
	
	@Provides Map<Node, List<Edge>> provideChildren() {
		return BasicSchedulerConfig.getChild(counter);
		
	}
	
	@Provides Map<Node, List<Node>> provideParents() {
		return BasicSchedulerConfig.getParent(counter);
	}
	
	@Provides Map<String, Node> provideNodes() {
		return BasicSchedulerConfig.getNodes(counter);
	}
	
	@Provides String provideName() {
		return BasicSchedulerConfig.getName(counter);
	}
}
