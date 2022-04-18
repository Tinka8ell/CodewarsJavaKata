import java.io.InputStream;
import java.util.*;

@SuppressWarnings("ALL")
public class WhitespaceInterpreter {

    public static final int PUSH_N = 900; // random seed
    public static final int DUPE_N = PUSH_N + 1;
    public static final int DISC_N = DUPE_N + 1;
    public static final int DUPE = DISC_N + 1;
    private static final int SWAP = DUPE + 1;
    public static final int DISC = SWAP + 1;
    public static final int FINE = DISC + 1;
    public static final int OUTC = FINE + 1;
    public static final int OUTN = OUTC + 1;
    public static final int CALL = OUTN + 1;
    public static final int JUMP = CALL + 1;
    public static final int ADD = JUMP + 1;
    public static final int SUB = ADD + 1;
    public static final int MUL = SUB + 1;
    public static final int DIV = MUL + 1;
    public static final int MOD = DIV + 1;
    public static final int JUMP_Z = MOD + 1;
    public static final int JUMP_N= JUMP_Z + 1;
    public static final int JUMP_P = JUMP_N + 1;

    private final StringBuilder debug;
    private final List<Integer> pseudocode;
    private final Map<Integer, Integer> labels;
    private int location;
    private StringBuilder output;
    private Stack<Integer> stack;
    private Stack<Integer> returnLocation;
    private Map<Integer, Integer> heap;
    private final Integer[] codeArray;
    private boolean executing;

    /**
     * Take whitespace code and convert to code array (of 0, 1  & -1).
     * Parse the codeArray into pseudocode.
     *   Pseudocode will be ints:
     *     An int code followed by optional int value
     *     which may be a signed int or a label int.
     *   Also generate a map of labels to location in the code.
     *
     * @param code String of whitespace code
     */
    public WhitespaceInterpreter(String code) {
        codeArray = toCodes(code);
        debug = new StringBuilder();
        pseudocode = new ArrayList<>();
        labels = new HashMap<>();
        location = 0;
        executing = true;
        while (executing && location < codeArray.length){
            parseIMP();
        }
    }

    /*
     * Parse methods
     */

    private Integer readNext() {
        if (location >= codeArray.length)
            throw new RuntimeException("Early end of code");
        int code = codeArray[location];
        location++;
        debug.append((code == 0) ? '0' : (code > 0) ? '+' : '-');
        return code;
    }

    private void parseIMP() {
        int IMP = readNext();
        switch (IMP){
            case 0: // Stack Manipulation
                parseStackManipulation();
                break;
            case -1: // Flow Control
                parseFlowControl();
                break;
            default: // 1
                IMP = readNext();
                switch (IMP) {
                    case 0: // Arithmetic
                        parseArithmetic();
                        break;
                    case -1: // Input/Output
                        parseInputOutput();
                        break;
                    default: // 1
                        // Heap Access
                        throw new RuntimeException("Heap Access not implemented yet");
                }
        }
    }

    private void parseStackManipulation() {
        int cmd = readNext();
        int n;
        switch (cmd) {
            case 0:
                // Push n onto the stack
                pseudocode.add(PUSH_N);
                n = readN();
                pseudocode.add(n);
                debug.append("push(").append(n).append(")\n");
                break;
            case 1:
                cmd = readNext();
                switch (cmd) {
                    case 0:
                        // (number): Duplicate the nth value from the top of the stack and push onto the stack.
                        pseudocode.add(DUPE_N);
                        n = readN();
                        if (n < 0) // don't think this should support -nth from top of stack!
                            throw new RuntimeException("Not a valid stack manipulation command");
                        pseudocode.add(n);
                        debug.append("dupe(").append(n).append(")\n");
                        break;
                    case -1:
                        // (number): Discard the top n values below the top of the stack from the stack.
                        //              (For `n < 0` or `n >= stack.length`, remove everything but the top value.)
                        pseudocode.add(DISC_N);
                        n = readN();
                        pseudocode.add(n);
                        debug.append("disc(").append(n).append(")\n");
                        break;
                    default: // 1
                        throw new RuntimeException("Not a valid stack manipulation command");
                }
                break;
            default:
                cmd = readNext();
                switch (cmd) {
                    case 0:
                        // Duplicate the top value on the stack.
                        pseudocode.add(DUPE);
                        debug.append("dupe");
                        break;
                    case 1:
                        // Swap the top two value on the stack.
                        pseudocode.add(SWAP);
                        debug.append("swap");
                        break;
                    default:
                        // -1 - Discard the top value on the stack.
                        pseudocode.add(DISC);
                        debug.append("disc");
                }
        }
    }

