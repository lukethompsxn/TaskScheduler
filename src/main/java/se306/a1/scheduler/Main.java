package se306.a1.scheduler;

import se306.a1.scheduler.Input.CLIParser;
import se306.a1.scheduler.data.GraphParser;
import se306.a1.scheduler.data.Node;
import se306.a1.scheduler.data.GraphParseException;
import se306.a1.scheduler.data.TaskGraph;
import org.apache.commons.cli.ParseException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        GraphParser p = new GraphParser();
        CLIParser cli = new CLIParser();

        try {
            cli.parseCLI(args);

            TaskGraph g = p.parseGraph("input_graphs/Nodes_10_Random.dot");
            System.out.println(g.getName());
            for (Node n : g.getRootNode().getChildren()) {
                System.out.println(n);
            }
        } catch (IOException | GraphParseException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
