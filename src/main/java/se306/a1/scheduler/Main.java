package se306.a1.scheduler;

import org.apache.commons.cli.ParseException;
import se306.a1.scheduler.algorithm.BasicScheduler;
import se306.a1.scheduler.data.Graph;
import se306.a1.scheduler.data.Schedule;
import se306.a1.scheduler.util.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            InputConfig config = CLIParser.getCLIParserInst().parseCLI(args);
            System.out.println(config.inputPath);
            System.out.println(config.outputPath);
            Graph g = GraphParser.parse(config.inputPath);
            Schedule s = new BasicScheduler().run(g, config.processors, config.cores);
            System.out.println("Length: " + s.getLength());

            GraphParser.generateOutput(s, g, config.outputPath);

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }
}
