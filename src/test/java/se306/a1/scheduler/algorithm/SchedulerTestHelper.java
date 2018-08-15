package se306.a1.scheduler.algorithm;

import se306.a1.scheduler.data.graph.Edge;
import se306.a1.scheduler.data.graph.Graph;
import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.data.schedule.Processor;
import se306.a1.scheduler.data.schedule.Schedule;
import se306.a1.scheduler.support.GXLGraph;
import se306.a1.scheduler.util.exception.GraphException;
import se306.a1.scheduler.util.exception.ScheduleException;
import se306.a1.scheduler.util.parse.GraphParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Stream;

public class SchedulerTestHelper {
    private static List<GXLGraph> gxlGraphs = new ArrayList<>();
    private static List<Graph> graphs = new ArrayList<>();

    enum GraphType {
        TWO("2p"),
        FOUR("4p"),
        EIGHT("8p"),
        SIXTEEN("16p"),
        FORK("Fork_Nodes"),
        JOIN("Join"),
        FORK_JOIN("Fork_Join"),
        INDEPENDENT("Independent"),
        INTREE("InTree"),
        OUTTREE("OutTree"),
        BALANCED("Balanced"),
        UNBALANCED("Unbalanced"),
        PIPELINE("Pipeline"),
        RANDOM("Random_Nodes"),
        SERIESPARALLEL("SeriesParallel"),
        STENCIL("Stencil"),
        ALL(""),
        NONE("N/A");

        private final String value;

        GraphType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static List<Graph> parseGraphs() {
        graphs = new ArrayList<>();

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

        return graphs;
    }

    public static List<GXLGraph> parseGXLGraphs(GraphType filter) {
        gxlGraphs = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get("input_graphs/gxl_graphs/"))) {
            paths.filter(p ->
                    p.toString().endsWith(".dot") &&
                            !p.toString().endsWith("-output.dot") &&
                            p.toString().contains(filter.toString())).forEach(p -> {
                try {
                    String[] strings = p.toString().split("_");
                    int numProcessors = Integer.parseInt(strings[strings.length - 3]);
                    int length = Integer.parseInt(strings[strings.length - 2]);
                    int sequentialLength = Integer.parseInt(strings[strings.length - 1].replaceAll(".dot", ""));
                    gxlGraphs.add(new GXLGraph(GraphParser.parse(p.toString()), numProcessors, length, sequentialLength));
                } catch (IOException | GraphException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gxlGraphs;
    }

    public static boolean isValid(Graph g, Schedule s) throws ScheduleException {
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
