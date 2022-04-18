import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class WhitespaceInterpreterTest {

    @Test
    public void testPush() {
        System.out.println("Testing push, output of numbers 0 through 3");
        String[][] tests = {
                {"   \t\n\t\n \t\n\n\n", "1"},
                {"   \t \n\t\n \t\n\n\n", "2"},
                {"   \t\t\n\t\n \t\n\n\n", "3"},
                {"    \n\t\n \t\n\n\n", "0"}
        };
        for (String[] test : tests) {
            assertEquals(test[1], WhitespaceInterpreter.execute(test[0], null));
        }
    }

    @Test
    public void testOutNumbers() {
        System.out.println("Testing output of numbers -1 through -3");
        String[][] tests = {
                {"  \t\t\n\t\n \t\n\n\n", "-1"},
                {"  \t\t \n\t\n \t\n\n\n", "-2"},
                {"  \t\t\t\n\t\n \t\n\n\n", "-3"},
        };
        for (String[] test : tests) {
            assertEquals(test[1], WhitespaceInterpreter.execute(test[0], null));
        }
    }

    @Test
    public void testFlowEdge() {
        System.out.println("Testing simple flow control edge case");
        assertThrows(RuntimeException.class, () -> WhitespaceInterpreter.execute("", null));
    }

    @Test
    public void testOutLetters() {
        System.out.println("Testing output of letters A through C");
        String[][] tests = {
                {"   \t     \t\n\t\n  \n\n\n", "A"},
                {"   \t    \t \n\t\n  \n\n\n", "B"},
                {"   \t    \t\t\n\t\n  \n\n\n", "C"},
        };
        for (String[] test : tests) {
            assertEquals(test[1], WhitespaceInterpreter.execute(test[0], null));
        }
    }

    @Test
    public void testOutLettersWithComments() {
        System.out.println("Testing output of letters A through C with comments");
        //noinspection SpellCheckingInspection
        String[][] tests = {
                {"blahhhh   \targgggghhh     \t\n\t\n  \n\n\n", "A"},
                {" I heart \t  cats  \t \n\t\n  \n\n\n", "B"},
                {"   \t  welcome  \t\t\n\t\n to the\nnew\nworld\n", "C"},
        };
        for (String[] test : tests) {
            assertEquals(test[1], WhitespaceInterpreter.execute(test[0], null));
        }
    }

    @Test
    public void testStack() {
        System.out.println("Testing stack functionality");
        String[][] tests = {
                {"   \t\t\n   \t\t\n\t\n \t\t\n \t\n\n\n", "33"},
                {"   \t\t\n \n \t\n \t\t\n \t\n\n\n", "33"},
                {"   \t\n   \t \n   \t\t\n \t  \t \n\t\n \t\n\n\n", "1"},
                {"   \t\n   \t \n   \t\t\n \t  \t\n\t\n \t\n\n\n", "2"},
                {"   \t\n   \t \n   \t\t\n \t   \n\t\n \t\n\n\n", "3"},
                {"   \t\t\n   \t \n \n\t\t\n \t\t\n \t\n\n\n", "32"},
                {"   \t\t\n   \t \n \n\t \n\n\t\n \t\n\n\n", "2"},
                {"   \t\t\n   \t \n   \t\n   \t  \n   \t\t \n   \t \t\n   \t\t\t\n \n\t \t\n \t\t\n\t\n \t\t\n \t\t\n \t\t\n \t\n\n\n", "5123"},
        };
        for (String[] test : tests) {
            assertEquals(test[1], WhitespaceInterpreter.execute(test[0], null));
        }
    }

    @Test
    public void testHelloWorld(){
        String test = "S S S T\tS S T\tS S S L\n" +
                "T\tL\n" +
                "S S S S S T\tT\tS S T\tS T\tL\n" +
                "T\tL\n" +
                "S S S S S T\tT\tS T\tT\tS S L\n" +
                "T\tL\n" +
                "S S S S S T\tT\tS T\tT\tS S L\n" +
                "T\tL\n" +
                "S S S S S T\tT\tS T\tT\tT\tT\tL\n" +
                "T\tL\n" +
                "S S S S S T\tS T\tT\tS S L\n" +
                "T\tL\n" +
                "S S S S S T\tS S S S S L\n" +
                "T\tL\n" +
                "S S S S S T\tT\tT\tS T\tT\tT\tL\n" +
                "T\tL\n" +
                "S S S S S T\tT\tS T\tT\tT\tT\tL\n" +
                "T\tL\n" +
                "S S S S S T\tT\tT\tS S T\tS L\n" +
                "T\tL\n" +
                "S S S S S T\tT\tS T\tT\tS S L\n" +
                "T\tL\n" +
                "S S S S S T\tT\tS S T\tS S L\n" +
                "T\tL\n" +
                "S S S S S T\tS S S S T\tL\n" +
                "T\tL\n" +
                "S S L\n" +
                "L\n" +
                "L";
        assertEquals("Hello, world!", WhitespaceInterpreter.execute(test, null));
    }

    @Test
    public void testCatProgram(){
        /*
         * This will output whatever is on it's input ...
         */
        String test = "L\nS S S L\n" +
                "S S S T\tL\n" +
                "T\tL\nT\tS " +
                "S S S T\tL\n " +
                "T\tT\tT\t" +
                "T\tL\nS S " +
                "S S S T\tL\n" +
                "L\nT\tS T\tL\n" +
                "L\nS L\nS L\n" +
                "L\nS S T\tL\n" +
                "L\nL\nL\n";
        String expected = "Input";
        InputStream input = new ByteArrayInputStream(expected.getBytes());
        assertEquals(expected, WhitespaceInterpreter.execute(test, input));
    }

    @Test
    public void testTruthMachine(){
        String test = "S S S L\n" +
                "S L\nS " +
                "T\tL\nT\tT\t" +
                "T\tT\tT\t" +
                "L\nT\tS S L\n" +
                "L\nS S T\tL\n" +
                "S S S T\tL\n" +
                "T\tL\nS T\t" +
                "L\nS L\nT\tL\n" +
                "L\nS S S L\n" +
                "S S S L\n" +
                "T\tL\nS T\t" +
                "L\nL\nL\n";
        String expected = "111110"; // any number of 1's will be output as is, and terminate at 0
        InputStream input = new ByteArrayInputStream(expected.getBytes());
        assertEquals(expected, WhitespaceInterpreter.execute(test, input));
    }

    @Test
    public void testOneToTen(){
        // Here is an annotated example of a program which counts from 1 to 10, outputting the current value as it goes.
        String test = "S S S T\tL\n" + //Put a 1 on the stack
                "L\nS S S T\tS S  S S T\tT\tL\n" + //Set a Label at this point
                "S L\nS " + //Duplicate the top stack item
                "T\tL\nS T\t" + //Output the current value
                "S S S T\tS T\tS L\n" + //Put 10 (newline) on the stack...
                "T\tL\nS S " + //...and output the newline
                "S S S T\tL\n" + //Put a 1 on the stack
                "T\tS S S " + //Addition. This increments our current value.
                "S L\nS " + //Duplicate that value so we can test it
                "S S S T\tS T\tT\tL\n" + //Push 11 onto the stack
                "T\tS S T\t" + //Subtraction. So if we've reached the end, we have a zero on the stack.
                "L\nT\tS S T\tS S  S T\tS T\tL\n" + //If we have a zero, jump to the end
                "L\nS L\nS T\tS  S S S T\tT\tL\n" + //Jump to the start
                "L\nS S S T\tS  S S T\tS T\tL\n" + //Set the end label
                "S L\nL\n" + //Discard our accumulator, to be tidy
                "L\nL\nL\n"; //Finish
        assertEquals("12345678910", WhitespaceInterpreter.execute(test, null));
    }

}