package com.tinkabell.t3pcompiler;

import java.util.Objects;

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

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        return toString().equals(o.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(command, value);
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
