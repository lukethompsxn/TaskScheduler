package se306.a1.scheduler;

import org.apache.commons.cli.ParseException;
import se306.a1.scheduler.algorithm.BasicScheduler;
import se306.a1.scheduler.data.Graph;
import se306.a1.scheduler.data.Node;
import se306.a1.scheduler.util.CLIParser;
import se306.a1.scheduler.util.GraphParseException;
import se306.a1.scheduler.util.GraphParser;
import se306.a1.scheduler.util.InputConfig;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        GraphParser p = new GraphParser();
        try {
            InputConfig config = CLIParser.getCLIParserInst().parseCLI(args);

            Graph g = p.parseGraph(config.inputPath);
            new BasicScheduler().init(g, config.processors, config.cores);

            System.out.println(g.getName());
            for (Node n : g.getEntryNodes()) {
                System.out.println(n);
            }

//            p.generateOutput(g, config.outputPath);

        } catch (IOException | GraphParseException | ParseException e) {
            e.printStackTrace();
        }
    }
}
