package se306.a1.scheduler.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
     * @throws ParseException if the graph cannot be parsed
     */
    public TaskGraph parseGraph(String inputPath) throws IOException, ParseException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
            String name = "";
            String line = reader.readLine();
            Matcher nameMatcher = NAME_REGEX.matcher(line);
            if (nameMatcher.find()) {
                name = nameMatcher.group(1);
            } else {
                throw new ParseException(line);
            }
            System.out.println("Name: " + name);
            while ((line = reader.readLine()) != null) {
                Matcher nodeMatcher = NODE_REGEX.matcher(line);
                Matcher edgeMatcher = EDGE_REGEX.matcher(line);
                if (edgeMatcher.find()) {
                    String first = edgeMatcher.group(1);
                    String second = edgeMatcher.group(2);
                    int weight = Integer.parseInt(edgeMatcher.group(3));
                    System.out.println("Edge " + first + "->" + second + " with weight " + weight);
                } else if (nodeMatcher.find()) {
                    String label = nodeMatcher.group(1);
                    int weight = Integer.parseInt(nodeMatcher.group(2));
                    System.out.println("Node " + label + " with weight " + weight);
                } else if (line.equals("}")) {
                    break;
                }
            }
        }
        return null;
    }

    /**
     * Generates the .dot file output from TaskGraph object.
     * This method will take the computed schedule object and generate the
     * corresponding .dot output file.
     *
     * @param taskGraph calculated schedule object
     */
    public void generateOutput(TaskGraph taskGraph) {

    }
}

