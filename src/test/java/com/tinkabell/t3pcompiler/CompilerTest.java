package com.tinkabell.t3pcompiler;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CompilerTest {

    /**
     * [ ] n
     * would look like:
     * new UnOp("imm", n)
     * which looks like:
     * {'op':'imm','value':n}
     * where n is a non-negative integer
     */
    @ParameterizedTest
    @ValueSource(strings = {"0", "1", "99", "" + Integer.MAX_VALUE /* , "-1", "" + Integer.MIN_VALUE} are invalid inputs*/ })
    public void testMinimalInt(String n){
        String program = "[ ] " + n;
        int output = Integer.parseInt(n);
        Compiler compiler = new Compiler();
        String expected = "{'op':'imm','value':" + n + "}";

        Ast t1 = new UnOp("imm", output);
        assertEquals("Pass 1 as JSON", expected, t1.toString());

        Ast p1 = compiler.pass1(program);
        assertEquals("Pass 1", t1, p1);

        // This is a no-op as there is nothing to simplify
        Ast p2 = compiler.pass2(p1);
        assertEquals("Pass 2", t1, p2);

        List<String> p3 = compiler.pass3(p2);
        assertEquals("program() == " + n, output, Simulator.simulate(p3));
    }

    /**
     * [ a ] a
     * would look like:
     * new UnOp("arg", 0)
     * which looks like:
     * {'op':'arg','value':0}
     * where a is a valid parameter name
     * For any parameter n (is an integer) it will generate that number when run
     */
    @ParameterizedTest
    @CsvSource({
            "a, 1",
            "b, -2",
            "aVeryLongName, 99",
            "maxInt, " + Integer.MAX_VALUE,
            "minInt, " + Integer.MIN_VALUE,
            })
    public void testMinimalVar(String var, int n){
        String program = "[ " + var + " ] " + var;
        Compiler compiler = new Compiler();
        String expected = "{'op':'arg','value':0}";
        int[] parameters = { n };

        Ast t1 = new UnOp("arg", 0);
        assertEquals("Pass 1 as JSON", expected, t1.toString());

        Ast p1 = compiler.pass1(program);
        assertEquals("Pass 1 for " + program, t1, p1);

        // This is a no-op as there is nothing to simplify
        Ast p2 = compiler.pass2(p1);

        List<String> p3 = compiler.pass3(p2);

        assertEquals("program() == " + n, n, Simulator.simulate(p3, parameters));
    }

    /**
     * [ ] ( n )
     * would look like:
     * new UnOp("imm", n)
     * which looks like:
     * {'op':'imm','value':n}
     * where n is a non-negative integer
     * or:
     * [ a ] ( a )
     * would look like:
     * new UnOp("arg", 0)
     * which looks like:
     * {'op':'arg','value':0}
     * where a is a valid parameter name
     * For any parameter number (is an integer) it will generate that number when run
     */
    @ParameterizedTest
    @CsvSource({
            "a, 1",
            "b, -2",
            "aVeryLongName, 99",
            "maxInt, " + Integer.MAX_VALUE,
            "minInt, " + Integer.MIN_VALUE,
            "0 , 0",
            "1 , 0",
            "99 , 0",
            Integer.MAX_VALUE + " , 0",
    })
    public void testMinimalBraKet(String var, int n){
        // if n == 0, assume no parameter and var is the expression
        int output = n;
        if (n == 0)
            output = Integer.parseInt(var);
        String program = "[ " + var + " ] ( " + var + " ) ";
        if (n == 0)
            program = "[ ] ( " + var + " ) ";
        Compiler compiler = new Compiler();
        String expected = "{'op':'arg','value':0}";
        if (n == 0)
            expected = "{'op':'imm','value':" + var + "}";
        int[] parameters = { n };

        Ast t1 = new UnOp("arg", 0);
        if (n == 0)
            t1 = new UnOp("imm", output);
        assertEquals("Pass 1 as JSON", expected, t1.toString());

        Ast p1 = compiler.pass1(program);
        assertEquals("Pass 1 for " + program, t1, p1);

        // This is a no-op as there is nothing to simplify
        Ast p2 = compiler.pass2(p1);
        assertEquals("Pass 2 for " + p1, t1, p2);

        List<String> p3 = compiler.pass3(p2);

        if (n == 0)
            assertEquals("program() == " + n, output, Simulator.simulate(p3));
        else
            assertEquals("program() == " + n, n, Simulator.simulate(p3, parameters));
    }

    /**
     * [ x y ] ( x + y ) / 2
     * would look like:
     * new BinOp("/", new BinOp("+", new UnOp("arg", 0), new UnOp("arg", 1)), new UnOp("imm", 2))
     * which looks like:
     * {'op':'/','a':{'op':'+','a':{'op':'arg','value':0},'b':{'op':'arg','value':1}},'b':{'op':'imm','value':2}}
     */
    @ParameterizedTest
    @CsvSource({
            "x, 1, y, 3, 2",
    })
    public  void testSimpleCompile(String left, int leftValue, String right, int rightValue, int result){
        String program = "[ " + left + " " + right + " ] ( " + left + " + " + right + " ) / 2 " ;
        Compiler compiler = new Compiler();
        String expected = "{'op':'/','a':{'op':'+','a':{'op':'arg','value':0},'b':{'op':'arg','value':1}},'b':{'op':'imm','value':2}}";
        int[] parameters = { leftValue, rightValue };

        Ast t1 = new BinOp("/", new BinOp("+", new UnOp("arg", 0), new UnOp("arg", 1)), new UnOp("imm", 2));
        assertEquals("Pass 1 as JSON", expected, t1.toString());

        Ast p1 = compiler.pass1(program);
        assertEquals("Pass 1 for " + program, t1, p1);

        // This is a no-op as there is nothing to simplify
        Ast p2 = compiler.pass2(p1);
        assertEquals("Pass 2 for " + p1, t1, p2);

        List<String> p3 = compiler.pass3(p2);
        System.out.println("Pass3:");
        for (String line: p3) {
            System.out.println(">>> " + line);
        }
        assertNotEquals(0, p3.size());
        assertNotEquals(0, p3.get(0).trim().length());
        assertEquals("program() == " + leftValue + ", " + rightValue, result, Simulator.simulate(p3, parameters));
    }

    /**
     * [ x y ] ( x + y ) / 2
     * would look like:
     * new BinOp("/", new BinOp("+", new UnOp("arg", 0), new UnOp("arg", 1)), new UnOp("imm", 2))
     * which looks like:
     * {'op':'/','a':{'op':'+','a':{'op':'arg','value':0},'b':{'op':'arg','value':1}},'b':{'op':'imm','value':2}}
     */
    @Test
    public  void testSimplePass1(){
        String program = "[ x y ] ( x + y ) / 2 ";
        Compiler compiler = new Compiler();
        String expected = "{'op':'/','a':{'op':'+','a':{'op':'arg','value':0},'b':{'op':'arg','value':1}},'b':{'op':'imm','value':2}}";

        Ast t1 = new BinOp("/", new BinOp("+", new UnOp("arg", 0), new UnOp("arg", 1)), new UnOp("imm", 2));
        assertEquals("Pass 1 as JSON", expected, t1.toString());

        Ast p1 = compiler.pass1(program);
        assertEquals("Pass 1", t1, p1);
    }

    @Ignore
    @Test
    public void testSimpleProg() {
        String program = "[ x y z ] ( 2*3*x + 5*y - 3*z ) / (1 + 3 + 2*2)";
        Compiler compiler = new Compiler();

        // {'op':'/','a':{'op':'-','a':{'op':'+','a':{'op':'*','a':{'op':'*','a':{'op':'imm','n':2},'b':{'op':'imm','n':3}},'b':{'op':'arg','n':0}},'b':{'op':'*','a':{'op':'imm','n':5},'b':{'op':'arg','n':1}}},'b':{'op':'*','a':{'op':'imm','n':3},'b':{'op':'arg','n':2}}},'b':{'op':'+','a':{'op':'+','a':{'op':'imm','n':1},'b':{'op':'imm','n':3}},'b':{'op':'*','a':{'op':'imm','n':2},'b':{'op':'imm','n':2}}}}
        Ast t1 = new BinOp("/", new BinOp("-", new BinOp("+", new BinOp("*", new BinOp("*", new UnOp("imm", 2), new UnOp("imm", 3)), new UnOp("arg", 0)), new BinOp("*", new UnOp("imm", 5), new UnOp("arg", 1))), new BinOp("*", new UnOp("imm", 3), new UnOp("arg", 2))), new BinOp("+", new BinOp("+", new UnOp("imm", 1), new UnOp("imm", 3)), new BinOp("*", new UnOp("imm", 2), new UnOp("imm", 2))));
        Ast p1 = compiler.pass1(program);
        assertEquals("Pass 1", t1, p1);

        // {'op':'/','a':{'op':'-','a':{'op':'+','a':{'op':'*','a':{'op':'imm','n':6},'b':{'op':'arg','n':0}},'b':{'op':'*','a':{'op':'imm','n':5},'b':{'op':'arg','n':1}}},'b':{'op':'*','a':{'op':'imm','n':3},'b':{'op':'arg','n':2}}},'b':{'op':'imm','n':8}}
        Ast t2 = new BinOp("/", new BinOp("-", new BinOp("+", new BinOp("*", new UnOp("imm", 6), new UnOp("arg", 0)), new BinOp("*", new UnOp("imm", 5), new UnOp("arg", 1))), new BinOp("*", new UnOp("imm", 3), new UnOp("arg", 2))), new UnOp("imm", 8));
        Ast p2 = compiler.pass2(p1);
        assertEquals("Pass 2", t2, p2);

        List<String> p3 = compiler.pass3(p2);
        assertEquals("program(4,0,0) == 3", 3, Simulator.simulate(p3, 4, 0, 0));
        assertEquals("program(4,8,0) == 8", 8, Simulator.simulate(p3, 4, 8, 0));
        assertEquals("program(4,8,16) == 2", 2, Simulator.simulate(p3, 4, 8, 16));
    }
}
