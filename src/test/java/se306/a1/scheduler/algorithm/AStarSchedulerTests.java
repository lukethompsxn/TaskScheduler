package se306.a1.scheduler.algorithm;

import org.junit.BeforeClass;
import org.junit.Test;
import se306.a1.scheduler.data.graph.Graph;
import se306.a1.scheduler.data.schedule.Schedule;
import se306.a1.scheduler.support.GXLGraph;
import se306.a1.scheduler.util.exception.GraphException;
import se306.a1.scheduler.util.exception.ScheduleException;
import se306.a1.scheduler.util.parse.GraphParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AStarSchedulerTests {
    private static Map<Graph, Integer> timedGraphs2Proc = new HashMap<>();
    private static Map<Graph, Integer> timedGraphs4Proc = new HashMap<>();
    private static List<GXLGraph> gxlGraphs = new ArrayList<>();

    @BeforeClass
    public static void prepareGraphs() throws IOException, GraphException {
        timedGraphs2Proc.put(GraphParser.parse("input_graphs/Nodes_7_OutTree.dot"), 28);
        timedGraphs2Proc.put(GraphParser.parse("input_graphs/Nodes_8_Random.dot"), 581);
        timedGraphs2Proc.put(GraphParser.parse("input_graphs/Nodes_9_SeriesParallel.dot"), 55);
        timedGraphs2Proc.put(GraphParser.parse("input_graphs/Nodes_10_Random.dot"), 50);
//        timedGraphs2Proc.put(GraphParser.parse("input_graphs/Nodes_11_OutTree.dot"), 350);

        timedGraphs4Proc.put(GraphParser.parse("input_graphs/Nodes_7_OutTree.dot"), 22);
        timedGraphs4Proc.put(GraphParser.parse("input_graphs/Nodes_8_Random.dot"), 581);
        timedGraphs4Proc.put(GraphParser.parse("input_graphs/Nodes_9_SeriesParallel.dot"), 55);
        timedGraphs4Proc.put(GraphParser.parse("input_graphs/Nodes_10_Random.dot"), 50);
        timedGraphs4Proc.put(GraphParser.parse("input_graphs/Nodes_11_OutTree.dot"), 227);

        gxlGraphs = SchedulerTestHelper.parseGXLGraphs();
    }

    @Test
    public void testTimedGraphs2ProcByte() throws ScheduleException {
        final int processors = 2;
        for (Graph g : timedGraphs2Proc.keySet()) {
            Schedule s = new AStarByteScheduler().run(g, processors, 1);
            assertTrue(SchedulerTestHelper.isValid(g, s));
            assertTrue(timedGraphs2Proc.get(g).equals(s.getLength()));
        }
    }

    @Test
    public void testTimedGraphs4ProcByte() throws ScheduleException {
        final int processors = 4;
        for (Graph g : timedGraphs4Proc.keySet()) {
            Schedule s = new AStarByteScheduler().run(g, processors, 1);
            assertTrue(SchedulerTestHelper.isValid(g, s));
            assertTrue(timedGraphs4Proc.get(g).equals(s.getLength()));
        }
    }

    @Test
    public void testGXLGraphsByte() throws ScheduleException {
        for (GXLGraph g : gxlGraphs) {
            Schedule s = new AStarByteScheduler().run(g.getGraph(), g.getNumProcessors(), 1);
            assertTrue(SchedulerTestHelper.isValid(g.getGraph(), s));
            assertEquals(s.getLength(), g.getLength());
            System.out.println("[Byte] Graph Passed Multiple Processor: " + g.getGraph().getName());

            if (g.getSequentialLength() != -1) {
                Schedule s_sequential = new AStarByteScheduler().run(g.getGraph(), 1, 1);
                assertTrue(SchedulerTestHelper.isValid(g.getGraph(), s_sequential));
                assertEquals(s_sequential.getLength(), g.getSequentialLength());
                System.out.println("[Byte] Graph Passed Single Processor: " + g.getGraph().getName());

            }
        }
    }
}
