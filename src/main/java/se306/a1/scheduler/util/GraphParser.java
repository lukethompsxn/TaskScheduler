package se306.a1.scheduler.util;

import com.paypal.digraph.parser.GraphEdge;
import com.paypal.digraph.parser.GraphNode;
import se306.a1.scheduler.data.Edge;
import se306.a1.scheduler.data.Graph;
import se306.a1.scheduler.data.Node;
import se306.a1.scheduler.data.TaskGraph;

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
     * This method will take the input filepath and parse directed acyclic graph
     * into a data object with nodes, edges, and corresponding costs. Within
     * this hierarchy, parent and child relationships will explicit.
     *
     * @param inputPath filepath of input .dot file
     * @return parsed object containing nodes, links, costs
     * @throws IOException if the file cannot be found, or if an IO error occurs while reading
     */
    public static Graph parse(String inputPath) throws IOException {
        com.paypal.digraph.parser.GraphParser parser =
                new com.paypal.digraph.parser.GraphParser(new FileInputStream(inputPath));
        Map<String, GraphNode> parsedNodes = parser.getNodes();
        Map<String, GraphEdge> parsedEdges = parser.getEdges();

        Map<String, Node> nodes = new HashMap<>();
        List<Edge> edges = new ArrayList<>();

        for (GraphNode node : parsedNodes.values()) {
            String label = node.getId();
            int weight = Integer.parseInt(node.getAttribute("Weight").toString());

            nodes.put(label, new Node(label, weight));
        }

        for (GraphEdge edge : parsedEdges.values()) {
            Node parent = nodes.get(edge.getNode1().getId());
            Node child = nodes.get(edge.getNode2().getId());
            int weight = Integer.parseInt(edge.getAttribute("Weight").toString());

            edges.add(new Edge(parent, child, weight));
        }
        return new TaskGraph(parser.getGraphId(), nodes, edges);
    }

    /**
     * Generates the .dot file output from TaskGraph object.
     * This method will take the computed schedule object and generate the
     * corresponding .dot output file.
     *
     * @param taskGraph calculated schedule object
     * @throws IOException if the output file cannot be created or if an error occurs while writing
     */
    public static void generateOutput(Graph taskGraph, String outputPath) throws IOException {
        File file = new File(outputPath);
        if (!file.exists()) {
            file.createNewFile();
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write("digraph \"" + taskGraph.getName() + "\" {");
            Set<Node> marked = new HashSet<>();
            Queue<Node> nodes = new LinkedList<>();
            nodes.addAll(taskGraph.getEntryNodes());

            while (!nodes.isEmpty()) {
                Node node = nodes.poll();
                if (!marked.contains(node)) {
                    bufferedWriter.newLine();
                    bufferedWriter.write(formatNode(node));
                    marked.add(node);
                } else {
                    continue;
                }
                List<Edge> links = taskGraph.getEdges(node);
                for (Edge e : links) {
                    nodes.add(e.getChild());
                    bufferedWriter.newLine();
                    bufferedWriter.write(e.toString());
                }
            }
            bufferedWriter.newLine();
            bufferedWriter.write("}");
        } catch (ScheduleException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method formats the string for printing a node.
     *
     * @param node the node which requires formatting for output
     * @return formatted output string for the supplied node
     */
    private static String formatNode(Node node) throws ScheduleException {
        // TODO Get appropriate information from the schedule
        return "\t" + node.toString() +
                "\t[Weight=" + node.getCost() +
                ", Start=" /*+ Schedule.getStartTime(node)*/ +
                ", Processor=" /*+ Schedule.getProcessor(node)*/ + "];";
    }
}

