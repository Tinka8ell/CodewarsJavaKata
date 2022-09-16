package com.tinkabell.t3pcompiler;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
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
        //System.out.println(tokens);
        if (!tokens.removeFirst().equals("["))
            throw new IllegalArgumentException("Program must start with '['");
        //System.out.println(tokens);
        while (!tokens.removeFirst().equals("]"))
            throw new IllegalArgumentException("Don't expect parameters in program");
        //System.out.println(tokens);
        String number = tokens.removeFirst(); // expect a number
        //System.out.println(tokens);
        //System.out.println(tokens.size());
        if (!tokens.isEmpty() && !tokens.removeFirst().equals("$"))
            throw new IllegalArgumentException("Program can't have more than one value, and next is not $");

        return new UnOp("imm", Integer.parseInt(number));
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
        String op = ast.op();
        if (ast instanceof UnOp unOp) {
            int number = unOp.n();
            code.add("IM " + number); // hard code return 0 for now
            return code;
        }
        return null;
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