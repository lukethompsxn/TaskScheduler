package se306.a1.scheduler.util;

import javafx.util.Pair;
import org.junit.BeforeClass;
import org.junit.Test;
import se306.a1.scheduler.data.Edge;
import se306.a1.scheduler.data.Graph;
import se306.a1.scheduler.data.Node;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;

public class GraphParserTests {
    private static final Map<String, Pair<Integer, Integer>> answers = new HashMap<>();

    @BeforeClass
    public static void setUp() {
        answers.put("input_graphs/Nodes_7_OutTree.dot", new Pair<>(7, 6));
        answers.put("input_graphs/Nodes_8_Random.dot", new Pair<>(8, 16));
        answers.put("input_graphs/Nodes_9_SeriesParallel.dot", new Pair<>(9, 12));
        answers.put("input_graphs/Nodes_10_Random.dot", new Pair<>(10, 19));
        answers.put("input_graphs/Nodes_11_OutTree.dot", new Pair<>(11, 10));
    }

    @Test
    public void testGraphParsing() throws IOException {
        for (Map.Entry<String, Pair<Integer, Integer>> e : answers.entrySet()) {
            int nodeCount = e.getValue().getKey();
            int edgeCount = e.getValue().getValue();

            Graph g = GraphParser.parse(e.getKey());
            Set<Node> nodes = new HashSet<>();
            Set<Edge> edges = new HashSet<>();

            Queue<Node> queue = new LinkedList<>();
            queue.addAll(g.getEntryNodes());

            while (!queue.isEmpty()) {
                Node node = queue.poll();
                nodes.add(node);
                edges.addAll(g.getEdges(node));
                for (Edge edge : g.getEdges(node)) {
                    queue.add(edge.getChild());
                }
            }
            assertEquals(nodes.size(), nodeCount);
            assertEquals(edges.size(), edgeCount);
        }
    }
}
