package se306.a1.scheduler.util;

/**
 * This exception class represents as exception thrown due to an error in
 * parsing the input of a .dot file to a TaskGraph.
 */
public class ParseException extends Exception {
    public ParseException(String line) {
        super("Could not parse line \"" + line + "\"");
    }
}
