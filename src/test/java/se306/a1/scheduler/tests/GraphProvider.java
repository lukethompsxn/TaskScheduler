package se306.a1.scheduler.tests;

import dagger.Component;
import se306.a1.scheduler.data.Graph;

@Component(modules = GraphsModule.class)
public interface GraphProvider {
	Graph injectGraph();
}
