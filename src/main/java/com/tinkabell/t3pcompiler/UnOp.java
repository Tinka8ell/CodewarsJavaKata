package com.tinkabell.t3pcompiler;

public class UnOp implements Ast{

    /**
     * new UnOp('arg', n)         // reference to n-th argument, n integer
     * new UnOp('imm', n)         // immediate value n, n integer
     */
    public UnOp(String command, int n) {
    }

    public int n() {
        return 0;
    }

    @Override
    public String op() {
        return null;
    }
}
