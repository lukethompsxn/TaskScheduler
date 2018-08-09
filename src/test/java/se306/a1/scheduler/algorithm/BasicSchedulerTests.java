package se306.a1.scheduler.algorithm;

import org.junit.BeforeClass;
import org.junit.Test;
import se306.a1.scheduler.data.graph.Edge;
import se306.a1.scheduler.data.graph.Graph;
import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.data.schedule.Processor;
import se306.a1.scheduler.data.schedule.Schedule;
import se306.a1.scheduler.util.exception.GraphException;
import se306.a1.scheduler.util.parse.GraphParser;
import se306.a1.scheduler.util.exception.ScheduleException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

public class BasicSchedulerTests {
    private static final List<Graph> graphs = new ArrayList<>();

    @BeforeClass
    public static void parseGraphs() {
        try (Stream<Path> paths = Files.walk(Paths.get("input_graphs/"))) {
            paths.filter(p -> p.toString().endsWith(".dot")).forEach(p -> {
                try {
                    if (!p.toString().contains("-cycle")) {
                        graphs.add(GraphParser.parse(p.toString()));
                    }
                } catch (IOException | GraphException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSingleProcessorScheduleIsValid() throws ScheduleException {
        final int processors = 1;
        for (Graph g : graphs) {
            Schedule s = new BasicScheduler().run(g, processors, 1);
            assertTrue(isValid(g, s));
        }
    }

    @Test
    public void testMultiProcessorScheduleIsValid() throws ScheduleException {
        final int processors = 4;
        for (Graph g : graphs) {
            Schedule s = new BasicScheduler().run(g, processors, 1);
            assertTrue(isValid(g, s));
        }
    }

    private boolean isValid(Graph g, Schedule s) throws ScheduleException {
        Queue<Node> nodes = new LinkedList<>(g.getEntryNodes());
        while (!nodes.isEmpty()) {
            Node node = nodes.poll();
            Processor p = s.getProcessor(node);
            int startTime = s.getStartTime(node);
            int endTime = startTime + node.getCost();

            // Check that the node has been scheduled correctly after all its parents
            for (Node parent : g.getParents(node)) {
                int parentEndTime = s.getStartTime(parent) + parent.getCost();
                if (s.getProcessor(parent).equals(p)) {
                    if (startTime < parentEndTime)
                        return false;
                } else {
                    if (startTime < parentEndTime + g.getCost(parent, node))
                        return false;
                }
            }

            // Check that the node does not overlap with any other nodes
            // scheduled on the same processor
            for (Node other : s.getTasks(p)) {
                if (node.equals(other))
                    continue;
                int otherStartTime = s.getStartTime(other);
                int otherEndTime = otherStartTime + other.getCost();
                int overlap = Math.max(0,
                        Math.min(endTime, otherEndTime) - Math.max(startTime, otherStartTime));

                if (overlap != 0)
                    return false;
            }

            nodes.remove(node);

            List<Node> children = new ArrayList<>();
            for (Edge e : g.getEdges(node)) {
                children.add(e.getChild());
            }

            nodes.addAll(children);
        }
        return true;
    }
}
