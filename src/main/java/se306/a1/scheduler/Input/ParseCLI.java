package se306.a1.scheduler.Input;

import org.apache.commons.cli.*;

/**
 * This class deals with the command line inputs.
 * The class contains methods that utilises Apache Commons CLI which reads in
 * and formats command line inputs.
 * @author Joel Clarke
 **/
public class ParseCLI {

    private Options options = new Options();
    private HelpFormatter help = new HelpFormatter();

    /**
     * Main method which carries out the parsing of the command line inputs.
     * Takes in the input args a string array containing the inputs, the method
     * then checks what inputs are present and whether or not they are in
     * the correct format.
     * @param args
     * @throws ParseException
     */
    public void parseCLI(String[] args) throws ParseException {
        //Options options = new Options();
        //HelpFormatter help = new HelpFormatter();

        // Calls method which creates the CLI options
        addOptions();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // Checks if help is wanted to be displayed
        if(cmd.hasOption("h") || (cmd.hasOption("help"))) {
            printHelp();
        }

        // Checks if parallel option is set
        if (cmd.hasOption("p")) {
            // set parametrised variable to true
            System.out.println("Has specified parallel option...");
        }

        // Checks if if visualization wants to be displayed
        if(cmd.hasOption("v")) {
            // set visualise variable to true
            System.out.println("Has visualise option...");
        }

        // Checks for file name, and there is only two unnamed CLI (file name + num processors)
        if (cmd.getArgs().length > 2) {
            // Too many unnamed args
            System.out.println("Unknown unnamed inputs found...");
            printHelp();
        } else if (cmd.getArgs().length == 2)  {
            String potentialFile = cmd.getArgs()[0];
            System.out.println(potentialFile);

            // Need to check 2nd unnamed input is integer
        } else {
            // Does not have required args
            System.out.println("Required inputs not found...");
            printHelp();
        }

        //help.printHelp("CLI-Test", "Header for CLI test: ", options, "Footer for CLI test...");
    }

    /**
     * This method creates each command line option utilising the Apache Commons CLI
     * Option object. These are then added to the instance variable 'options,'
     * another Apache Commons CLI object which holds the Option objects.
     */
    private void addOptions() {
        OptionGroup group = new OptionGroup();

//        Option file = Option.builder("f")
//                .argName("INPUT.dot")
//                .desc("A task graph with integer weights in dot format.")
//                .hasArg(true)
//                .numberOfArgs(1)
//                .required(true)
//                .build();

        // Create selection of cores options
        Option cores = Option.builder("p")
                .argName("N")
                .desc("Use N cores for execution in parallel.")
                .hasArg(true)
                .numberOfArgs(1)
                .required(true)
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

        //group.addOption(file).addOption(cores).addOption(visualise).addOption(outputFile);
        //group.addOption(cores).addOption(visualise).addOption(outputFile);
        options.addOption(outputFile).addOption(visualise).addOption(cores);

        options.addOptionGroup(group);
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

}