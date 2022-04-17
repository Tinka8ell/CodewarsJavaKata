import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class WhitespaceInterpreter {

    private StringBuilder output;
    private Stack<Integer> stack;
    private Map<Integer, Integer> heap;
    private Queue<Integer> codeQueue;
    private boolean executing;

    public WhitespaceInterpreter(String code) {
        codeQueue = toCodes(code);
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
        int IMP = codeQueue.poll();
        switch (IMP){
            case 0 -> {
                // Stack Manipulation
                processStackManipulation();
            }
            case -1 -> {
                // Flow Control
                throw new RuntimeException("Flow control not implemented yet");
            }
            default -> { // 1
                IMP = codeQueue.poll();
                switch (IMP) {
                    case 0 -> {
                        // Arithmetic
                        throw new RuntimeException("Arithmetic not implemented yet");
                    }
                    case -1 -> {
                        // Input/Output
                        throw new RuntimeException("Input/Output not implemented yet");
                    }
                    default -> { // 1
                        // Heap Access
                        throw new RuntimeException("Heap Access not implemented yet");
                    }
                }
            }
        }
    }

    private void processStackManipulation() {
        int cmd = codeQueue.poll();
        switch (cmd) {
            case 0 -> {
                // Push n onto the stack
                throw new RuntimeException("Push n not implemented yet");
            }
            case 1 -> {
                // 0 (number): Duplicate the nth value from the top of the stack and push onto the stack.
                // -1 (number): Discard the top n values below the top of the stack from the stack.
                //              (For `n < 0` or `n >= stack.length`, remove everything but the top value.)
                throw new RuntimeException("Not implemented number yet");
            }
            default -> { // 1
                // 0 - Duplicate the top value on the stack.
                // 1 - Swap the top two value on the stack.
                // -1 - Discard the top value on the stack.
                throw new RuntimeException("Not implemented single yet");
            }
        }
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

    // solution
    public static  String execute(String code, InputStream input) {
        WhitespaceInterpreter whitespaceInterpreter = new WhitespaceInterpreter(code);
        return whitespaceInterpreter.execute(input);
    }

}
