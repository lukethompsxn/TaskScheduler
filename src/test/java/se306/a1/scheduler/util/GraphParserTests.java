package se306.a1.scheduler.util;

import com.paypal.digraph.parser.GraphEdge;
import com.paypal.digraph.parser.GraphNode;
import org.junit.BeforeClass;
import org.junit.Test;
import se306.a1.scheduler.algorithm.BasicScheduler;
import se306.a1.scheduler.data.graph.Edge;
import se306.a1.scheduler.data.graph.Graph;
import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.data.schedule.Processor;
import se306.a1.scheduler.data.schedule.Schedule;
import se306.a1.scheduler.util.exception.GraphException;
import se306.a1.scheduler.util.exception.ScheduleException;
import se306.a1.scheduler.util.parse.GraphParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class GraphParserTests {
    private static final Map<String, Pair<Integer, Integer>> answers = new HashMap<>();
    private static final List<String> cycleGraphs = new ArrayList<>();

    @BeforeClass
    public static void setUp() {
        answers.put("input_graphs/Nodes_7_OutTree.dot", new Pair<>(7, 6));
        answers.put("input_graphs/Nodes_8_Random.dot", new Pair<>(8, 16));
        answers.put("input_graphs/Nodes_9_SeriesParallel.dot", new Pair<>(9, 12));
        answers.put("input_graphs/Nodes_10_Random.dot", new Pair<>(10, 19));
        answers.put("input_graphs/Nodes_11_OutTree.dot", new Pair<>(11, 10));


        try (Stream<Path> paths = Files.walk(Paths.get("input_graphs/"))) {
            paths.filter(p -> p.toString().endsWith(".dot")).forEach(p -> {
                if (p.toString().contains("-cycle")) {
                    cycleGraphs.add(p.toString());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testGraphParsing() throws IOException, GraphException {
        for (Map.Entry<String, Pair<Integer, Integer>> e : answers.entrySet()) {
            int nodeCount = e.getValue().first();
            int edgeCount = e.getValue().second();

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

    @Test
    public void testCycleGraphParsing() throws IOException {
        for (String path : cycleGraphs) {
            try {
                Graph g = GraphParser.parse(path);
                fail("Should have thrown GraphException");
            } catch (GraphException e) {
                assertTrue(true);
            }
        }
    }

    /**
     * This test only tests the values written to file, not the syntax of the output
     */
    @Test
    public void testOutputGeneration() throws IOException, GraphException, ScheduleException {
        final String outputPath = "test-output.dot";
        for (String inputPath : answers.keySet()) {
            Graph g = GraphParser.parse(inputPath);
            Schedule s = new BasicScheduler().run(g, 1, 2);
            GraphParser.generateOutput(s, g, outputPath);
            ParsedOutput parsedOutput = parseOutput(outputPath);

            Map<String, MockNode> nodes = parsedOutput.getNodes();
            Map<Pair<String, String>, MockEdge> edges = parsedOutput.getEdges();

            int numNodes = 0;
            int numEdges = 0;

            for (Processor processor : s.getProcessors()) {
                for (Node n : s.getTasks(processor)) {
                    numNodes++;
                    assertTrue(n.getLabel().equals(nodes.get(n.getLabel()).getName()));
                    assertTrue(("" + n.getCost()).equals(nodes.get(n.getLabel()).getWeight()));
                    assertTrue(s.getStartTime(n).toString().equals(nodes.get(n.getLabel()).getStartTime()));
                    assertTrue(processor.getName().equals(nodes.get(n.getLabel()).getProcessor()));
                    for (Edge e : g.getEdges(n)) {
                        numEdges++;
                        Pair<String, String> pair = new Pair<>(e.getParent().getLabel(), e.getChild().getLabel());
                        assertTrue(e.getParent().toString().equals(edges.get(pair).getParent()));
                        assertTrue(e.getChild().toString().equals(edges.get(pair).getChild()));
                        assertTrue(("" + e.getCost()).equals(edges.get(pair).getWeight()));
                    }
                }
            }
            assertEquals(parsedOutput.getNodes().size(), numNodes);
            assertEquals(parsedOutput.getEdges().size(), numEdges);
        }
    }

    private ParsedOutput parseOutput(String path) throws FileNotFoundException {
        com.paypal.digraph.parser.GraphParser parser =
                new com.paypal.digraph.parser.GraphParser(new FileInputStream(path));
        Map<String, GraphNode> parsedNodes = parser.getNodes();
        Map<String, GraphEdge> parsedEdges = parser.getEdges();

        ParsedOutput parsedOutput = new ParsedOutput();

        for (GraphNode node : parsedNodes.values()) {
            String label = node.getId();
            String weight = node.getAttribute("Weight").toString();
            String startTime = node.getAttribute("Start").toString();
            String processor = node.getAttribute("Processor").toString();

            parsedOutput.addNode(new MockNode(label, weight, startTime, processor));
        }

        for (GraphEdge edge : parsedEdges.values()) {
            String parent = edge.getNode1().getId();
            String child = edge.getNode2().getId();
            String weight = edge.getAttribute("Weight").toString();

            parsedOutput.addEdge(new MockEdge(parent, child, weight));
        }

        return parsedOutput;
    }

    public class ParsedOutput {
        Map<String, MockNode> nodes = new HashMap<>();
        Map<Pair<String, String>, MockEdge> edges = new HashMap<>();

        void addNode(MockNode node) {
            nodes.put(node.getName(), node);
        }

        void addEdge(MockEdge edge) {
            edges.put(new Pair<>(edge.getParent(), edge.getChild()), edge);
        }

        Map<String, MockNode> getNodes() {
            return nodes;
        }

        Map<Pair<String, String>, MockEdge> getEdges() {
            return edges;
        }
    }

    public class MockNode {
        String name;
        String weight;
        String startTime;
        String processor;

        MockNode(String name, String weight, String startTime, String processor) {
            this.name = name;
            this.weight = weight;
            this.startTime = startTime;
            this.processor = processor;
        }

        String getName() {
            return name;
        }

        String getWeight() {
            return weight;
        }

        String getStartTime() {
            return startTime;
        }

        String getProcessor() {
            return processor;
        }
    }

    public class MockEdge {
        String parent;
        String child;
        String weight;

        String getParent() {
            return parent;
        }

        String getChild() {
            return child;
        }

        String getWeight() {
            return weight;
        }

        MockEdge(String parent, String child, String weight) {
            this.parent = parent;
            this.child = child;
            this.weight = weight;
        }
    }
}
