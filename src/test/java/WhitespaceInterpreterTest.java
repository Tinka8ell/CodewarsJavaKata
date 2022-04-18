import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
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
        String test = """
                S S S T\tS S T\tS S S L
                T\tL
                S S S S S T\tT\tS S T\tS T\tL
                T\tL
                S S S S S T\tT\tS T\tT\tS S L
                T\tL
                S S S S S T\tT\tS T\tT\tS S L
                T\tL
                S S S S S T\tT\tS T\tT\tT\tT\tL
                T\tL
                S S S S S T\tS T\tT\tS S L
                T\tL
                S S S S S T\tS S S S S L
                T\tL
                S S S S S T\tT\tT\tS T\tT\tT\tL
                T\tL
                S S S S S T\tT\tS T\tT\tT\tT\tL
                T\tL
                S S S S S T\tT\tT\tS S T\tS L
                T\tL
                S S S S S T\tT\tS T\tT\tS S L
                T\tL
                S S S S S T\tT\tS S T\tS S L
                T\tL
                S S S S S T\tS S S S T\tL
                T\tL
                S S L
                L
                L""";
        /*
         * 000+00+000-push(72)
         * +-00OutC000++00+0+-push(101)
         * +-00OutC000++0++00-push(108)
         * +-00OutC000++0++00-push(108)
         * +-00OutC000++0++++-push(111)
         * +-00OutC000+0++00-push(44)
         * +-00OutC000+00000-push(32)
         * +-00OutC000+++0+++-push(119)
         * +-00OutC000++0++++-push(111)
         * +-00OutC000+++00+0-push(114)
         * +-00OutC000++0++00-push(108)
         * +-00OutC000++00+00-push(100)
         * +-00OutC000+0000+-push(33)
         * +-00OutC--END!
         */
        assertEquals("Hello, world!", WhitespaceInterpreter.execute(test, null));
    }

    @Test
    public void testCatProgram(){
        /*
         * This will output whatever is on it's input ...
         * Hand compiled:
         *    LSS - label
         *    SL - "0"
         *    SS - push
         *    STL - "+1"
         *    TLTS - read character (to "1")
         *    SS - push
         *    STL - "+1"
         *    TTT - retrieve from "1"
         *    LTTTL - test < 0 and jump to end
         *    SS - push
         *    STL - "+1"
         *    TTT - retrieve from "1"
         *    TLSS - output from stack
         *    LSL - jump
         *    SL - "0"
         *    LSS - label
         *    TL - "1"
         *    LLL - end
         *
         */
        String test = """
                L
                S S S L
                S S S T\tL
                T\tL
                T\tS S S S T\tL
                T\tT\tT\tL
                T\tT\tT\tL
                S S S T\tL
                T\tT\tT\tT\tL
                S S L
                S L
                S L
                L
                S S T\tL
                L
                L
                L
                """;
        String expected = "Input as expected!";
        InputStream input = new ByteArrayInputStream(expected.getBytes());
        assertEquals(expected, WhitespaceInterpreter.execute(test, input));
    }

    @Test
    public void testTruthMachine(){
        String test = """
                S S S L
                S L
                S T\tL
                T\tT\tT\tT\tT\tL
                T\tS S L
                L
                S S T\tL
                S S S T\tL
                T\tL
                S T\tL
                S L
                T\tL
                L
                S S S L
                S S S L
                T\tL
                S T\tL
                L
                L
                """;
        String expected = "0"; // any '1's will create continuous '1's, otherwise terminate at 0
        InputStream input = new ByteArrayInputStream(expected.getBytes());
        assertEquals(expected, WhitespaceInterpreter.execute(test, input));
    }

    @Test
    public void testOneToTen(){
        // Here is a program which counts from 1 to 10, outputting the current value as it goes.
        // The logic:
        //   Put a 1 on the stack
        //   Set a Label at this point
        //   Duplicate the top stack item
        //   Output the current value
        //   Put 10 (newline) on the stack...
        //   ...and output the newline
        //   Put a 1 on the stack
        //   Addition. This increments our current value.
        //   Duplicate that value so we can test it
        //   Push 11 onto the stack
        //   Subtraction. So if we've reached the end, we have a zero on the stack.
        //   If we have a zero, jump to the end
        //   Jump to the start
        //   Set the end label
        //   Discard our accumulator, to be tidy
        String test = """
                S S S T\tL
                L
                S S S T\tS S  S S T\tT\tL
                S L
                S T\tL
                S T\tS S S T\tS T\tS L
                T\tL
                S S S S S T\tL
                T\tS S S S L
                S S S S T\tS T\tT\tL
                T\tS S T\tL
                T\tS S T\tS S  S T\tS T\tL
                L
                S L
                S T\tS  S S S T\tT\tL
                L
                S S S T\tS  S S T\tS T\tL
                S L
                L
                L
                L
                L
                """; //Finish
        assertEquals("1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n", WhitespaceInterpreter.execute(test, null));
    }

}