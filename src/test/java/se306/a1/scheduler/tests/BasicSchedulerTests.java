package se306.a1.scheduler.tests;

import org.junit.Before;
import org.junit.Test;

public class BasicSchedulerTests {
	private GraphProvider g;
	private TestGraph t;
	
	@Before
	private void setUp() {
		g = DaggerGraphProvider.create();
		t = (TestGraph) g.injectGraph();
	}
	
	@Test
	public static void testSingleProcessor() {
		
	}
}
