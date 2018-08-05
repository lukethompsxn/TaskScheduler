package se306.a1.scheduler.util;

import org.apache.commons.cli.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.fail;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class CLIParserTests {

    @BeforeClass
    public static void setUp() {

    }

    @Test
    public void testOnlyRequiredInputs() {
        String[] testArgs = {"test.dot", "4"};

        try {
            InputConfig config = CLIParser.getCLIParserInst().parseCLI(testArgs);
            assertEquals(4, config.processors);
            assertEquals("test.dot", config.inputPath);
            assertEquals("test-output.dot", config.outputPath);
            assertEquals(1, config.cores);
            assertEquals(false, config.isParallel);
            assertEquals(false, config.isVisualised);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (CLIException e) {
            fail();
        }
    }

    @Test
    public void testMissingRequiredInputs1() {
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
    public void testMissingRequiredInputs2() {
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
    public void testWithExtraInputs() {
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
    public void testOptionsWithUnneededValues() {
        String[] testArgs = {"test.dot", "3", "-v", "3", "-p", "4"};

        try {
            CLIParser.getCLIParserInst().parseCLI(testArgs);
            fail();
        } catch (ParseException ignored) {
        } catch (CLIException e) {
        }
    }

    @Test
    public void testUnknownExtraInput() {
        String[] testArgs = {"test.dot", "3", "-d"};

        try {
            CLIParser.getCLIParserInst().parseCLI(testArgs);
        } catch (ParseException ignored) {
        } catch (CLIException ignored) {
        }
    }

    @Test
    public void testInvalidOutputPath() {
        List<String[]> testArgs = new ArrayList<>();
        testArgs.add(new String[]{"test.dot", "3", "-o", ""});
        testArgs.add(new String[]{"test.dot", "3", "-o", "27$9*&@()*&#"});
        testArgs.add(new String[]{"test.dot", "3", "-o", " "});
        testArgs.add(new String[]{"test.dot", "3", "-o", " * a * "});

        for (String[] s : testArgs) {
            try {
                CLIParser.getCLIParserInst().parseCLI(s);
                fail("Should have thrown CLIException " + s[3]);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (CLIException e) {
                assertTrue(true);
            }
        }
    }

}
