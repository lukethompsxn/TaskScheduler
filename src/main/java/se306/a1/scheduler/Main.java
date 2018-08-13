package se306.a1.scheduler;

import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se306.a1.scheduler.algorithm.AStarByteScheduler;
import se306.a1.scheduler.algorithm.AStarScheduler;
import se306.a1.scheduler.algorithm.BasicScheduler;
import se306.a1.scheduler.data.graph.Graph;
import se306.a1.scheduler.data.schedule.Schedule;
import se306.a1.scheduler.util.*;
import se306.a1.scheduler.util.exception.CLIException;
import se306.a1.scheduler.util.exception.GraphException;
import se306.a1.scheduler.util.exception.ScheduleException;
import se306.a1.scheduler.util.parse.CLIParser;
import se306.a1.scheduler.util.parse.GraphParser;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Logger logger = LogManager.getLogger(Main.class.getSimpleName());
            InputConfig config = CLIParser.getCLIParserInst().parseCLI(args);

            logger.info(config.inputPath);
            logger.info(config.outputPath);

            Graph graph = GraphParser.parse(config.inputPath);
            Schedule schedule = new AStarByteScheduler().run(graph, config.processors, config.cores, config.isVisualised);

            logger.info("Length: " + schedule.getLength());

            GraphParser.generateOutput(schedule, graph, config.outputPath);
        } catch (FileNotFoundException fe) {
            System.out.println("File not found");
            CLIParser.getCLIParserInst().printHelp();
        } catch (ParseException | ScheduleException | IOException e) {
            e.printStackTrace();
        } catch (GraphException ge) {
            System.out.println(ge.getMessage());
        } catch (CLIException ce) {
            System.out.println(ce.getMessage());
            CLIParser.getCLIParserInst().printHelp();
        }
    }
}
