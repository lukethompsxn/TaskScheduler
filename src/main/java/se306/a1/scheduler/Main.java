package se306.a1.scheduler;

import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se306.a1.scheduler.algorithm.BasicScheduler;
import se306.a1.scheduler.data.Graph;
import se306.a1.scheduler.data.Schedule;
import se306.a1.scheduler.util.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args)  {
        try {
            Logger logger = LogManager.getLogger(Main.class.getSimpleName());
            InputConfig config = CLIParser.getCLIParserInst().parseCLI(args);
            logger.info(config.inputPath);
            logger.info(config.outputPath);
            Graph graph = GraphParser.parse(config.inputPath);
            Schedule s = new BasicScheduler().run(graph, config.processors, config.cores);
            logger.info("Length: " + s.getLength());

            GraphParser.generateOutput(s, graph, config.outputPath);

        } catch (ParseException | IOException | GraphException e) {
            e.printStackTrace();
        } catch (CLIException ignored) { }
    }
}
