package com.tinkabell.t3pcompiler;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class CompilerTest {

    /**
     * [ ] 0
     * would look like:
     * new UnOp("imm", 0)
     * which looks like:
     * {'op':'imm','value':0}
     */
    @Test
    public  void testMinimal(){
        String program = "[ ] 0";
        Compiler compiler = new Compiler();
        String expected = "{'op':'imm','value':0}";

        Ast t1 = new UnOp("imm", 0);
        assertEquals("Pass 1 as JSON", expected, t1.toString());

        Ast p1 = compiler.pass1(program);
        assertEquals("Pass 1", t1, p1);

        // This is a no-op as there is nothing to simplify
        Ast p2 = compiler.pass2(p1);
        assertEquals("Pass 2", t1, p2);

        List<String> p3 = compiler.pass3(p2);
        assertEquals("program() == 0", 0, Simulator.simulate(p3));
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
