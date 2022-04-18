import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
    public static final int RETN = JUMP_N + 1;
    public static final int GETC = RETN + 1;
    public static final int GETN = GETC + 1;
    public static final int HEAP_S = GETN + 1;
    public static final int HEAP_G = HEAP_S + 1;

    private StringBuilder debug;
    private final List<Integer> pseudocode;
    private final Map<Integer, Integer> labels;
    private int location;
    private Stack<Integer> stack;
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
        try {
            while (executing && location < codeArray.length){
                parseIMP();
            }
        }
        catch (RuntimeException e){
            System.out.println("Parse error: " + debug);
            System.out.println("Labels: " + labels.size());
            for (int label: labels.keySet()) {
                System.out.println("   " + label + ": " + labels.get(label));
            }
            throw e;
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
        //noinspection EnhancedSwitchMigration
        switch (IMP){
            case 0: // Stack Manipulation
                parseStackManipulation();
                break;
            case -1: // Flow Control
                parseFlowControl();
                break;
            default: // 1
                IMP = readNext();
                //noinspection EnhancedSwitchMigration
                switch (IMP) {
                    case 0: // Arithmetic
                        parseArithmetic();
                        break;
                    case -1: // Input/Output
                        parseInputOutput();
                        break;
                    default: // 1
                        // Heap Access
                        parseHeapAccess();
                }
        }
    }

    private void parseStackManipulation() {
        int cmd = readNext();
        int n;
        //noinspection EnhancedSwitchMigration
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
                //noinspection EnhancedSwitchMigration
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
                //noinspection EnhancedSwitchMigration
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
        //noinspection EnhancedSwitchMigration
        switch (cmd) {
            case 0:
                cmd = readNext();
                //noinspection EnhancedSwitchMigration
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
                //noinspection EnhancedSwitchMigration
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
        //noinspection EnhancedSwitchMigration
        switch (cmd) {
            case 0:
                cmd = readNext();
                //noinspection EnhancedSwitchMigration
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
                //noinspection EnhancedSwitchMigration
                switch (cmd) {
                    case 0:
                        // (label): Pop a value off the stack and jump to the label specified by n if the value is zero.
                        pseudocode.add(JUMP_Z);
                        label = readLabel();
                        pseudocode.add(label);
                        debug.append("Jump0(").append(label).append(")\n");
                        break;
                    case 1:
                        //* (label): Pop a value off the stack and jump to the label specified by n if the value is less than zero.
                        pseudocode.add(JUMP_N);
                        label = readLabel();
                        pseudocode.add(label);
                        debug.append("JumpN(").append(label).append(")\n");
                        break;
                    default:
                        //* Exit a subroutine and return control to the location from which the subroutine was called.
                        pseudocode.add(RETN);
                        debug.append("Return");
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
        //noinspection EnhancedSwitchMigration
        switch (cmd) {
            case 0:
                // Pop and output
                cmd = readNext();
                //noinspection EnhancedSwitchMigration
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
                cmd = readNext();
                //noinspection EnhancedSwitchMigration
                switch (cmd) {
                    case 0:
                        // Read a character from input, a, Pop a value off the stack, b, then store the ASCII value of a at heap address b.
                        pseudocode.add(GETC);
                        debug.append("GetC");
                        break;
                    case 1:
                        // Read a number from input, a, Pop a value off the stack, b, then store a at heap address b.
                        pseudocode.add(GETN);
                        debug.append("GetN");
                        break;
                    default: // Invalid command
                        throw new RuntimeException("Not a valid input / output command");
                }
                break;
            default: // Invalid command
                    throw new RuntimeException("Not a valid input / output command");
        }
    }

    private void parseHeapAccess() {
        int cmd = readNext();
        //noinspection EnhancedSwitchMigration
        switch (cmd) {
            case 0:
                // Pop a and b, then store a at heap address b.
                pseudocode.add(HEAP_S);
                debug.append("HeapS");
                break;
            case 1:
                // Pop a and then push the value at heap address a onto the stack.
                pseudocode.add(HEAP_G);
                debug.append("HeapG");
                break;
            default: // Invalid command
                throw new RuntimeException("Not a valid heap access command");
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
        return readBinary(1);
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

    public String execute(InputStream rawInput, OutputStream outputStream) throws IOException {
        BufferedReader input = rawInput == null ? null :
                new BufferedReader(new InputStreamReader(rawInput, StandardCharsets.UTF_8));
        StringBuilder output = new StringBuilder();
        stack = new Stack<>();
        Stack<Integer> returnLocation = new Stack<>();
        Map<Integer, Integer> heap = new HashMap<>();
        executing = true;
        location = 0;
        int n;
        int m;
        int label;
        while (executing && location < pseudocode.size()){
            debug.append("[").append(location).append("]");
            int action = nextInteger();
            //noinspection EnhancedSwitchMigration
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
                    if (outputStream != null)
                        outputStream.write(n);
                    break;
                case OUTN:
                    n = popFromStack();
                    output.append(n);
                    if (outputStream != null)
                        outputStream.write(((Integer) n).toString().getBytes(StandardCharsets.UTF_8));
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
                    break;
                case RETN:
                    location = returnLocation.pop();
                    debug.append(", ").append(location);
                    break;
                case HEAP_S:
                    n = popFromStack();
                    m = popFromStack();
                    heap.put(m, n);
                    debug.append(", ").append(m).append("(").append(n).append(")");
                    break;
                case HEAP_G:
                    n = popFromStack();
                    m = heap.get(n);
                    pushToStack(m);
                    debug.append(", ").append(n).append("(").append(m).append(")");
                    break;
                case GETC:
                    try {
                        assert input != null;
                        n = input.read();
                    } catch (IOException e) {
                        throw new RuntimeException("Error reading from input: " +e.getMessage());
                    }
                    // only for debug! System.out.println("Read in: " + n);
                    m = popFromStack();
                    heap.put(m, n);
                    debug.append(", ").append(m).append("(").append(n).append(")");
                    break;
                case GETN:
                    try {
                        assert input != null;
                        String number = input.readLine();
                        n = Integer.parseInt(number);
                    } catch (IOException e) {
                        throw new RuntimeException("Error reading from input: " +e.getMessage());
                    }
                    m = popFromStack();
                    heap.put(m, n);
                    debug.append(", ").append(m).append("(").append(n).append(")");
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
        return execute(code, input, null);
    }

    /**
     * Execute the given code string with the given input stream
     *
     * @param code String of Whitespace code
     * @param inputStream an InputStream to possibly read from
     * @param outputStream an OutputStream to optionally output to if provided
     * @return output as a String
     */
    // solution
    public static String execute(String code, InputStream inputStream, OutputStream outputStream) {
        WhitespaceInterpreter whitespaceInterpreter = new WhitespaceInterpreter(code);
        System.out.println("Parse: " + whitespaceInterpreter.debug);
        System.out.println("Labels: " + whitespaceInterpreter.labels.size());
        for (int label: whitespaceInterpreter.labels.keySet()) {
            System.out.println("   " + label + ": " + whitespaceInterpreter.labels.get(label));
        }
        whitespaceInterpreter.debug = new StringBuilder(); // reset
        String output = "Nothing";
        try {
            output = whitespaceInterpreter.execute(inputStream, outputStream);
            System.out.println("Debug: " + whitespaceInterpreter.debug);
            System.out.println("Output: " + output);
        } catch (RuntimeException e){
            System.out.println("Runtime Error: " + e.getMessage());
            System.out.println("Debug: " + whitespaceInterpreter.debug);
            System.out.println("Output: " + output);
            throw e;
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
            System.out.println("Debug: " + whitespaceInterpreter.debug);
            System.out.println("Output: " + output);
            throw new RuntimeException("IOException: " + e.getMessage());
        }
        return output;
    }

}
