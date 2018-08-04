package se306.a1.scheduler.util;

public class CLIException extends Exception {

    CLIException(String e) {
        System.out.println(e);
        CLIParser.getCLIParserInst().printHelp();
    }

}
