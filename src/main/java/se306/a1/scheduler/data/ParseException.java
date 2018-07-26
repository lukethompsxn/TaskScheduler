package se306.a1.scheduler.data;

public class ParseException extends Exception {
    public ParseException(String line) {
        super("Could not parse line \"" + line + "\"");
    }
}
