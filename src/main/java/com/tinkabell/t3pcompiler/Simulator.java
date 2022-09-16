package com.tinkabell.t3pcompiler;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

class Simulator {
    public static int simulate(List<String> asm, int... argv) {
        int r0 = 0;
        int r1 = 0;
        Deque<Integer> stack = new LinkedList<>();
        for (String ins : asm) {
            String code = ins.replaceAll("\\s+[0-9]+", "");
            final int trimmedR0 = Integer.parseInt(ins.substring(2).trim());
            switch (code) {
                case "IM" -> r0 = trimmedR0;
                case "AR" -> r0 = argv[trimmedR0];
                case "SW" -> {
                    int tmp = r0;
                    r0 = r1;
                    r1 = tmp;
                }
                case "PU" -> stack.addLast(r0);
                case "PO" -> r0 = stack.removeLast();
                case "AD" -> r0 += r1;
                case "SU" -> r0 -= r1;
                case "MU" -> r0 *= r1;
                case "DI" -> r0 /= r1;
            }
        }
        return r0;
    }
}