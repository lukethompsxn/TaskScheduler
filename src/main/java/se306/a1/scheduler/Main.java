package se306.a1.scheduler;

import se306.a1.scheduler.data.Graph;
import se306.a1.scheduler.data.Node;
import se306.a1.scheduler.util.GraphParseException;
import se306.a1.scheduler.util.GraphParser;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        GraphParser p = new GraphParser();

        try {
//            InputConfig config = CLIParser.getCLIParserInst().parseCLI(args);

            Graph g = p.parseGraph("input_graphs/Nodes_10_Random.dot");

            System.out.println(g.getName());
            for (Node n : g.getEntryNodes()) {
                System.out.println(n);
            }

            p.generateOutput(g, "test1.dot");

        } catch (IOException | GraphParseException e) {
            e.printStackTrace();
        } //catch (ParseException e) {
//            e.printStackTrace();
//        }
    }
}
