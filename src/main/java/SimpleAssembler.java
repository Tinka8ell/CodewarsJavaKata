import java.util.HashMap;
//import java.util.HashSet;
import java.util.Map;

/**
 * This is the first part of this kata series. Second part is here:
 * https://www.codewars.com/kata/assembler-interpreter-part-ii/
 *
 * We want to create a simple interpreter of assembler which will support the following instructions:
 *
 * mov x y - copies y (either a constant value or the content of a register) into register x
 * inc x - increases the content of the register x by one
 * dec x - decreases the content of the register x by one
 * jnz x y - jumps to an instruction y steps away
 *    (positive means forward, negative means backward, y can be a register or a constant),
 *    but only if x (a constant or a register) is not zero
 *
 * Register names are alphabetical (letters only). Constants are always integers (positive or negative).
 *
 * Note: the jnz instruction moves relative to itself.
 * For example, an offset of -1 would continue at the previous instruction,
 * while an offset of 2 would skip over the next instruction.
 *
 * The function will take an input list with the sequence of the program instructions and will execute them.
 * The program ends when there are no more instructions to execute,
 * then it returns a dictionary with the contents of the registers.
 *
 * Also, every inc/dec/jnz on a register will always be preceded by a mov on the register first,
 * so you don't need to worry about uninitialized registers.
 *
 * Example
 * SimpleAssembler.interpret(new String[]{"mov a 5","inc a","dec a","dec a","jnz a -1","inc a"});
 * Visualized as:
 * mov a 5
 * inc a
 * dec a
 * dec a
 * jnz a -1
 * inc a
 *
 * The above code will:
 *
 * set register a to 5,
 * increase its value by 1,
 * decrease its value by 2,
 * then decrease its value until it is zero (jnz a -1 jumps to the previous instruction if a is not zero)
 * and then increase its value by 1, leaving register a at 1
 *
 * So, the function should return:
 *
 * {a=1}
 */
public class SimpleAssembler {
    public static Map<String, Integer> interpret(String[] program){
        // HashSet<Integer> debug = new HashSet<>();
        Map<String, Integer> registers = new HashMap<>();
        int nextInstruction = 0;
        while (nextInstruction < program.length){
            // read instruction and move on pointer
            String [] parts = program[nextInstruction++].split(" ");
            final String opCode = parts[0];
            final String register = parts[1];
            final String parameter = (parts.length > 2) ? parts[2] : "";
            // if (debug.add(nextInstruction-1)) System.err.println("" + (nextInstruction-1) + ": " + opCode + " " + register + " " + parameter);
            switch (opCode) { // action part
                case "mov" -> registers.put(register, getValue(registers, parameter));
                case "inc" -> registers.put(register, getValue(registers, register) + 1);
                case "dec" -> registers.put(register, getValue(registers, register) - 1);
                case "jnz" -> {
                    if (getValue(registers, register) != 0)
                        nextInstruction += getValue(registers, parameter) - 1;
                }
            }
        }
        // System.err.println(registers);
        return registers;
    }

    private static int getValue(Map<String, Integer> registers, String parameter) {
        int value = 0; // if register not yet set assume it is 0
        try {
            // check for a constant
            value = Integer.parseInt(parameter);
        } catch (NumberFormatException nfe){
            // else must be a register
            value = registers.getOrDefault(parameter, value);
        }
        return value;
    }

}