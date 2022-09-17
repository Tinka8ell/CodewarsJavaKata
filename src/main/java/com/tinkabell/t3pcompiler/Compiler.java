package com.tinkabell.t3pcompiler;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Compiler {
    public List<String> compile(String program) {
        return pass3(pass2(pass1(program)));
    }

    /**
     * Returns an un-optimized AST
     */
    public Ast pass1(String program) {
        Deque<String> tokens = tokenize(program);
        if (!tokens.removeFirst().equals("["))
            throw new IllegalArgumentException("Program must start with '['");
        Map<String, Integer> parameters = new HashMap<>();
        int parameterIndex = 0;
        String parameter = tokens.removeFirst();
        while (!parameter.equals("]")) { // Should really also check for end of tokens!
            parameters.put(parameter, parameterIndex);
            parameterIndex ++;
            parameter = tokens.removeFirst();
        }
        String token = tokens.removeFirst(); // expect a number
        int number;
        String command = "imm";
        try {
            number = Integer.parseInt(token);
        } catch (NumberFormatException e) {
            command = "arg";
            number = parameters.get(token); // assume no invalid tokens!
        }
        return new UnOp(command, number);
    }

    /**
     * Returns an AST with constant expressions reduced
     */
    public Ast pass2(Ast ast) {
        return ast;
    }

    /**
     * Returns assembly instructions
     */
    public List<String> pass3(Ast ast) {
        List<String> code = new ArrayList<>();
        String command =  "";
        String op = ast.op();
        if (ast instanceof UnOp unOp) {
            int number = unOp.n();
            command = "IM";
            if (!op.equals("imm"))
                command = "AR";
            command += " " + number;
        }
        code.add(command);
        return code;
    }

    private static Deque<String> tokenize(String program) {
        Deque<String> tokens = new LinkedList<>();
        Pattern pattern = Pattern.compile("[-+*/()\\[\\]]|[a-zA-Z]+|\\d+");
        Matcher m = pattern.matcher(program);
        while (m.find()) {
            tokens.add(m.group());
        }
        tokens.add("$"); // end-of-stream
        return tokens;
    }

}