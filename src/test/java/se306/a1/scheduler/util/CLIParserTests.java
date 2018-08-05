package se306.a1.scheduler.util;

import org.apache.commons.cli.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.fail;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static se306.a1.scheduler.util.CLIParser.getCLIParserInst;

public class CLIParserTests {

    @BeforeClass
    public static void setUp() {

    }

    @Test
    public void testOnlyRequiredInputs() {
        String[] testArgs = {"test.dot", "4"};

        try {
            InputConfig config = getCLIParserInst().parseCLI(testArgs);
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
            InputConfig config = getCLIParserInst().parseCLI(testArgs);
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
            getCLIParserInst().parseCLI(testArgs);
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
            InputConfig config = getCLIParserInst().parseCLI(testArgs);
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
            getCLIParserInst().parseCLI(testArgs);
            fail();
        } catch (ParseException ignored) {
        } catch (CLIException e) {
        }
    }

    @Test
    public void testUnknownExtraInput() {
        String[] testArgs = {"test.dot", "3", "-d"};

        try {
            getCLIParserInst().parseCLI(testArgs);
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
                getCLIParserInst().parseCLI(s);
                fail("Should have thrown CLIException " + s[3]);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (CLIException e) {
                assertTrue(true);
            }
        }
    }

    @Test
    public void testOutputPath() throws ParseException, CLIException {
        String[] s1 = new String[]{"test.dot", "3", "-o",  "/a/b/c/test.dot"};
        String[] s2 = new String[]{"test.dot", "3", "-o",  "/test.dot"};
        String[] s3 = new String[]{"test.dot", "3", "-o",  "test.dot"};
        String[] s4 = new String[]{"test.dot", "3", "-o",  "a/b/c/test.dot"};
        String[] s5 = new String[]{"test.dot", "3", "-o",  "../a/b/c/test.dot"};
        String[] s6 = new String[]{"test.dot", "3", "-o",  "test"};
        String[] s7 = new String[]{"test.dot", "3", "-o",  "test\\abc\\wew.dot"};
        String[] s8 = new String[]{"test.dot", "3", "-o",  "\\test\\wew\\abc.dot"};
        InputConfig config1 = CLIParser.getCLIParserInst().parseCLI(s1);
        InputConfig config2 = CLIParser.getCLIParserInst().parseCLI(s2);
        InputConfig config3 = CLIParser.getCLIParserInst().parseCLI(s3);
        InputConfig config4 = CLIParser.getCLIParserInst().parseCLI(s4);
        InputConfig config5 = CLIParser.getCLIParserInst().parseCLI(s5);
        InputConfig config6 = CLIParser.getCLIParserInst().parseCLI(s6);
        InputConfig config7 = CLIParser.getCLIParserInst().parseCLI(s7);
        InputConfig config8 = CLIParser.getCLIParserInst().parseCLI(s8);
        assertTrue(config1.outputPath.equals("/a/b/c/test.dot"));
        assertTrue(config2.outputPath.equals("/test.dot"));
        assertTrue(config3.outputPath.equals("test.dot"));
        assertTrue(config4.outputPath.equals("a/b/c/test.dot"));
        assertTrue(config5.outputPath.equals("../a/b/c/test.dot"));
        assertTrue(config6.outputPath.equals("test.dot"));
        assertTrue(config7.outputPath.equals("test\\abc\\wew.dot"));
        assertTrue(config8.outputPath.equals("\\test\\wew\\abc.dot"));
    }

}
