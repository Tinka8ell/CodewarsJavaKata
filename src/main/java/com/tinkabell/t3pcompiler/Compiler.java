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
        Map<String, Integer> parameters = getParameters(tokens);
        Ast ast = getAst(tokens, parameters);
        String peek = tokens.peekFirst();
        assert peek != null; // as we expect at least the end of token character
        if (!peek.equals("$")){
            // not the expected end of the expression
            throw new IllegalArgumentException("Program has extra tokens beyond the expression: " + peek);
        }
        return ast;
    }

    private static Map<String, Integer> getParameters(Deque<String> tokens) {
        Map<String, Integer> parameters = new HashMap<>();
        int parameterIndex = 0;
        String parameter = tokens.removeFirst();
        while (!parameter.equals("]")) { // Should really also check for end of tokens!
            parameters.put(parameter, parameterIndex);
            parameterIndex ++;
            parameter = tokens.removeFirst();
        }
        return parameters;
    }

    private static Ast getAst(Deque<String> tokens, Map<String, Integer> parameters) {
        String token = tokens.removeFirst(); // expect a number or parameter or bra
        Ast ast;
        if (token.equals("(")){ // start of bra & ket
            // ignore bra and process next part
            ast = getAst(tokens, parameters);
            token = tokens.removeFirst(); // expect a ket
            if (!token.equals(")"))
                throw new IllegalArgumentException("Program unbalanced bra - no matching ket");
        } else {
            int number;
            String command = "imm";
            try {
                number = Integer.parseInt(token);
            } catch (NumberFormatException e) {
                command = "arg";
                number = parameters.get(token); // assume no invalid tokens!
            }
            ast = new UnOp(command, number);
        }
        // now check for binary operators or end of expression (ket or $)
        String peek = tokens.peekFirst();
        assert peek != null; // as we expect at least the end of token character
        if (!peek.equals(")") && !peek.equals("$")){
            // part of a binary operation
            String binary = tokens.removeFirst(); // expect a '+', '-', '*' or '/'
            ast = new BinOp(binary, ast, getAst(tokens, parameters));
        }
        return ast;
    }

    /**
     * Returns an AST with constant expressions reduced
     */
    public Ast pass2(Ast ast) {
        System.out.println("Pass2 not yet implemented");
        return ast;
    }

    /**
     * Returns assembly instructions
     */
    public List<String> pass3(Ast ast) {
        List<String> code = new ArrayList<>();
        extractCodeFromAst(code, ast);
        return code;
    }

    private static void extractCodeFromAst(List<String> code, Ast ast) {
        String command =  "";
        String op = ast.op();
        if (ast instanceof UnOp unOp) {
            int number = unOp.n();
            command = "IM";
            if (!op.equals("imm"))
                command = "AR";
            command += " " + number;
        } else if (ast instanceof BinOp binOp) {
            // process left
            extractCodeFromAst(code, binOp.a());
            // push left to stack
            code.add("PU");
            // process right
            extractCodeFromAst(code, binOp.b());
            // swap right to R1
            code.add("SW");
            // pop left off stack
            code.add("PO");
            command = "AD"; // add R1 to R0 and put the result in R0
            if (!op.equals("+")) {
                command = "SU"; // subtract R1 from R0 and put the result in R0
                if (!op.equals("-")) {
                    command = "MU"; // multiply R0 by R1 and put the result in R0
                    if (!op.equals("*")) {
                        command = "DI"; // divide R0 by R1 and put the result in R0
                        if (!op.equals("/")) {
                            throw new IllegalArgumentException("Program contains unrecognised binary operator: " + op);
                        }
                    }
                }
            }
        }
        code.add(command);
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