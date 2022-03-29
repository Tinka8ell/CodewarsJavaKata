import java.util.*;
import java.util.stream.Collectors;

/**
 * This is the second part of this kata series. First part is here.
 *
 * We want to create an interpreter of assembler which will support the following instructions:
 *
 * mov x, y - copy y (either an integer or the value of a register) into register x.
 * inc x - increase the content of register x by one.
 * dec x - decrease the content of register x by one.
 * add x, y - add the content of the register x with y (either an integer or the value of a register)
 *    and stores the result in x (i.e. register[x] += y).
 * sub x, y - subtract y (either an integer or the value of a register)
 *    from the register x and stores the result in x (i.e. register[x] -= y).
 * mul x, y - same with multiply (i.e. register[x] *= y).
 * div x, y - same with integer division (i.e. register[x] /= y).
 * label: - define a label position
 *    (label = identifier + ":", an identifier being a string that does not match any other command).
 *    Jump commands and call are aimed to these labels positions in the program.
 * jmp lbl - jumps to the label lbl.
 * cmp x, y - compares x (either an integer or the value of a register)
 *    and y (either an integer or the value of a register).
 *    The result is used in the conditional jumps (jne, je, jge, jg, jle and jl)
 * jne lbl - jump to the label lbl if the values of the previous cmp command were not equal.
 * je lbl - jump to the label lbl if the values of the previous cmp command were equal.
 * jge lbl - jump to the label lbl if x was greater or equal than y in the previous cmp command.
 * jg lbl - jump to the label lbl if x was greater than y in the previous cmp command.
 * jle lbl - jump to the label lbl if x was less or equal than y in the previous cmp command.
 * jl lbl - jump to the label lbl if x was less than y in the previous cmp command.
 * call lbl - call to the subroutine identified by lbl.
 *    When a ret is found in a subroutine,
 *    the instruction pointer should return to the instruction next to this call command.
 * ret - when a ret is found in a subroutine,
 *    the instruction pointer should return to the instruction that called the current function.
 * msg 'Register: ', x - this instruction stores the output of the program.
 *    It may contain text strings (delimited by single quotes) and registers.
 *    The number of arguments isn't limited and will vary, depending on the program.
 * end - this instruction indicates that the program ends correctly,
 *    so the stored output is returned
 *    (if the program terminates without this instruction it should return the default output: see below).
 * ; comment - comments should not be taken in consideration during the execution of the program.
 *
 * Output format:
 * The normal output format is a string (returned with the end command).
 * If the program does finish itself without using an end instruction, the default return value is:
 *    null
 *
 * Input format:
 * The function/method will take as input a multiline string of instructions, delimited with EOL characters.
 * Please, note that the instructions may also have indentation for readability purposes.
 *
 * For example:
 *
 * program = "\n; My first program\nmov  a, 5\ninc  a\ncall function\n
 *    msg  '(5+1)/2 = ', a    ; output message\nend\n
 *    \nfunction:\n    div  a, 2\n    ret\n"
 * AssemblerInterpreter.interpret(program);
 *
 * Which is equivalent to (keep in mind that empty lines are not displayed in the console on CW,
 * so you actually won't see the separation before "function:"...):
 *
 * ; My first program
 * mov  a, 5
 * inc  a
 * call function
 * msg  '(5+1)/2 = ', a    ; output message
 * end
 *
 * function:
 *     div  a, 2
 *     ret
 * The above code would
 *    set register a to 5,
 *    increase its value by 1,
 *    calls the subroutine function,
 *    divide its value by 2,
 *    returns to the first call instruction,
 *    prepares the output of the program
 *    and then returns it with the end instruction.
 * In this case, the output would be (5+1)/2 = 3.
 *
 * Based on the previous Kata (SimpleAssembler) we will need to identify
 * the important parts of the language,
 * translate the source into "machine code" based on the analysis above
 * then run it in a virtual machine.
 *
 * Assembly:
 * current location = 0
 * each line:
 *    remove anything after (and including) a ';' - comments
 *    strip leading ad trailing spaces
 *    if none blank:
 *       split at first space: op-code and (trimmed) rest
 *       if op-code ends in ':' treat as label:
 *          add label (less ':') with current location to look-up map
 *       else
 *          store validates opcode (against valid / known ones) in current location
 *          parse rest according to format for op-code:
 *             [[<left-hand>, ] <right-hand>] - assigned to and assignee
 *             <right-hand>, <right-hand> - two read from values to be compared
 *             <string>|<register> [, <string>|<register>]*
 *             where:
 *                <left-hand> is a <register>
 *                <right-hand> is <register>|<label>|<integer>
 *                <string> is "'" <characters>* "'"
 *                <register> is <letter>
 *          store code as String[] - <op-code> [<parameter>]*
 *          increment current location
 * machine code:
 *    code - List of String[]
 *    labels - Map of String, Integer
 *
 * Virtual machine:
 *    pointer - int of position in code
 *    stack - may not need to be stack, of return pointers
 *    registers - Map of String, Integer
 *    output - String of appended output (or null)
 */
public class AssemblerInterpreter {

    private static List<List<String>> code;
    private static Map<String, Integer> labels;

    public static String interpret(final String input) {
        // Convert input into "machine code"
        assemble(input);
        // Run the code
        String result = null;
        int pointer = 0;
        boolean finished = false;
        while (!finished){
            // run the code
            // get the instruction and move pointer to 'next'
            List<String> instruction = code.get(pointer++);
            String opCode = instruction.get(0);
            // action opcode
            switch (opCode){

            }
        }
        // Output the fine result
        return result;
    }

    private static void assemble(final String input) {
        List<String> lines = Arrays.stream(input.split("\n"))
                .map(s -> {
                    int pos = s.indexOf(';');
                    return (pos != -1) ? s.substring(0, pos) : s;
                })
                .map(String::strip)
                .filter(str -> str.trim().length() > 0)
                .collect(Collectors.toList());
        code = new ArrayList<>();
        labels = new HashMap<>();
        for (String line: lines) {
            int pos = line.indexOf(' ');
            String opCode = line;
            List<String> parameters = new ArrayList<>();
            if (pos == -1) {
                if (opCode.endsWith(":")){
                    // add a label where the next element of code will be
                    labels.put(opCode.substring(0, opCode.length()-1), code.size());
                    opCode = null;
                }
            }
            else {
                opCode = line.substring(0, pos);
                line =  line.substring(pos).trim();
                pos = line.indexOf('\'');
                if (pos != -1){
                    // have at least one string so must be message format
                    // must check is "'" is first ig this is still true ....
                    // split into strings (odd elements) and non-strings (even elements)
                    String[] alternates = line.split("'");
                    for (int i = 0; i < alternates.length; i++) {
                        if (i % 2 == 0){
                            // even - non-strings
                            parameters.addAll(Arrays.stream(line.split(","))
                                    .map(String::trim)
                                    .collect(Collectors.toList()));
                        }
                        else {
                            //odd - string
                            parameters.add(alternates[i]);
                        }
                    }
                } else {
                    // only csv!
                    parameters = Arrays.stream(line.split(","))
                            .map(String::trim)
                            .collect(Collectors.toList());
                }
            }
            if (opCode != null) {// add as next element
                parameters.add(0, opCode);
                code.add(parameters);
            }
        }
    }
}