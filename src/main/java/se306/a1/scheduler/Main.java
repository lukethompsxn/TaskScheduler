package se306.a1.scheduler;

import javafx.application.Application;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se306.a1.scheduler.algorithm.AStarByteScheduler;
import se306.a1.scheduler.data.graph.Graph;
import se306.a1.scheduler.data.schedule.Schedule;
import se306.a1.scheduler.util.InputConfig;
import se306.a1.scheduler.util.exception.CLIException;
import se306.a1.scheduler.util.exception.GraphException;
import se306.a1.scheduler.util.exception.ScheduleException;
import se306.a1.scheduler.util.parse.CLIParser;
import se306.a1.scheduler.util.parse.GraphParser;
import se306.a1.scheduler.visualisation.controller.GUILauncher;

import java.io.FileNotFoundException;
import java.io.IOException;


public class Main {

    private static InputConfig config;
    private static Logger logger = LogManager.getLogger(Main.class.getSimpleName());

    public static void main(String[] args) {
        try {
            config = CLIParser.getCLIParserInst().parseCLI(args);

            logger.info(config.inputPath);
            logger.info(config.outputPath);

            if (config.isVisualised) {
                Application.launch(GUILauncher.class);
            } else {
                run();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (CLIException ce) {
            System.out.println(ce.getMessage());
            CLIParser.getCLIParserInst().printHelp();
        }

    }

    /**
     * This method is used to execute the algorithm.
     */
    public static void run() {
        try {

            Graph graph = GraphParser.parse(config.inputPath);
            long s = System.nanoTime();
            Schedule schedule = new AStarByteScheduler().run(graph,config.processors, config.cores, config.isVisualised);
            logger.info((System.nanoTime() - s) / 1000);

            logger.info("Length: " + schedule.getLength());

            GraphParser.generateOutput(schedule, graph, config.outputPath);
        } catch (FileNotFoundException fe) {
            System.out.println("File not found");
            CLIParser.getCLIParserInst().printHelp();
        } catch (ScheduleException | IOException e) {
            e.printStackTrace();
        } catch (GraphException ge) {
            System.out.println(ge.getMessage());
        }
    }
}

