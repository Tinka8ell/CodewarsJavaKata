package com.tinkabell.t3pcompiler;

import java.util.Objects;

public class BinOp implements Ast {

    private final String command;
    private final Ast left;
    private final Ast right;

    /**
     * new BinOp('+', a, b)       // add subtree a to subtree b
     * new BinOp('-', a, b)       // subtract subtree b from subtree a
     * new BinOp('*', a, b)       // multiply subtree a by subtree b
     * new BinOp('/', a, b)       // divide subtree a from subtree b
     */
    public BinOp(String  op, Ast a, Ast b) {
        command = op;
        left = a;
        right = b;
    }

    public Ast a(){
        return left;
    }
    public Ast b(){
        return right;
    }

    @Override
    public String op() {
        return command;
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        return toString().equals(o.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(command, left, right);
    }

    /*
     * {'op':'?','a':?,'b':?}
     */
    @Override
    public String toString() {
        return "{'op':'" + command + "','a':" + left + ",'b':" + right + "}";
    }
}