    private void parseArithmetic() {
        int cmd = readNext();
        switch (cmd) {
            case 0:
                cmd = readNext();
                switch (cmd) {
                    case 0: //0: Pop a and b, then push b+a.
                        pseudocode.add(ADD);
                        debug.append("add");
                        break;
                    case 1: //1: Pop a and b, then push b-a.
                        pseudocode.add(SUB);
                        debug.append("sub");
                        break;
                    default:
                        //-1: Pop a and b, then push b*a.
                        pseudocode.add(MUL);
                        debug.append("mul");
                }
                break;
            case 1:
                cmd = readNext();
                switch (cmd) {
                    case 0: // Pop a and b, then push b/a. If a is zero, throw an error.
                        // Note that the result is defined as the floor of the quotient.
                        pseudocode.add(DIV);
                        debug.append("div");
                        break;
                    case 1:
                        // Pop a and b, then push b%a. If a is zero, throw an error.
                        // Note that the result is defined as the remainder after division and sign (+/-) of the divisor (a).
                        pseudocode.add(MOD);
                        debug.append("mod");
                        break;
                    default:
                        throw new RuntimeException("Not a valid arithmetic command");
                }
                break;
            default:
                throw new RuntimeException("Not a valid arithmetic command");
        }
    }

    private void parseFlowControl() {
        int cmd = readNext();
        int label;
        switch (cmd) {
            case 0:
                cmd = readNext();
                switch (cmd) {
                    case 0: // (label): Mark a location in the program with label n.
                        // no pseudocode for label
                        label = readLabel();
                        labels.put(label, pseudocode.size()); // address of next executable instruction
                        debug.append("Lbl(").append(label).append(")\n");
                        break;
                    case 1: // (label): Call a subroutine with the location specified by label n.
                        pseudocode.add(CALL);
                        label = readLabel();
                        pseudocode.add(label);
                        debug.append("Call(").append(label).append(")\n");
                        break;
                    default: // (label): Jump unconditionally to the position specified by label n.
                        pseudocode.add(JUMP);
                        label = readLabel();
                        pseudocode.add(label);
                        debug.append("Jump(").append(label).append(")\n");
                }
                break;
            case 1:
                cmd = readNext();
                switch (cmd) {
                    case 0:
                        // (label): Pop a value off the stack and jump to the label specified by n if the value is zero.
                        pseudocode.add(JUMP_Z);
                        label = readLabel();
                        pseudocode.add(label);
                        debug.append("Jump0(").append(label).append(")\n");
                        break;
                    case 1:
                        //* `[tab][tab]` (label): Pop a value off the stack and jump to the label specified by n if the value is less than zero.
                        pseudocode.add(JUMP_N);
                        label = readLabel();
                        pseudocode.add(label);
                        debug.append("JumpN(").append(label).append(")\n");
                        break;
                    default:
                        //* `[tab][line-feed]`: Exit a subroutine and return control to the location from which the subroutine was called.
                        pseudocode.add(JUMP_P);
                        label = readLabel();
                        pseudocode.add(label);
                        debug.append("JumpP(").append(label).append(")\n");
                }
                break;
            default:
                // Exit the program.
                pseudocode.add(FINE);
                debug.append("END!\n\n");
                executing = false;
        }
    }

    private void parseInputOutput() {
        int cmd = readNext();
        switch (cmd) {
            case 0:
                // Pop and output
                cmd = readNext();
                switch (cmd) {
                    case 0:
                        // Pop a value off the stack and output it as a character
                        pseudocode.add(OUTC);
                        debug.append("OutC");
                        break;
                    case 1:
                        // Pop a value off the stack and output it as a number.
                        pseudocode.add(OUTN);
                        debug.append("OutN");
                        break;
                    default: // Invalid command
                            throw new RuntimeException("Not a valid input / output command");
                }
                break;
            case 1: // Read ...
                    // '\t' ' ': Read a character from input, a, Pop a value off the stack, b, then store the ASCII value of a at heap address b.
                    // '\t' '\t' : Read a number from input, a, Pop a value off the stack, b, then store a at heap address b.
                    throw new RuntimeException("Input not implemented number yet");
            default: // Invalid command
                    throw new RuntimeException("Not a valid input / output command");
        }
    }

    /**
     * Read a label from the code:
     * Much like numbers, but different ...
     * Labels end with a terminal symbol: -1 ('\n')'
     * Consists of binary digits 0 (' ') or 1 ('\t')
     * The expression of just <terminal> is valid
     * Labels are unique, so "01" and "1" are different
     * Adding a pseudo "1" before the binary creates unique numbers
     * as "101" is 5, but "11" is 3 ...
     *
     * @return label as a number
     */
    private int readLabel() {
        int number = readBinary(1);
        return number;
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
        int number = readBinary(0);
        return number * sign;
    }

    /**
     * Read binary from the code seeded with number:
     * Binary end with a terminal symbol: -1 ('\n')'
     * Consists of binary digits 0 (' ') or 1 ('\t')
     *
     * @param number is seed: 1 for a label and 0 for a number
     * @return the number
     */
    private int readBinary(int number) {
        int terminator = readNext();
        while (terminator != -1){
            number *= 2;
            number += terminator;
            terminator = readNext(); // if we end code early we should get an error!
        }
        return number;
    }

    /*
     * Execute methods
     */

    private int nextInteger() {
        int action = pseudocode.get(location);
        location++;
        return action;
    }

