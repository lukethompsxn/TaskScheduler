package se306.a1.scheduler;

import org.apache.commons.cli.ParseException;
import se306.a1.scheduler.data.Edge;
import se306.a1.scheduler.data.Graph;
import se306.a1.scheduler.util.CLIParser;
import se306.a1.scheduler.util.GraphParseException;
import se306.a1.scheduler.util.GraphParser;
import se306.a1.scheduler.util.InputConfig;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        GraphParser p = new GraphParser();

        try {
//            InputConfig config = CLIParser.getCLIParserInst().parseCLI(args);

            Graph g = p.parseGraph("input_graphs/Nodes_10_Random.dot");
            System.out.println(g.getName());
            for (Edge e : g.getLinks(g.getRootNode())) {
                System.out.println(e.getParent() + "->" + e.getChild());
            }

            p.generateOutput(g, "test1.dot");

        } catch (IOException | GraphParseException e) {
            e.printStackTrace();
        } //catch (ParseException e) {
//            e.printStackTrace();
//        }
    }
}
