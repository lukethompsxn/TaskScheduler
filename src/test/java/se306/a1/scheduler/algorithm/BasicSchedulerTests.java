package se306.a1.scheduler.algorithm;

import org.junit.BeforeClass;
import org.junit.Test;
import se306.a1.scheduler.data.graph.Graph;
import se306.a1.scheduler.data.schedule.Schedule;
import se306.a1.scheduler.util.exception.ScheduleException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class BasicSchedulerTests {
    private static SchedulerTestHelper helper = new SchedulerTestHelper();
    private static List<Graph> graphs = new ArrayList<>();

    @BeforeClass
    public static void parseGraphs() {
        graphs = helper.parseGraphs();
    }

    @Test
    public void testSingleProcessorScheduleIsValid() throws ScheduleException {
        final int processors = 1;
        for (Graph g : graphs) {
            Schedule s = new BasicScheduler().run(g, processors, 1);
            assertTrue(helper.isValid(g, s));
        }
    }

    @Test
    public void testMultiProcessorScheduleIsValid() throws ScheduleException {
        final int processors = 4;
        for (Graph g : graphs) {
            Schedule s = new BasicScheduler().run(g, processors, 1);
            assertTrue(helper.isValid(g, s));
        }
    }


}