    public String execute(InputStream input) {
        output = new StringBuilder();
        stack = new Stack<>();
        returnLocation = new Stack<>();
        heap = new HashMap<>();
        executing = true;
        location = 0;
        int n;
        int m;
        int label;
        while (executing && location < pseudocode.size()){
            debug.append("[").append(location).append("]");
            int action = nextInteger();
            switch (action){
                case PUSH_N:
                    n = nextInteger();
                    debug.append(", ").append(n);
                    pushToStack(n);
                    break;
                case DUPE_N:
                    n = nextInteger();
                    m = stack.elementAt(stack.size() - 1 - n);
                    debug.append(", ").append(m);
                    pushToStack(m);
                    break;
                case DISC_N:
                    n = nextInteger();
                    m = popFromStack(); // top of stack
                    while (!stack.empty() && n > 0){
                        popFromStack();
                        n--;
                    }
                    pushToStack(m);
                    break;
                case DUPE:
                    n = popFromStack();
                    pushToStack(n);
                    debug.append(", ").append(n);
                    pushToStack(n);
                    break;
                case SWAP:
                    n = popFromStack();
                    m = popFromStack();
                    debug.append(", ").append(n);
                    pushToStack(n);
                    debug.append(", ").append(m);
                    pushToStack(m);
                    break;
                case DISC:
                    popFromStack();
                    break;
                case FINE:
                    executing = false;
                    location = pseudocode.size(); // make sure we stop here
                    debug.append("!!!");
                    break;
                case OUTC:
                    n = popFromStack();
                    output.append((char) n);
                    break;
                case OUTN:
                    n = popFromStack();
                    output.append(n);
                    break;
                case CALL:
                    label = nextInteger();
                    returnLocation.push(location);
                    location = labels.get(label);
                    debug.append(", ").append(location);
                    break;
                case JUMP:
                    label = nextInteger();
                    location = labels.get(label);
                    debug.append(", ").append(location);
                    break;
                case ADD:
                    n = popFromStack();
                    m = popFromStack();
                    m += n;
                    debug.append(", ").append(m);
                    pushToStack(m);
                    break;
                case SUB:
                    n = popFromStack();
                    m = popFromStack();
                    m -= n;
                    debug.append(", ").append(m);
                    pushToStack(m);
                    break;
                case MUL:
                    n = popFromStack();
                    m = popFromStack();
                    m *= n;
                    debug.append(", ").append(m);
                    pushToStack(m);
                    break;
                case DIV:
                    n = popFromStack();
                    m = popFromStack();
                    m /= n;
                    debug.append(", ").append(m);
                    pushToStack(m);
                    break;
                case MOD:
                    n = popFromStack();
                    m = popFromStack();
                    m %= n;
                    debug.append(", ").append(m);
                    pushToStack(m);
                    break;
                case JUMP_Z:
                    label = nextInteger();
                    n = popFromStack();
                    if (n == 0) {
                        location = labels.get(label);
                        debug.append(", ").append(location);
                    }
                    break;
                case JUMP_N:
                    label = nextInteger();
                    n = popFromStack();
                    if (n < 0){
                        location = labels.get(label);
                        debug.append(", ").append(location);
                    }
                    debug.append(", NJ");
                    break;
                case JUMP_P:
                    label = nextInteger();
                    n = popFromStack();
                    if (n > 0){
                        location = labels.get(label);
                        debug.append(", ").append(location);
                    }
                    debug.append(", NJ");
                    break;
                default:
                    throw new RuntimeException("Process Pseudocode not written yet!");
            }
        }
        if (executing) // not finished properly
            throw new RuntimeException("Code ended before execution completed");
        return output.toString();
    }

    private void pushToStack(int number) {
        stack.push(number);
    }

    private int popFromStack() {
        return stack.pop();
    }

    /**
     * Convert the "whitespace" source code into a queue of tri-state codes.
     * As:
     *    ' ' is often the equivalent of binary 0, so use 0
     *    '\t' is often the equivalent of binary 1, so use 1
     *    '\n' if usually more special, so use -1
     *
     * @param source String of Whitespace code
     * @return an Array of Integer codes
     */
    public static Integer[] toCodes(String source){
        return source
                .chars()
                .filter(Character::isWhitespace)
                .map(c -> (c == ' ') ? 0 : (c == '\t') ? 1 : -1)
                .boxed()
                .toArray(Integer[]::new);
                //.collect(Collectors.toList())
                //.toArray(new Integer[0]);
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
            System.out.println("Labels: " + whitespaceInterpreter.labels.size());
            for (int label: whitespaceInterpreter.labels.keySet()) {
                System.out.println("   " + label + ": " + whitespaceInterpreter.labels.get(label));
            }
            System.out.println("Output: " + output);
        } catch (RuntimeException e){
            System.out.println("Runtime Error: " + e.getMessage());
            System.out.println("Debug: " + whitespaceInterpreter.debug);
            System.out.println("Labels: " + whitespaceInterpreter.labels.size());
            for (int label: whitespaceInterpreter.labels.keySet()) {
                System.out.println("   " + label + ": " + whitespaceInterpreter.labels.get(label));
            }
            System.out.println("Output: " + output);
            throw e;
        }
        return output;
    }

}
