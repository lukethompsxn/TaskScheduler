package se306.a1.scheduler;

import se306.a1.scheduler.util.GraphParser;
import se306.a1.scheduler.data.Node;
import se306.a1.scheduler.util.ParseException;
import se306.a1.scheduler.data.TaskGraph;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        GraphParser p = new GraphParser();
        try {
            TaskGraph g = p.parseGraph("example-input-graphs/Nodes_10_Random.dot");
            for (Node n : g.getRootNode().getChildren().keySet()) {
                System.out.println(n);
            }
            p.generateOutput(g, "test1.dot");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
