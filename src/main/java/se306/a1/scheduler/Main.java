package se306.a1.scheduler;

import se306.a1.scheduler.data.GraphParser;
import se306.a1.scheduler.data.Node;
import se306.a1.scheduler.data.ParseException;
import se306.a1.scheduler.data.TaskGraph;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        GraphParser p = new GraphParser();
        try {
            TaskGraph g = p.parseGraph("input_graphs/Nodes_10_Random.dot");
            System.out.println(g.getName());
            for (Node n : g.getNode("0").getChildren()) {
                System.out.println(n);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
