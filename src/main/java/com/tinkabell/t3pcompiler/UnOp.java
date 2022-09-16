package com.tinkabell.t3pcompiler;

public class UnOp implements Ast{

    private final String command;
    private final int value;


    /**
     * new UnOp('arg', n)         // reference to n-th argument, n integer
     * new UnOp('imm', n)         // immediate value n, n integer
     */
    public UnOp(String command, int n) {
        this.command = command;
        value = n;
    }

    public int n() {
        return value;
    }

    @Override
    public String op() {
        return this.command;
    }

    @Override
    public String toString() {
        return "{'op':'" + command + "','value':" + value + "}";
    }
}
