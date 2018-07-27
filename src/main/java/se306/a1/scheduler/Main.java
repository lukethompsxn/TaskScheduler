package se306.a1.scheduler;

import org.apache.commons.cli.ParseException;
import se306.a1.scheduler.util.GraphParser;
import se306.a1.scheduler.data.Node;
import se306.a1.scheduler.data.TaskGraph;
import se306.a1.scheduler.util.CLIParser;
import se306.a1.scheduler.util.GraphParseException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        GraphParser p = new GraphParser();
//        CLIParser cli = new CLIParser();

        try {
//            cli.parseCLI(args);

            TaskGraph g = p.parseGraph("example-input-graphs/Nodes_10_Random.dot");
            System.out.println(g.getName());
            for (Node n : g.getRootNode().getChildren().keySet()) {
                System.out.println(n);
            }

            p.generateOutput(g, "test1.dot");

        } catch (IOException | GraphParseException e) {
            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
        }
    }
}
