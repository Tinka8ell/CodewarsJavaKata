import java.util.Stack;

public class BefungeInterpreter {

    private static final int[] RIGHT = new int[]{0, 1};
    private static final int[] LEFT = new int[]{0, -1};
    private static final int[] DOWN = new int[]{1, 0};
    private static final int[] UP = new int[]{-1, 0};
    private static final int[][] ALL = new int[][]{RIGHT, LEFT, UP, DOWN};
    private int[][] memory;
    private int[] cp;
    private int[] nextCp;
    private Stack<Integer> stack;

    /**
     * Befunge Interpreter.
     * 
     * Esoteric languages are pretty hard to program, but it's fairly interesting to write interpreters for them!
     * Your task is to write a method which will interpret Befunge-93 code! 
     * Befunge-93 is a language in which the code is presented not as a series of instructions, 
     * but as instructions scattered on a 2D plane; your pointer starts at the top-left corner 
     * and defaults to moving right through the code. Note that the instruction pointer wraps around the screen! 
     * There is a singular stack which we will assume is unbounded and only contain integers. 
     * While Befunge-93 code is supposed to be restricted to 80x25, you need not be concerned with code size. 
     * Befunge-93 supports the following instructions (from Wikipedia):
     *
     * 0-9 Push this number onto the stack.
     * + Addition: Pop a and b, then push a+b.
     * - Subtraction: Pop a and b, then push b-a.
     * * Multiplication: Pop a and b, then push a*b.
     * / Integer division: Pop a and b, then push b/a, rounded down. If a is zero, push zero.
     * % Modulo: Pop a and b, then push the b%a. If a is zero, push zero.
     * ! Logical NOT: Pop a value. If the value is zero, push 1; otherwise, push zero.
     * ` (backtick) Greater than: Pop a and b, then push 1 if b>a, otherwise push zero.
     * > Start moving right.
     * < Start moving left.
     * ^ Start moving up.
     * v Start moving down.
     * ? Start moving in a random cardinal direction.
     * _ Pop a value; move right if value = 0, left otherwise.
     * | Pop a value; move down if value = 0, up otherwise.
     * " Start string mode: push each character's ASCII value all the way up to the next ".
     * : Duplicate value on top of the stack. If there is nothing on top of the stack, push a 0.
     * \ Swap two values on top of the stack. If there is only one value, pretend there is an extra 0 on bottom of the stack.
     * $ Pop value from the stack and discard it.
     * . Pop value and output as an integer.
     * , Pop value and output the ASCII character represented by the integer code that is stored in the value.
     * # Trampoline: Skip next cell.
     * p A "put" call (a way to store a value for later use). Pop y, x and v, then change the character at the position (x,y) in the program to the character with ASCII value v.
     * g A "get" call (a way to retrieve data in storage). Pop y and x, then push ASCII value of the character at that position in the program.
     * @ End program.
     *   (i.e. a space) No-op. Does nothing.
     * The above list is slightly modified: you'll notice if you look at the Wikipedia page 
     * that we do not use the user input instructions and dividing by zero simply yields zero.
     *
     * Here's an example:
     * >987v>.v
     * v456<  :
     * >321 ^ _@
     * will create the output 123456789.
     *
     * So what you must do is create a function such that when you pass in the Befunge code, 
     * the function returns the output that would be generated by the code. So, for example:
     *
     * "123456789".equals(new BefungeInterpreter().interpret(">987v>.v\nv456<  :\n>321 ^ _@")
     * 
     * @param code a string or newline separated rows of Befunge code
     * @return a string representing the "print" output from the code
     */
    public String interpret(String code) {
        String[] rows = code.split("\n");
        memory = new int[rows.length][];
        for (int row = 0; row < rows.length; row++) {
            memory[row] = rows[row].chars().toArray();
        }
        // initial current pointer
        cp = new int[]{0, 0};
        boolean stringMode = false; // if we are reading characters
        // initial next direction
        nextCp = RIGHT;
        stack = new Stack<>();
        boolean stop = false;
        StringBuilder output = new StringBuilder();
        while (!stop){
            // read next operation
            int op = get(cp[1], cp[0]);
            if (stringMode){
                // string mode: push each character's ASCII value all the way up to the next '"'.
                if (op == '"')
                    stringMode = false; // end string mode
                else
                    push(op);
            } else {
                switch (op) {
                    case '+' -> {
                        // Addition: Pop a and b, then push a+b.
                        int a = pop();
                        int b = pop();
                        push(b + a);
                    }
                    case '-' -> {
                        // Subtraction: Pop a and b, then push b-a.
                        int a = pop();
                        int b = pop();
                        push(b - a);
                    }
                    case '*' -> {
                        // Multiplication: Pop a and b, then push a*b.
                        int a = pop();
                        int b = pop();
                        push(b * a);
                    }
                    case '/' -> {
                        // Integer division: Pop a and b, then push b/a, rounded down. If a is zero, push zero.
                        int a = pop();
                        int b = pop();
                        push(b / a);
                    }
                    case '%' -> {
                        // Modulo: Pop a and b, then push the b%a. If a is zero, push zero.
                        int a = pop();
                        int b = pop();
                        push(b % a);
                    }
                    case '!' -> {
                        // Logical NOT: Pop a value. If the value is zero, push 1; otherwise, push zero.
                        int a = pop();
                        push(a == 0 ? 1 : 0);
                    }
                    case '`' -> {
                        // (backtick) Greater than: Pop a and b, then push 1 if b>a, otherwise push zero.
                        int a = pop();
                        int b = pop();
                        push(b > a ? 1 : 0);
                    }
                    case '>' -> // Start moving right.
                            nextCp = RIGHT;
                    case '<' -> // Start moving left.
                            nextCp = LEFT;
                    case '^' -> // Start moving up.
                            nextCp = UP;
                    case 'v' -> // Start moving down.
                            nextCp = DOWN;
                    case '?' -> // Start moving in a random cardinal direction.
                            nextCp = ALL[Math.toIntExact((long) (4 * Math.random()))];
                    case '_' -> {
                        // Pop a value; move right if value = 0, left otherwise.
                        int b = pop();
                        nextCp = (b == 0) ? RIGHT : LEFT;
                    }
                    case '|' -> {
                        // Pop a value; move down if value = 0, up otherwise.
                        int b = pop();
                        nextCp = (b == 0) ? DOWN : UP;
                    }
                    case '"' -> // Start string mode: push each character's ASCII value all the way up to the next '"'.
                            stringMode = true;
                    case ':' -> {
                        // Duplicate value on top of the stack. If there is nothing on top of the stack, push a 0.
                        int a = pop();
                        push(a);
                        push(a);
                    }
                    case '\\' -> {
                        // Swap two values on top of the stack. If there is only one value, pretend there is an extra 0 on bottom of the stack.
                        int a = pop();
                        int b = pop();
                        push(a);
                        push(b);
                    }
                    case '$' -> {
                        // Pop value from the stack and discard it.
                        if (!stack.empty())
                            stack.pop();
                    }
                    case '.' -> {
                        // Pop value and output as an integer.
                        int a = pop();
                        output.append(a);
                    }
                    case ',' -> {
                        // Pop value and output the ASCII character represented by the integer code that is stored in the value.
                        int a = pop();
                        output.append(Character.toString(a));
                    }
                    case '#' -> // Trampoline: Skip next cell.
                            incrementPointer(); // extra increment
                    case 'p' -> {
                        // A "put" call (a way to store a value for later use).
                        // Pop y, x and v, then change the character at the position (x,y) in the program to the character with ASCII value v.
                        int y = pop();
                        int x = pop();
                        int v = pop();
                        put(x, y, v);
                    }
                    case 'g' -> {
                        // A "get" call (a way to retrieve data in storage).
                        // Pop y and x, then push ASCII value of the character at that position in the program.
                        int y = pop();
                        int x = pop();
                        push(get(x, y));
                    }
                    case '@' -> // End program.
                            stop = true;
                    case ' ' ->  // (i.e. a space) No-op. Does nothing.
                            {}
                    default -> {
                        // process numbers 0-9
                        // or error!
                        int num = op - '0';
                        if (num >= 0 && num <= 9)
                            push(num);
                    }
                }
            }
            // finally, move current pointer
            incrementPointer();
        }
        return output.toString();
    }

    private int get(int x, int y) {
        return memory[y][x];
    }

    private void put(int x, int y, int v) {
        memory[y][x] = v;
    }

    private int pop() {
        return stack.empty() ? 0 : stack.pop();
    }

    private void push(int op) {
        stack.push(op);
    }

    private void incrementPointer() {
        // inc x
        cp[1] += nextCp[1];
        if (cp[1] < 0)
            cp[1] = memory[cp[0]].length;
        else if (cp[1] >= memory[cp[0]].length)
            cp[1] = 0;
        // inc y
        cp[0] += nextCp[0];
        if (cp[0] < 0)
            cp[0] = memory.length;
        else if (cp[0] >= memory.length)
            cp[0] = 0;
    }

}