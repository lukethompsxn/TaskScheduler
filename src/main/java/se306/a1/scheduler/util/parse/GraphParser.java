package se306.a1.scheduler.util.parse;

import com.paypal.digraph.parser.GraphEdge;
import com.paypal.digraph.parser.GraphNode;
import se306.a1.scheduler.data.graph.Edge;
import se306.a1.scheduler.data.graph.Graph;
import se306.a1.scheduler.data.graph.Node;
import se306.a1.scheduler.data.graph.TaskGraph;
import se306.a1.scheduler.data.schedule.Processor;
import se306.a1.scheduler.data.schedule.Schedule;
import se306.a1.scheduler.util.exception.GraphException;
import se306.a1.scheduler.util.exception.ScheduleException;

import java.io.*;
import java.util.*;

/**
 * This class is responsible for parsing graphs in the .dot file format into
 * TaskGraph objects and vice versa, from a TaskGraph to a valid .dot file.
 *
 * @author Abhinav Behal, Luke Thompson
 */
public class GraphParser {

    /**
     * Parses the input .dot file into an TaskGraph object.
     * This method will take the input filepath and parse the directed acyclic graph
     * into a data object with nodes, edges, and corresponding costs. Within
     * this hierarchy, parent and child relationships will be defined.
     *
     * @param inputPath filepath of input .dot file
     * @return parsed object containing nodes, links, costs
     * @throws IOException    if the file cannot be found, or if an IO error occurs while reading
     * @throws GraphException if the graph object contains invalid elements (cycles etc.)
     */
    public static Graph parse(String inputPath) throws IOException, GraphException {
        com.paypal.digraph.parser.GraphParser parser =
                new com.paypal.digraph.parser.GraphParser(new FileInputStream(inputPath));
        Map<String, GraphNode> parsedNodes = parser.getNodes();
        Map<String, GraphEdge> parsedEdges = parser.getEdges();

        Map<Node, List<Node>> ancestors = new HashMap<>();
        Map<String, Node> nodes = new HashMap<>();
        List<Edge> edges = new ArrayList<>();

        if (parsedNodes.values().isEmpty()) {
            throw new GraphException("No nodes contained in the graph");
        }

        for (GraphNode node : parsedNodes.values()) {
            String label = node.getId();

            if (node.getAttribute("Weight") == null)
                throw new GraphException("Node '" + label + "' not declared or does not have 'Weight' attribute");
            int weight = Integer.parseInt(node.getAttribute("Weight").toString());

            if (weight < 0)
                throw new GraphException("Nodes cannot have negative weight: " + label);

            nodes.put(label, new Node(label, weight));
        }

        for (GraphEdge edge : parsedEdges.values()) {
            Node parent = nodes.get(edge.getNode1().getId());
            Node child = nodes.get(edge.getNode2().getId());

            if (edge.getAttribute("Weight") == null)
                throw new GraphException("Edge '" + parent + "->" + child + "' does not have 'Weight' attribute");
            int weight = Integer.parseInt(edge.getAttribute("Weight").toString());

            if (weight < 0)
                throw new GraphException("Edges cannot have negative weight: " + parent + "->" + child);

            List<Node> currentAncestors = new ArrayList<>();
            if (ancestors.get(parent) != null) {
                currentAncestors.addAll(ancestors.get(parent));
            }

            currentAncestors.add(parent);
            ancestors.put(child, currentAncestors);

            if (currentAncestors.contains(child)) {
                throw new GraphException("Graph contains a cycle with edge: " + parent.getLabel() + " -> " + child.getLabel());
            }

            edges.add(new Edge(parent, child, weight));
        }
        return new TaskGraph(parser.getGraphId().replaceAll("\"", ""), nodes, edges);
    }

    /**
     * Generates the .dot file output from a Schedule and Graph object.
     * This method will take the computed schedule object and generate the
     * corresponding .dot output file.
     *
     * @param schedule   calculated schedule object
     * @param graph      graph representation that is scheduled
     * @param outputPath location where the output file will be made at
     * @throws IOException       if the output file cannot be created or if an error occurs while writing
     * @throws ScheduleException if a node hasn't been correctly scheduled
     */
    public static void generateOutput(Schedule schedule, Graph graph, String outputPath) throws IOException, ScheduleException {
        File file = new File(outputPath);

        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (parentFile != null && !parentFile.exists()) {
                parentFile.mkdirs();
            }
            file.createNewFile();
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write("digraph \"output" + graph.getName() + "\" {");

            Set<Edge> edges = new HashSet<>();

            for (Processor processor : schedule.getProcessors()) {
                for (Node node : schedule.getTasks(processor)) {
                    bufferedWriter.newLine();
                    bufferedWriter.write(formatNode(node, schedule.getStartTime(node), processor.getName()));
                    edges.addAll(graph.getEdges(node));
                }
            }

            for (Edge edge : edges) {
                bufferedWriter.newLine();
                bufferedWriter.write(edge.toString());
            }

            bufferedWriter.newLine();
            bufferedWriter.write("}");
        }
    }

    /**
     * This method formats the string for printing a node.
     *
     * @param node      the node which requires formatting for output
     * @param startTime time at which the node is scheduled to start
     * @param processor the processor on which the node is scheduled to be executed on
     * @return formatted output string for the supplied node
     */
    private static String formatNode(Node node, int startTime, String processor) {
        return "\t" + node.toString() +
                "\t[Weight=" + node.getCost() +
                ", Start=" + startTime +
                ", Processor=" + processor + "];";
    }
}
