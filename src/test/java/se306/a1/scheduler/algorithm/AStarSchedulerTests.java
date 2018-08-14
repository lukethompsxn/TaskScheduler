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

        //Change enum to desired graph type to test
        gxlGraphs = SchedulerTestHelper.parseGXLGraphs(SchedulerTestHelper.GraphType.TEN_NODES);
    }

//    @Test
    public void testTimedGraphs2ProcByte() throws ScheduleException {
        final int processors = 2;
        for (Graph g : timedGraphs2Proc.keySet()) {
            Schedule s = new AStarByteScheduler().run(g, processors, 1, false);
            assertTrue(SchedulerTestHelper.isValid(g, s));
            assertTrue(timedGraphs2Proc.get(g).equals(s.getLength()));
        }
    }

//    @Test
    public void testTimedGraphs4ProcByte() throws ScheduleException {
        final int processors = 4;
        for (Graph g : timedGraphs4Proc.keySet()) {
            Schedule s = new AStarByteScheduler().run(g, processors, 1, false);
            assertTrue(SchedulerTestHelper.isValid(g, s));
            assertTrue(timedGraphs4Proc.get(g).equals(s.getLength()));
        }
    }

    @Test
    public void testGXLGraphsByte() throws ScheduleException {
        long firstStart = System.nanoTime();
        for (GXLGraph g : gxlGraphs) {
            System.out.println("[Testing]\t" + g.getGraph().getName());

            long start = System.nanoTime();
            Schedule s = new AStarByteScheduler().run(g.getGraph(), g.getNumProcessors(), 1, false);
            long end = System.nanoTime();

            assertTrue(SchedulerTestHelper.isValid(g.getGraph(), s));
            assertEquals(g.getLength(), s.getLength());

            System.out.println("[Byte]\tGraph Passed on " + g.getNumProcessors() + " Processors");
            System.out.println("[Time]\t" + (end - start) / 1000000);

            if (g.getSequentialLength() != -1) {
                start = System.nanoTime();
                Schedule s_sequential = new AStarByteScheduler().run(g.getGraph(), 1, 1, false);
                end = System.nanoTime();

                assertTrue(SchedulerTestHelper.isValid(g.getGraph(), s_sequential));
                assertEquals(g.getSequentialLength(), s_sequential.getLength());

                System.out.println("[Byte]\tGraph Passed Single Processor");
                System.out.println("[Time]\t" + (end - start) / 1000000);
            }
        }
        System.out.println((System.nanoTime() - firstStart) / 1000000);
    }
}
