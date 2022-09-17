package com.tinkabell.t3pcompiler;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args){
        String program = args[0];
        int[] parameters = Arrays.stream(args)
                .skip(1) // remove program
                .mapToInt(Integer::parseInt) // convert to ints
                .toArray();
        Compiler compiler = new Compiler();
        List<String> pass3 = compiler.compile(program);
        int result = Simulator.simulate(pass3, parameters);
        System.out.println("Result is " + result);
    }
}
