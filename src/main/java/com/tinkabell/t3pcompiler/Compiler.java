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
        return getAst(tokens, parameters);
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

    /**
     * Generate an int to represent the token:
     * Token is one of:
     *    operator: "-", "+", "/", "*" - in order of precedence
     *    bra-ket: "(", ")"
     *    number: decimal digits only
     *    parameter: alpha character only
     * These are mutually exclusive characters,
     * so we can generate a number token from the index in "()-+/*".
     * If not matched then return -1 as not operator.
     */
    private static int getTokenType(String token) {
        return "()-+/*".indexOf(token);
    }

    /**
     * Thus must process the expression following BODMAS.
     * Using the Shunting Yard Algorithm
     * * while there are tokens to be read:
     *   * read a token
     *     * if the token is:
     *       * a number:
     *       * put it into the output queue
     *     * an operator o1:
     *       * while (there is an operator o2 other than the left parenthesis at the top of the operator stack, and (o2 has greater or equal precedence than o1 as o1 is left-associative)):
     *         * pop o2 from the operator stack into the output queue
     *       * push o1 onto the operator stack
     *       * a left parenthesis - bra (i.e. "("):
     *         * push it onto the operator stack
     *       * a right parenthesis (i.e. ")"):
     *         * while (the operator at the top of the operator stack is not a left parenthesis):
     *           * pop the operator from the operator stack into the output queue
     *         * pop the left parenthesis from the operator stack and discard it
     * * while there are tokens on the operator stack:
     *   * pop the operator from the operator stack onto the output queue
     *
     * @param tokens - the expression as tokens
     * @param parameters - list of the parameter names defined
     *
     * @return and AST holding the full tree
     */
    private static Ast getAst(Deque<String> tokens, Map<String, Integer> parameters) {
        // String token = tokens.removeFirst(); // expect a number or parameter or bra
        Ast ast = null; // to keep compiler happy!
        // in the algorithm this is a queue,
        // but we're going to condense the output down to 1 Ast
        Stack<Ast> outputQueue = new Stack<>();
        Stack<String> operatorStack = new Stack<>();
        int tokenType;
        int number;
        String astOperator;
        String token = tokens.pollFirst();
        while(token != null && !token.equals("$")) {
            tokenType = getTokenType(token);
            if (tokenType < 0) {
                astOperator = "imm";
                try {
                    number = Integer.parseInt(token);
                } catch (NumberFormatException e) {
                    astOperator = "arg";
                    number = parameters.get(token); // assume no invalid tokens!
                }
                ast = new UnOp(astOperator, number);
            }
            //noinspection EnhancedSwitchMigration
            switch (tokenType) {
                case -1: // number or parameter
                    outputQueue.push(ast);
                    break;
                case 2:
                case 3:
                case 4:
                case 5: // an operator
                    while (!operatorStack.empty() && getTokenType(operatorStack.peek()) >= tokenType) {
                        pushAstOnOutputQueue(outputQueue, operatorStack);
                    }
                    operatorStack.push(token);
                    break;
                case 0: // bra
                    operatorStack.push(token);
                    break;
                case 1: // ket
                    while (!operatorStack.empty() && !operatorStack.peek().equals("(")) {
                        pushAstOnOutputQueue(outputQueue, operatorStack);
                    }
                    operatorStack.pop(); // pop the left parenthesis and discard it
                    break;
            }
            token = tokens.pollFirst();
        }
        while (!operatorStack.empty()) {
            pushAstOnOutputQueue(outputQueue, operatorStack);
        }
        /*
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
        */
        return outputQueue.pop(); // should be only thing left there
    }

    private static void pushAstOnOutputQueue(Stack<Ast> outputQueue, Stack<String> operatorStack) {
        String astOperator = operatorStack.pop();
        Ast ast = outputQueue.pop(); // right hand parameter
        ast = new BinOp(astOperator, outputQueue.pop(), ast);
        outputQueue.push(ast);
        // System.out.println("pushed onto output: " + ast);
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

