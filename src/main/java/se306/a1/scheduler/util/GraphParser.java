package se306.a1.scheduler.util;

import se306.a1.scheduler.data.*;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is responsible for parsing graphs in the .dot file format into
 * TaskGraph objects and vice versa, from a TaskGraph to a valid .dot file.
 *
 * @author Abhinav Behal
 */
public class GraphParser {

    private final static Pattern NAME_REGEX = Pattern.compile("\"([^\"]+)\"");
    private final static Pattern NODE_REGEX = Pattern.compile("\\W*([\\w\\d]+)\\W*\\[Weight=(\\d+)");
    private final static Pattern EDGE_REGEX = Pattern.compile("\\W*([\\w\\d]+)\\W*\\->\\W*([\\w\\d]+)\\W*\\[Weight=(\\d+)");

    /**
     * Parses the input .dot file into an TaskGraph object.
     * This method will take the input filepath and parse directed acyclic graph
     * into a data object with nodes, edges, and corresponding costs. Within
     * this hierarchy, parent and child relationships will explicit.
     *
     * @param inputPath filepath of input .dot file
     * @return parsed object containing nodes, links, costs
     * @throws IOException         if the file cannot be found, or if an IO error occurs while reading
     * @throws GraphParseException if the graph cannot be parsed
     */
    public Graph parseGraph(String inputPath) throws IOException, GraphParseException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
            String name = "";
            String line = reader.readLine();
            Matcher nameMatcher = NAME_REGEX.matcher(line);

            if (nameMatcher.find()) {
                name = nameMatcher.group(1);
            } else {
                throw new GraphParseException(line);
            }

            Map<String, Node> nodes = new HashMap<>();
            ArrayList<Edge> edges = new ArrayList();

            while ((line = reader.readLine()) != null) {
                Matcher nodeMatcher = NODE_REGEX.matcher(line);
                Matcher edgeMatcher = EDGE_REGEX.matcher(line);

                if (edgeMatcher.find()) {
                    String parent = edgeMatcher.group(1);
                    String child = edgeMatcher.group(2);
                    int weight = Integer.parseInt(edgeMatcher.group(3));

                    edges.add(new Edge(nodes.get(parent), nodes.get(child), weight));
                } else if (nodeMatcher.find()) {
                    String label = nodeMatcher.group(1);
                    int weight = Integer.parseInt(nodeMatcher.group(2));

                    nodes.put(label, new Node(label, weight));
                } else if (line.equals("}")) {
                    break;
                }
            }
            return new TaskGraph(name, nodes, edges);
        }
    }

    /**
     * Generates the .dot file output from TaskGraph object.
     * This method will take the computed schedule object and generate the
     * corresponding .dot output file.
     *
     * @param taskGraph calculated schedule object
     * @throws IOException if the output file cannot be created or if an error occurs while writing
     */
    public void generateOutput(Graph taskGraph, String outputPath) throws IOException {
        File file = new File(outputPath);
        if (!file.exists()) {
            file.createNewFile();
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write("digraph \"" + taskGraph.getName() + "\" {");
            Queue<Edge> edges = new LinkedList<>();
            Set<Node> marked = new HashSet<>();

            for (Node n : taskGraph.getEntryNodes())
                edges.addAll(taskGraph.getEdges(n));

            // TODO fix output
            while (!edges.isEmpty()) {
                Node node = edges.poll().getChild();
                if (!marked.contains(node)) {
                    bufferedWriter.newLine();
                    bufferedWriter.write(formatNode(node));
                    marked.add(node);
                } else {
                    continue;
                }
                List<Edge> links = taskGraph.getEdges(node);
                for (Edge e : links) {
                    edges.add(e);
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
    private String formatNode(Node node) throws ScheduleException {
        return "\t" + node.toString() +
                "\t[Weight=" + node.getCost() +
                ", Start=" + Schedule.getStartTime(node) +
                ", Processor=" + Schedule.getProcessor(node) + "];";
    }
}

