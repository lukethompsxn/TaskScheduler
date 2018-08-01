package se306.a1.scheduler.util;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import java.util.Scanner;
import org.apache.logging.log4j.*;

/**
 * This class deals with the command line inputs.
 * The class contains methods that utilises Apache Commons CLI which reads in
 * and formats command line inputs.
 * @author Joel Clarke
 **/
public class CLIParser {

    private static Logger log = LogManager.getLogger(CLIParser.class);

    private static final CLIParser cliInstance = new CLIParser();

    public static final int DEFAULT_RADIX = 10;

    private Options options = new Options();
    private HelpFormatter help = new HelpFormatter();
    private InputConfig config = new InputConfig();

//    private String inputPath;
//    private int processors = 1;
//    private int cores;
//    private boolean isParallel;
//    private boolean isVisualised;
//    private String outPutFile;

    private CLIParser() {

    }

    public static CLIParser getCLIParserInst() {
        return cliInstance;
    }

    /**
     * Main method which carries out the parsing of the command line inputs.
     * Takes in the input args a string array containing the inputs, the method
     * then checks what inputs are present and whether or not they are in
     * the correct format.
     * @param args
     * @throws ParseException
     */
    public InputConfig parseCLI(String[] args) throws ParseException {

        // Calls method which creates the CLI options
        addOptions();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // Checks if help is wanted to be displayed
        if(cmd.hasOption("h") || (cmd.hasOption("help"))) {
            printHelp();
            System.exit(1);
        }

        // Checks if parallel option is set
        if (cmd.hasOption("p")) {
            // set parametrised variable to true

            //isParallel = true;
            config.isParallel = true;
            String stringCores = cmd.getOptionValue("p");

            // Need to check is valid int
            if (isInteger(stringCores)) {
                int cores = Integer.parseInt(cmd.getArgs()[1]);
                //cores = cores;
                config.cores = cores;
                //System.out.println("Has specified parallel option: number of cores = " + config.cores);
                log.info("Has specified parallel option: number of cores = " + config.cores);
            } else {
                // p value is not and integer
                //System.out.println("P is not an integer");
                log.error("p is not an integer");
                printHelp();
                System.exit(1);
            }
        }

        // Checks if visualization wants to be displayed
        if(cmd.hasOption("v")) {
            // set visualise variable to true
            //isVisualised = true;
            config.isVisualised = true;
            //System.out.println("Has visualise option: " + config.isVisualised);
            log.info("Has visualise option: " + config.isVisualised);
        }

        // Checks if custom output file is desired
        if (cmd.hasOption("o")) {
            //outPutFile = cmd.getOptionValue("o");
            config.outputPath = cmd.getOptionValue("o");
            //System.out.println("Custom output file desired:" + " " + config.outputPath);
            log.info("Custom output file desired:" + " " + config.outputPath);
        }

        // Checks for file name, and there is only two unnamed CLI (file name + num processors)
        if (cmd.getArgs().length > 2) { // Has too many unnamed inputs
            // Too many unnamed args
            //System.out.println("Unknown unnamed inputs found...");
            log.error("Unknown unnamed inputs found...");
            printHelp();
            System.exit(1);
        } else if (cmd.getArgs().length == 2)  { // Has correct number of unnamed inputs
            //inputPath = cmd.getArgs()[0];
            config.inputPath = cmd.getArgs()[0];
            //System.out.println("Specified filename: " + config.inputPath);
            log.info("Specified filename: " + config.inputPath);

            // Need to check 2nd unnamed input is integer
            if (isInteger(cmd.getArgs()[1])) {
                //processors = Integer.parseInt(cmd.getArgs()[1]);
                config.processors = Integer.parseInt(cmd.getArgs()[1]);
                //System.out.println("Specified number of processors: " + config.processors);
                log.info("Specified number of processors: " + config.processors);
            } else {
                // P is not and integer
                //System.out.println("P is not an integer");
                log.error("P is not an integer");
                printHelp();
                System.exit(1);
            }
        } else { // Does not have required unnamed inputs
            //System.out.println("Required inputs not found...");
            log.error("Required inputs not found...");
            printHelp();
            System.exit(1);
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
    private void printHelp() {
        help.printHelp("java -jar scheduler.jar INPUT.dot P [OPTION]",
                "  INPUT.dot      A task graph with integer weights in dot format" +
                        "\n  P              Number of processors to schedule the INPUT graph on\n ",
                options, "Footer for CLI test...");
    }


    /**
     * This is a helper method which takes in a input of type String and returns
     * true or false based on whether or not it is an integer. The method also
     * takes a second input radix, this is what base of numbering system should
     * be referenced.
     * @param potentialNum
     * @return boolean
     */
    private static boolean isInteger(String potentialNum) {
        Scanner sc = new Scanner(potentialNum.trim());
        if(!sc.hasNextInt(DEFAULT_RADIX)) return false;
        // Know it starts with a valid int, check rest
        sc.nextInt(DEFAULT_RADIX);
        return !sc.hasNext();
    }

//    public boolean isDoVisualise() {
//        return isVisualised;
//    }
//
//    public boolean isParallel() {
//        return isParallel;
//    }
//
//    public int getNumCores() {
//        return cores;
//    }
//
//    public int getNumProcessors() {
//        return processors;
//    }
//
//    public String getInputFileName() {
//        return inputPath;
//    }
//
//    public String getOutPutFile() {
//        return outPutFile;
//    }
}