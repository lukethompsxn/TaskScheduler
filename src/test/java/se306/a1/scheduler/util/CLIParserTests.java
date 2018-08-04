package se306.a1.scheduler.util;

import org.apache.commons.cli.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.fail;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class CLIParserTests {

    @BeforeClass
    public static void setUp() {

    }

    @Test
    public void onlyRequiredInputs() {
        String[] testArgs = {"test.dot", "4"};

        try {
            InputConfig config = CLIParser.getCLIParserInst().parseCLI(testArgs);
            assertEquals(4, config.processors);
            assertEquals("test.dot", config.inputPath);
            assertEquals("test-output.dot", config.outputPath);
            assertEquals(-1, config.cores);
            assertEquals(false, config.isParallel);
            assertEquals(false, config.isVisualised);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (CLIException e) {
            fail();
        }
    }

    @Test
    public void missingRequiredInputs1() {
        String[] testArgs = {"test.dot"};

        try {
            InputConfig config = CLIParser.getCLIParserInst().parseCLI(testArgs);
            fail();
        } catch (ParseException e) {

        } catch (CLIException e) {
            return;
        }
    }

    @Test
    public void missingRequiredInputs2() {
        String[] testArgs = {"4"};

        try {
            CLIParser.getCLIParserInst().parseCLI(testArgs);
            fail();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (CLIException e) {
            return;
        }
    }

    @Test
    public void withExtraInputs() {
        String[] testArgs = {"test.dot", "3", "-v", "-p", "4"};

        try {
            InputConfig config = CLIParser.getCLIParserInst().parseCLI(testArgs);
            assertEquals(3, config.processors);
            assertEquals("test.dot", config.inputPath);
            assertEquals("test-output.dot", config.outputPath);
            assertEquals(4, config.cores);
            assertTrue(config.isParallel);
            assertTrue(config.isVisualised);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (CLIException e) {
            fail();
        }
    }

    @Test
    public void optionsWithUnneededValues() {
        String[] testArgs = {"test.dot", "3", "-v", "3", "-p", "4"};

        try {
            CLIParser.getCLIParserInst().parseCLI(testArgs);
            fail();
        } catch (ParseException ignored) {
        } catch (CLIException e) {
        }
    }

    @Test
    public void unknownExtraInput() {
        String[] testArgs = {"test.dot", "3", "-d"};

        try {
            CLIParser.getCLIParserInst().parseCLI(testArgs);
        } catch (ParseException ignored) {
        } catch (CLIException ignored) {
        }
    }

}
