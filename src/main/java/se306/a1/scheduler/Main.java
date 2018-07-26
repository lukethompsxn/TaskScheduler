package se306.a1.scheduler;

import se306.a1.scheduler.data.GraphParser;
import se306.a1.scheduler.data.ParseException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        GraphParser p = new GraphParser();
        try {
            p.parseGraph("input_graphs/Nodes_10_Random.dot");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
