package se306.a1.scheduler.util;

/**
 * This is a config class that holds the default settings/values for the application.
 * On parsing of the command line inputs the values are updated so that they can be
 * used for the running of the application.
 *
 * @author Joel Clarke
 */
public class InputConfig {
    public String inputPath = null;
    public int processors = 1;
    public int cores = 1;
    public boolean isParallel = false;
    public boolean isVisualised = false;
    public String outputPath = null;
}
