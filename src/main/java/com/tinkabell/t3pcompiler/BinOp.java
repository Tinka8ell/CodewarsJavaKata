package com.tinkabell.t3pcompiler;

public class BinOp implements Ast {

    /**
     * new BinOp('+', a, b)       // add subtree a to subtree b
     * new BinOp('-', a, b)       // subtract subtree b from subtree a
     * new BinOp('*', a, b)       // multiply subtree a by subtree b
     * new BinOp('/', a, b)       // divide subtree a from subtree b
     */
    public BinOp(String  op, Ast a, Ast b) {
    }

    public Ast a(){
        return null;
    }
    public Ast b(){
        return null;
    }

    @Override
    public String op() {
        return null;
    }
}
