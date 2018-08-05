package se306.a1.scheduler.util;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

/**
 * This class deals with the command line inputs.
 * The class contains methods that utilises Apache Commons CLI which reads in
 * and formats command line inputs.
 *
 * @author Joel Clarke
 */
public class CLIParser {

    // Logger for runtime logging
    private static Logger logger = LogManager.getLogger(CLIParser.class.getSimpleName());

    // Singleton instance
    private static final CLIParser cliInstance = new CLIParser();

    // Defines the radix (number system) to be used
    private static final int DEFAULT_RADIX = 10;

    private Options options = new Options();
    private HelpFormatter help = new HelpFormatter();

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
     *
     * @param args Input args given by the command line
     * @return config object holding the parsed inputs of the application
     * @throws ParseException when unable to parse inputs
     * @throws CLIException   if command line arguments are not specified correctly
     */
    public InputConfig parseCLI(String[] args) throws ParseException, CLIException {
        InputConfig config = new InputConfig();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // If help option
        if (cmd.hasOption("h") || (cmd.hasOption("help") || cmd.getArgs().length == 0)) {
            printHelp();
            System.exit(1);
        }

        // Checks for file name and number of processors
        if (cmd.getArgs().length != 2) {
            logger.error("Unknown unnamed inputs found...");
            throw new CLIException("Unknown unnamed inputs found...");
        } else {
            config.inputPath = cmd.getArgs()[0];
            logger.info("Specified filename: " + config.inputPath);

            // Need to check number of processors specified
            if (isInteger(cmd.getArgs()[1])) {
                config.processors = Integer.parseInt(cmd.getArgs()[1]);
                logger.info("Specified number of processors: " + config.processors);
            } else {
                logger.error("Number of processors is not an integer");
                throw new CLIException("Number of processors is not an integer");
            }
        }

        // Checks if parallel option is set
        if (cmd.hasOption("p")) {
            config.isParallel = true;
            String stringCores = cmd.getOptionValue("p");

            if (isInteger(stringCores)) {
                config.cores = Integer.parseInt(stringCores);
                logger.info("Has specified parallel option: number of cores = " + config.cores);
            } else {
                logger.error("Number of cores is not an integer");
                throw new CLIException("Number of cores is not an integer");
            }
        }

        // Checks if visualization is to be displayed
        if (cmd.hasOption("v")) {
            config.isVisualised = true;
            logger.info("Has visualise option: true");
        }

        // Checks if output filename is specified
        if (cmd.hasOption("o")) {
            config.outputPath = cmd.getOptionValue("o");

            if (config.outputPath.matches(".*[:*?\"<>|].*") || config.outputPath.trim().length() == 0)
                throw new CLIException("Invalid characters in output path");

            if (!config.outputPath.endsWith(".dot")) config.outputPath += ".dot";
            logger.info("Custom output file desired: " + config.outputPath);
        } else {
            if (!config.inputPath.endsWith(".dot"))
                throw new CLIException("Input file must be in INPUT.dot format");

            config.outputPath = config.inputPath.substring(0, config.inputPath.length() - 4) + "-output.dot";
            logger.info("No custom output path specified, output path set to: " + config.outputPath);
        }

        return config;
    }

    /**
     * This method creates each command line option utilising the Apache Commons CLI
     * Option object. These are then added to the instance variable 'options',
     * another Apache Commons CLI object which holds the Option objects.
     */
    private void addOptions() {

        // Creates option to specify number of cores
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

        // Creates option to specify output filename
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
     * -h or --help as an option. The method is also called when an
     * incorrect input is received.
     */
    public void printHelp() {
        help.printHelp("java -jar scheduler.jar INPUT.dot P [OPTION]",
                "  INPUT.dot      A task graph with integer weights in dot format" +
                        "\n  P              Number of processors to schedule the INPUT graph on\n ",
                options, "");
    }


    /**
     * This is a helper method which takes in an input of type String and returns
     * true or false based on whether or not it is an integer. The method uses the
     * default radix of decimal (10).
     *
     * @param potentialNum String which will be tested to see if it is a number
     * @return boolean if the input is a number
     */
    private static boolean isInteger(String potentialNum) {
        Scanner sc = new Scanner(potentialNum.trim());
        if (!sc.hasNextInt(DEFAULT_RADIX)) return false;
        // Know it starts with a valid int, check rest
        sc.nextInt(DEFAULT_RADIX);
        return !sc.hasNext();
    }
}