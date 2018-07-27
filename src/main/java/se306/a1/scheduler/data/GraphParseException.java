package se306.a1.scheduler.data;

public class GraphParseException extends Exception {
    public GraphParseException(String line) {
        super("Could not parse line \"" + line + "\"");
    }
}
