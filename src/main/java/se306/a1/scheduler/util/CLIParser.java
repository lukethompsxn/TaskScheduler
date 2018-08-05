package se306.a1.scheduler.util;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import java.util.Scanner;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * This class deals with the command line inputs.
 * The class contains methods that utilises Apache Commons CLI which reads in
 * and formats command line inputs.
 * @author Joel Clarke
 **/
public class CLIParser {

    // Logger for runtime logging
    private static Logger logger = LogManager.getLogger(CLIParser.class.getSimpleName());

    // Singleton instance
    private static final CLIParser cliInstance = new CLIParser();

    // Defines the radix (number system) to be used
    private static final int DEFAULT_RADIX = 10;

    private Options options = new Options();
    private HelpFormatter help = new HelpFormatter();
    private InputConfig config = new InputConfig();

    private CLIParser() {
        // Calls method which creates the CLI options
        addOptions();
    }

    // Returns the Singleton instance of CLIParser
    public static CLIParser getCLIParserInst() {
        return cliInstance;
    }

    /**
     * Main method which carries out the parsing of the command line inputs.
     * Takes in the input args a string array containing the inputs, the method
     * then checks what inputs are present and whether or not they are in
     * the correct format.
     * @param args Input args given by the command line
     * @return config object holding the parsed inputs of the application
     * @throws ParseException Exception thrown when unable to parse inputs
     */
    public InputConfig parseCLI(String[] args) throws ParseException, CLIException {
        config = new InputConfig();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // Checks if help is wanted to be displayed
        if(cmd.hasOption("h") || (cmd.hasOption("help") || cmd.getArgs().length == 0)) {
            printHelp();
            System.exit(1);
        }

        // Checks if parallel option is set
        if (cmd.hasOption("p")) {
            // Set parametrised variable to true

            config.isParallel = true;
            String stringCores = cmd.getOptionValue("p");

            // Need to check is valid int
            if (isInteger(stringCores)) {
                config.cores = Integer.parseInt(stringCores);
                logger.info("Has specified parallel option: number of cores = " + config.cores);
            } else {
                // p value is not and integer
                logger.error("p is not an integer");
                throw new CLIException("p is not an integer");
            }
        }

        // Checks if visualization wants to be displayed
        if(cmd.hasOption("v")) {
            // Set visualise variable to true
            config.isVisualised = true;
            logger.info("Has visualise option: true");
        }

        // Checks if custom output file is desired
        if (cmd.hasOption("o")) {
            config.outputPath = cmd.getOptionValue("o");
            if (config.outputPath.matches(".*[\\/:*?\"<>|].*") || config.outputPath.length() == 0 || config.outputPath.trim().length() == 0) {
                throw new CLIException("Invalid characters in output path");
            }
            logger.info("Custom output file desired:" + " " + config.outputPath);
        } else {
            config.outputPath = cmd.getArgs()[0].replaceAll(".dot", "") + "-output.dot";
            logger.info("No custom output path selected, output path set to: " + config.outputPath);
        }

        // Checks for file name, and there is only two unnamed CLI (file name + num processors)
        if (cmd.getArgs().length > 2) { // Has too many unnamed inputs
            // Too many unnamed args
            logger.error("Unknown unnamed inputs found...");
            throw new CLIException("Unknown unnamed inputs found...");

        } else if (cmd.getArgs().length == 2)  { // Has correct number of unnamed inputs
            //inputPath = cmd.getArgs()[0];
            config.inputPath = cmd.getArgs()[0];
            logger.info("Specified filename: " + config.inputPath);

            // Need to check 2nd unnamed input is integer
            if (isInteger(cmd.getArgs()[1])) {
                config.processors = Integer.parseInt(cmd.getArgs()[1]);
                logger.info("Specified number of processors: " + config.processors);
            } else {
                // P is not and integer
                logger.error("P is not an integer");
                throw new CLIException("P is not an integer");
            }

        } else { // Does not have required unnamed inputs
            logger.error("Required inputs not found...");
            throw new CLIException("Required inputs not found...");
        }

        return config;
    }

    /**
     * This method creates each command line option utilising the Apache Commons CLI
     * Option object. These are then added to the instance variable 'options,'
     * another Apache Commons CLI object which holds the Option objects.
     */
    private void addOptions() {

        // Create selection of cores options
        Option cores = Option.builder("p")
                .argName("N")
                .desc("Use N cores for execution in parallel.")
                .hasArg(true)
                .numberOfArgs(1)
                .required(false)
                .build();

        // Creates option to visualise the scheduler
        Option visualise = Option.builder("v")
                .desc("Visualise the search.")
                .hasArg(false)
                .required(false)
                .build();

        // Creates option to select output file
        Option outputFile = Option.builder("o")
                .argName("OUTPUT")
                .desc("Output file is named OUTPUT (default is INPUT-output.dot.)")
                .hasArg(true)
                .numberOfArgs(1)
                .required(false)
                .build();

        // Creates option for printing help
        options.addOption(Option.builder("h").longOpt("help").desc("Displays usage of Java scheduler app.").build());
        options.addOption(outputFile).addOption(visualise).addOption(cores);
    }

    /**
     * This method generates the desired output to print when help is
     * needed regarding running the app on the command line. Usage requires
     * -h or --help when running the app. The method is also called when an
     * incorrect input is received.
     */
    public void printHelp() {
        help.printHelp("java -jar scheduler.jar INPUT.dot P [OPTION]",
                "  INPUT.dot      A task graph with integer weights in dot format" +
                        "\n  P              Number of processors to schedule the INPUT graph on\n ",
                options, "");
    }


    /**
     * This is a helper method which takes in a input of type String and returns
     * true or false based on whether or not it is an integer. The method also
     * takes a second input radix, this is what base of numbering system should
     * be referenced.
     * @param potentialNum String which will be tested to see if it is a number
     * @return boolean true/false if the input is a number
     */
    private static boolean isInteger(String potentialNum) {
        Scanner sc = new Scanner(potentialNum.trim());
        if(!sc.hasNextInt(DEFAULT_RADIX)) return false;
        // Know it starts with a valid int, check rest
        sc.nextInt(DEFAULT_RADIX);
        return !sc.hasNext();
    }
}