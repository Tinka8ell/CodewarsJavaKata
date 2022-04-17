import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class WhitespaceInterpreter {

    private final StringBuilder debug;
    private StringBuilder output;
    private Stack<Integer> stack;
    private Map<Integer, Integer> heap;
    private final Queue<Integer> codeQueue;
    private boolean executing;

    public WhitespaceInterpreter(String code) {
        codeQueue = toCodes(code);
        debug = new StringBuilder();
    }

    // transforms space characters to ['s','t','n'] chars;
    /*
    public static String unbleach(String code) {
        return code != null ? code.replace(' ', 's').replace('\t', 't').replace('\n', 'n') : null;
    }
     */

    public String execute(InputStream input) {
        output = new StringBuilder();
        stack = new Stack<>();
        heap = new HashMap<>();
        executing = true;
        while (codeQueue.peek() != null) {
            processIMP();
        }
        if (executing) // not finished properly
            throw new RuntimeException("Code ended before execution completed");
        return output.toString();
    }

    private void processIMP() {
        int IMP = readNext();
        switch (IMP){
            case 0 -> // Stack Manipulation
                    processStackManipulation();
            case -1 -> // Flow Control
                    processFlowControl();
            default -> { // 1
                IMP = readNext();
                switch (IMP) {
                    case 0 -> // Arithmetic
                            throw new RuntimeException("Arithmetic not implemented yet");
                    case -1 -> // Input/Output
                            processInputOutput();
                    default -> // 1
                            // Heap Access
                            throw new RuntimeException("Heap Access not implemented yet");
                }
            }
        }
    }

    private void processFlowControl() {
        int cmd = readNext();
        switch (cmd) {
            case 0 -> //* `[space][space]` (label): Mark a location in the program with label n.
                    //* `[space][tab]` (label): Call a subroutine with the location specified by label n.
                    //* `[space][line-feed]` (label): Jump unconditionally to the position specified by label n.
                    throw new RuntimeException("Label definition not implemented number yet");
            case 1 -> //* `[tab][space]` (label): Pop a value off the stack and jump to the label specified by n if the value is zero.
                    //* `[tab][tab]` (label): Pop a value off the stack and jump to the label specified by n if the value is less than zero.
                    //* `[tab][line-feed]`: Exit a subroutine and return control to the location from which the subroutine was called.
                    throw new RuntimeException("Label goto not implemented number yet");
            default -> {
                // Exit the program.
                executing = false;
                debug.append("END");
                codeQueue.clear(); // make sure we stop here
            }
        }
    }

    private Integer readNext() {
        int code = codeQueue.poll();
        debug.append((code == 0) ? '0' : (code > 0) ? '+' : '-');
        return code;
    }

    private void processInputOutput() {
        int cmd = readNext();
        switch (cmd) {
            case 0 -> {
                // Pop and output
                int value = popFromStack();
                int what = readNext();
                switch (what) {
                    case 0 -> {
                        // Pop a value off the stack and output it as a character
                        debug.append("outputChar");
                        output.append((char) value);
                    }
                    case 1 -> {
                        // Pop a value off the stack and output it as a number.
                        debug.append("outputNum");
                        output.append(value);
                    }
                    default -> // Invalid command
                            throw new RuntimeException("Not a valid input / output command");
                }
            }
            case 1 -> // Read ...
                    // '\t' ' ': Read a character from input, a, Pop a value off the stack, b, then store the ASCII value of a at heap address b.
                    // '\t' '\t' : Read a number from input, a, Pop a value off the stack, b, then store a at heap address b.
                    throw new RuntimeException("Input not implemented number yet");
            default -> // Invalid command
                    throw new RuntimeException("Not a valid input / output command");
        }
    }

    private void pushToStack(int number) {
        stack.push(number);
    }

    private int popFromStack() {
        return stack.pop();
    }

    private void processStackManipulation() {
        int cmd = readNext();
        switch (cmd) {
            case 0 -> {
                // Push n onto the stack
                int n = readN();
                debug.append("push(").append(n).append(")");
                pushToStack(n);
            }
            case 1 -> // 0 (number): Duplicate the nth value from the top of the stack and push onto the stack.
                    // -1 (number): Discard the top n values below the top of the stack from the stack.
                    //              (For `n < 0` or `n >= stack.length`, remove everything but the top value.)
                    throw new RuntimeException("Not implemented number yet");
            default -> // 1
                    // 0 - Duplicate the top value on the stack.
                    // 1 - Swap the top two value on the stack.
                    // -1 - Discard the top value on the stack.
                    throw new RuntimeException("Not implemented single yet");
        }
    }

    /**
     * Read a number from the code:
     * Numbers begin with a sign symbol: 1 ('\t') -> negative or 0 (' ') -> positive
     * Numbers end with a terminal symbol: -1 ('\n')'
     * Between the sign symbol and the terminal symbol are binary digits 0 (' ') or 1 ('\t')
     * The number expression <sign><terminal> will be treated as zero.
     * The expression of just <terminal> should throw an error
     *
     * @return the number
     */
    private int readN() {
        int sign = readNext();
        if (sign < 0)
            throw new NumberFormatException("bad number format <terminator> with no <sign>");
        sign = 1 - 2 * sign; // convert 0 / 1 to +1 / -1
        int terminator = readNext();
        int number = 0;
        while (terminator != -1){
            number *= 2;
            number += terminator;
            terminator = readNext(); // if we end code early we should get an error!
        }
        return number * sign;
    }

    /**
     * Convert the "whitespace" source code into a queue of tri-state codes.
     * As:
     *    ' ' is often the equivalent of binary 0, so use 0
     *    '\t' is often the equivalent of binary 1, so use 1
     *    '\n' if usually more special, so use -1
     *
     * @param source String of Whitespace code
     * @return a Queue of Integer codes
     */
    public static Queue<Integer> toCodes(String source){
        return source
                .chars()
                .filter(Character::isWhitespace)
                .map(c -> (c == ' ') ? 0 : (c == '\t') ? 1 : -1)
                .boxed().collect(Collectors.toCollection(ArrayDeque::new));
    }

    /**
     * Execute the given code string with the given input stream
     *
     * @param code String of Whitespace code
     * @param input an InputStream to possibly read from
     * @return output as a String
     */
    // solution
    public static  String execute(String code, InputStream input) {
        WhitespaceInterpreter whitespaceInterpreter = new WhitespaceInterpreter(code);
        String output = "Nothing";
        try {
            output = whitespaceInterpreter.execute(input);
            System.out.println("Debug: " + whitespaceInterpreter.debug);
            System.out.println("Output: " + output);
        } catch (RuntimeException e){
            System.out.println("Runtime Error: " + e.getMessage());
            System.out.println("Debug: " + whitespaceInterpreter.debug);
            System.out.println("Output: " + output);
            throw e;
        }
        return output;
    }

}
