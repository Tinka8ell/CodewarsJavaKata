import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WhitespaceInterpreter {

    public static final int BAD = -1; // for bad codes!
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

    private final List<Integer> pseudocode;
    private final Map<Integer, Integer> labels;
    private int location;
    private Stack<Integer> stack;
    private final Integer[] codeArray;

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
        pseudocode = new ArrayList<>();
        labels = new HashMap<>();
        location = 0;
        while (location < codeArray.length - 1){
            parseIMP();
        }
    }

    /*
     * Parse methods
     */

    private void badInstruction() {
        pseudocode.add(BAD);
        // end parsing
        location = codeArray.length;
    }

    private Integer readNext() {
        if (location >= codeArray.length)
            throw new Error("Early end of code");
        int code = codeArray[location];
        location++;
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
                            throw new RuntimeException("Invalid parameter");
                        pseudocode.add(n);
                        break;
                    case -1:
                        // (number): Discard the top n values below the top of the stack from the stack.
                        //              (For `n < 0` or `n >= stack.length`, remove everything but the top value.)
                        pseudocode.add(DISC_N);
                        n = readN();
                        pseudocode.add(n);
                        break;
                    default: // 1
                        badInstruction();
                }
                break;
            default:
                cmd = readNext();
                //noinspection EnhancedSwitchMigration
                switch (cmd) {
                    case 0:
                        // Duplicate the top value on the stack.
                        pseudocode.add(DUPE);
                        break;
                    case 1:
                        // Swap the top two value on the stack.
                        pseudocode.add(SWAP);
                        break;
                    default:
                        // -1 - Discard the top value on the stack.
                        pseudocode.add(DISC);
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
                        break;
                    case 1: //1: Pop a and b, then push b-a.
                        pseudocode.add(SUB);
                        break;
                    default:
                        //-1: Pop a and b, then push b*a.
                        pseudocode.add(MUL);
                }
                break;
            case 1:
                cmd = readNext();
                //noinspection EnhancedSwitchMigration
                switch (cmd) {
                    case 0: // Pop a and b, then push b/a. If a is zero, throw an error.
                        // Note that the result is defined as the floor of the quotient.
                        pseudocode.add(DIV);
                        break;
                    case 1:
                        // Pop a and b, then push b%a. If a is zero, throw an error.
                        // Note that the result is defined as the remainder after division and sign (+/-) of the divisor (a).
                        pseudocode.add(MOD);
                        break;
                    default:
                        badInstruction();
                }
                break;
            default:
                badInstruction();
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
                        if (null != labels.put(label, pseudocode.size())) // address of next executable instruction
                            labels.put(label, -1); //throw new Error("multiple label definition");
                        break;
                    case 1: // (label): Call a subroutine with the location specified by label n.
                        pseudocode.add(CALL);
                        label = readLabel();
                        pseudocode.add(label);
                        break;
                    default: // (label): Jump unconditionally to the position specified by label n.
                        pseudocode.add(JUMP);
                        label = readLabel();
                        pseudocode.add(label);
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
                        break;
                    case 1:
                        //* (label): Pop a value off the stack and jump to the label specified by n if the value is less than zero.
                        pseudocode.add(JUMP_N);
                        label = readLabel();
                        pseudocode.add(label);
                        break;
                    default:
                        //* Exit a subroutine and return control to the location from which the subroutine was called.
                        pseudocode.add(RETN);
                }
                break;
            default:
                cmd = readNext();
                if (cmd != -1)
                    badInstruction();
                // Exit the program.
                pseudocode.add(FINE);
                // Opps! of course the program can be beyond the first end point!
                // executing = false;
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
                        break;
                    case 1:
                        // Pop a value off the stack and output it as a number.
                        pseudocode.add(OUTN);
                        break;
                    default: // Invalid command
                        badInstruction();
                }
                break;
            case 1: // Read ...
                cmd = readNext();
                //noinspection EnhancedSwitchMigration
                switch (cmd) {
                    case 0:
                        // Read a character from input, a, Pop a value off the stack, b, then store the ASCII value of a at heap address b.
                        pseudocode.add(GETC);
                        break;
                    case 1:
                        // Read a number from input, a, Pop a value off the stack, b, then store a at heap address b.
                        pseudocode.add(GETN);
                        break;
                    default: // Invalid command
                        badInstruction();
                }
                break;
            default: // Invalid command
                badInstruction();
        }
    }

    private void parseHeapAccess() {
        int cmd = readNext();
        //noinspection EnhancedSwitchMigration
        switch (cmd) {
            case 0:
                // Pop a and b, then store a at heap address b.
                pseudocode.add(HEAP_S);
                break;
            case 1:
                // Pop a and then push the value at heap address a onto the stack.
                pseudocode.add(HEAP_G);
                break;
            default: // Invalid command
                badInstruction();
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
        BufferedReader input = null;
        if (rawInput != null){
            input = new BufferedReader(new InputStreamReader(rawInput, StandardCharsets.UTF_8));
        }
        StringBuilder output = new StringBuilder();
        if (outputStream != null)
            outputStream.flush();
        stack = new Stack<>();
        Stack<Integer> returnLocation = new Stack<>();
        Map<Integer, Integer> heap = new HashMap<>();
        boolean executing = true;
        location = 0;
        int n;
        int m;
        int label;
        while (executing && location < pseudocode.size()){
            int action = nextInteger();
            //noinspection EnhancedSwitchMigration
            switch (action){
                case PUSH_N:
                    n = nextInteger();
                    pushToStack(n);
                    break;
                case DUPE_N:
                    n = nextInteger();
                    m = stack.elementAt(stack.size() - 1 - n);
                    pushToStack(m);
                    break;
                case DISC_N:
                    n = nextInteger();
                    m = popFromStack(); // top of stack
                    while (!stack.empty() && n != 0){
                        popFromStack();
                        n--;
                    }
                    pushToStack(m);
                    break;
                case DUPE:
                    n = popFromStack();
                    pushToStack(n);
                    pushToStack(n);
                    break;
                case SWAP:
                    n = popFromStack();
                    m = popFromStack();
                    pushToStack(n);
                    pushToStack(m);
                    break;
                case DISC:
                    popFromStack();
                    break;
                case FINE:
                    executing = false;
                    location = pseudocode.size(); // make sure we stop here
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
                    setLocation(label);
                    break;
                case JUMP:
                    label = nextInteger();
                    setLocation(label);
                    break;
                case ADD:
                    n = popFromStack();
                    m = popFromStack();
                    m += n;
                    pushToStack(m);
                    break;
                case SUB:
                    n = popFromStack();
                    m = popFromStack();
                    m -= n;
                    pushToStack(m);
                    break;
                case MUL:
                    n = popFromStack();
                    m = popFromStack();
                    m *= n;
                    pushToStack(m);
                    break;
                case DIV:
                    n = popFromStack();
                    m = popFromStack();
                    m = Math.floorDiv(m, n);
                    pushToStack(m);
                    break;
                case MOD:
                    // using floor mod:  x - y * Math.floorDiv(x, y);
                    n = popFromStack();
                    m = popFromStack();
                    if (m != 0)
                        m = m - n * Math.floorDiv(m, n);
                    pushToStack(m);
                    break;
                case JUMP_Z:
                    label = nextInteger();
                    n = popFromStack();
                    if (n == 0) {
                        setLocation(label);
                    }
                    break;
                case JUMP_N:
                    label = nextInteger();
                    n = popFromStack();
                    if (n < 0){
                        setLocation(label);
                    }
                    break;
                case RETN:
                    location = returnLocation.pop();
                    break;
                case HEAP_S:
                    n = popFromStack();
                    m = popFromStack();
                    heap.put(m, n);
                    break;
                case HEAP_G:
                    n = popFromStack();
                    m = heap.get(n);
                    pushToStack(m);
                    break;
                case GETC:
                    try {
                        assert input != null;
                        n = input.read();
                    } catch (IOException e) {
                        throw new Error("Error reading from input: " +e.getMessage());
                    }
                    if (n < 0) // Note: Cat program will no longer work!
                        throw new RuntimeException("Input has run out!");
                    // only for debug! System.out.println("Read in: " + n);
                    m = popFromStack();
                    heap.put(m, n);
                    break;
                case GETN:
                    try {
                        assert input != null;
                        String number = input.readLine();
                        n = Integer.parseInt(number);
                    } catch (IOException e) {
                        throw new Error("Error reading from input: " +e.getMessage());
                    } catch (NumberFormatException e){
                        throw new RuntimeException("Error reading a number: " +e.getMessage());
                    }
                    m = popFromStack();
                    heap.put(m, n);
                    break;
                default:
                    throw new RuntimeException("Trying to execute unrecognised code");
            }
        }
        if (executing) // not finished properly
            throw new RuntimeException("Code ended before execution completed");
        if (outputStream != null)
            outputStream.flush();
        return output.toString();
    }

    private void setLocation(int label) {
        Integer integer = labels.get(label);
        if (integer == null)
            throw new RuntimeException("Trying to reach undefined label");
        location = integer;
        if (location < 0)
            throw new RuntimeException("Trying to reach multiply defined label");
    }

    private void pushToStack(int number) {
        stack.push(number);
    }

    private int popFromStack() {
        try{
            return stack.pop();
        }
        catch (EmptyStackException e){
            throw new RuntimeException("Stack is empty when popping!");
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
        try {
            if (outputStream != null) outputStream.flush();
            WhitespaceInterpreter whitespaceInterpreter = new WhitespaceInterpreter(code);
            return whitespaceInterpreter.execute(inputStream, outputStream);
        } catch (IOException e) {
            throw new Error(e);
        }
    }

}
