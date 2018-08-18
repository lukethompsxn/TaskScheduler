package se306.a1.scheduler;

import javafx.application.Application;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scala.App;
import se306.a1.scheduler.algorithm.AStarByteScheduler;
import se306.a1.scheduler.data.graph.Graph;
import se306.a1.scheduler.data.schedule.Schedule;
import se306.a1.scheduler.util.InputConfig;
import se306.a1.scheduler.util.exception.CLIException;
import se306.a1.scheduler.util.exception.GraphException;
import se306.a1.scheduler.util.exception.ScheduleException;
import se306.a1.scheduler.util.parse.CLIParser;
import se306.a1.scheduler.util.parse.GraphParser;
import se306.a1.scheduler.visualisation.GUILauncher;

import java.io.FileNotFoundException;
import java.io.IOException;


public class Main {

    private static String[] _args;
    private static InputConfig _config;
    private static Logger logger = LogManager.getLogger(Main.class.getSimpleName());

    public static void main(String[] args) {
        try {
            _args = args;
            _config = CLIParser.getCLIParserInst().parseCLI(_args);

            logger.info(_config.inputPath);
            logger.info(_config.outputPath);

            if (_config.isVisualised) {
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

    public static void run() {
        try {

            Graph graph = GraphParser.parse(_config.inputPath);
            long s = System.nanoTime();
            Schedule schedule = new AStarByteScheduler().run(graph,_config.processors, _config.cores, _config.isVisualised);
            logger.info((System.nanoTime() - s) / 1000);

            logger.info("Length: " + schedule.getLength());

            GraphParser.generateOutput(schedule, graph, _config.outputPath);
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

