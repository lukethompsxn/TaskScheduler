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
     * @throws IOException    if the file cannot be found, or if an IO error occurs while reading
     * @throws GraphParseException if the graph cannot be parsed
     */
    public TaskGraph parseGraph(String inputPath) throws IOException, GraphParseException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
            String name = "";
            String line = reader.readLine();
            Matcher nameMatcher = NAME_REGEX.matcher(line);
            if (nameMatcher.find()) {
                name = nameMatcher.group(1);
            } else {
                throw new GraphParseException(line);

            }
            TaskGraph graph = new TaskScheduleGraph(name);
            while ((line = reader.readLine()) != null) {
                Matcher nodeMatcher = NODE_REGEX.matcher(line);
                Matcher edgeMatcher = EDGE_REGEX.matcher(line);
                if (edgeMatcher.find()) {
                    String parent = edgeMatcher.group(1);
                    String child = edgeMatcher.group(2);
                    int weight = Integer.parseInt(edgeMatcher.group(3));
                    graph.addEdge(parent, child, weight);
                } else if (nodeMatcher.find()) {
                    String label = nodeMatcher.group(1);
                    int weight = Integer.parseInt(nodeMatcher.group(2));
                    graph.addNode(label, weight);
                } else if (line.equals("}")) {
                    break;
                }
            }
            graph.build();
            return graph;
        }
    }

    /**
     * Generates the .dot file output from TaskGraph object.
     * This method will take the computed schedule object and generate the
     * corresponding .dot output file.
     *
     * @param taskGraph calculated schedule object
     * @throws IOException
     */
    public void generateOutput(TaskGraph taskGraph, String outputPath) throws IOException {
        BufferedWriter bufferedWriter = null;

        try {
            File file = new File(outputPath);
            if (!file.exists()) {
                file.createNewFile();
            }

            bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write("digraph \"" + taskGraph.getName() + "\" {");

            Queue<Node> nodeQueue = new LinkedList<>();
            Map<Node, Integer> children;
            Set<Node> printed = new HashSet<>();

//            nodeQueue.add(taskGraph.getNode("0"));
            nodeQueue.addAll(taskGraph.getRootNode().getChildren().keySet());
            while (nodeQueue.size() > 0) {
                Node node = nodeQueue.poll();

                if (!printed.contains(node)) {
                    bufferedWriter.newLine();
                    bufferedWriter.write(formatNode(node));
                    printed.add(node);
                }

                children = node.getChildren();
                for (Node child : children.keySet()) {

                    if (!printed.contains(child)) {
                        bufferedWriter.newLine();
                        bufferedWriter.write(formatNode(child));
                        printed.add(child);
                    }

                    bufferedWriter.newLine();
                    bufferedWriter.write(formatLink(node, child, children.get(child)));

                    if (child.getChildren().size() > 0) {
                        nodeQueue.add(child);
                    }
                }
            }
            bufferedWriter.newLine();
            bufferedWriter.write("}");
        } catch (ScheduleException e) {
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        }
    }

    /**
     * This method formats the string for printing a node.
     * @param node the node which requires formatting for output
     * @return formatted output string for the supplied node
     */
    private String formatNode(Node node) throws ScheduleException {
        return "\t" + node.toString() +
                "\t[Weight=" + node.getValue() +
                ", Start=" + Schedule.getStartTime(node) +
                ", Processor=" + Schedule.getProcessor(node) + "];";
    }


    /**
     * This method formats the string for printing a link.
     * @param parent the parent node of the link
     * @param child the child node of the link
     * @param cost the cost of the link from parent to child
     * @return formatted output string for supplied link
     */
    private String formatLink(Node parent, Node child, Integer cost) {
        return "\t" + parent.toString() + " -> " + child.toString() +
                "\t [Weight=" + cost + "];";

    }
}

