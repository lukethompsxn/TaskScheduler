package se306.a1.scheduler.util;

/**
 * This is a config class that holds the default settings/values for the application.
 * On parsing of the command line inputs the values are updated so that they can be
 * used for the running of the application.
 * @author Joel Clarke
 */
public class InputConfig {

    String inputFileName = null;
    int numProcessors = 1;
    int numCores = -1;
    boolean isParallel = false;
    boolean doVisualise = false;
    String outPutFile = null;
}
