# Tiny Three-Pass Compiler
You are writing a three-pass compiler for a simple programming language into a small assembly language.

The programming language has this syntax:
```
    function   ::= '[' arg-list ']' expression

    arg-list   ::= /* nothing */
                 | variable arg-list

    expression ::= term
                 | expression '+' term
                 | expression '-' term

    term       ::= factor
                 | term '*' factor
                 | term '/' factor

    factor     ::= number
                 | variable
                 | '(' expression ')'
```
Variables are strings of alphabetic characters. 
Numbers are strings of decimal digits representing integers. 
So, for example, a function which computes a2 + b2 might look like:
```
    [ a b ] a*a + b*b
```
A function which computes the average of two numbers might look like:
```
    [ first second ] (first + second) / 2
```
You need write a three-pass compiler. 
All test cases will be valid programs, so you needn't concentrate on error-handling.

The first pass will be the method pass1 
which takes a string representing a function in the original programming language and 
will return a (JSON) object that represents that Abstract Syntax Tree. 
The Abstract Syntax Tree must use the following representations:
```
// Each node type implements interface 'Ast' and has the
// following methods:
// interface Ast has method 'op()' returning 'String'
// BinOp has methods 'a()' and 'b()', both return 'Ast'
// UnOp has method 'n()' returning 'int'
new BinOp('+', a, b)       // add subtree a to subtree b
new BinOp('-', a, b)       // subtract subtree b from subtree a
new BinOp('*', a, b)       // multiply subtree a by subtree b
new BinOp('/', a, b)       // divide subtree a from subtree b
new UnOp('arg', n)         // reference to n-th argument, n integer
new UnOp('imm', n)         // immediate value n, n integer
```
Note: arguments are indexed from zero. So, for example, the function

`[ x y ] ( x + y ) / 2` would look like:
```
new BinOp("/", new BinOp("+", new UnOp("arg", 0), new UnOp("arg", 1)), new UnOp("imm", 2))
```
The second pass of the compiler will be called pass2. 
This pass will take the output from pass1 and 
return a new Abstract Syntax Tree (with the same format) 
with all constant expressions reduced as much as possible. 
So, if for example, the function is `[ x ] x + 2*5`, the result of pass1 would be:
```
new BinOp("+", new UnOp("arg", 0), new BinOp("*", new UnOp("imm", 2), new UnOp("imm", 5)))
```
This would be passed into pass2 which would return:
```
new BinOp("+", new UnOp("arg", 0), new UnOp("imm", 10))
```
The third pass of the compiler is pass3. 
The pass3 method takes in an Abstract Syntax Tree and returns an array of strings. 
Each string is an assembly directive. 
You are working on a small processor 
with two registers (R0 and R1), a stack, and an array of input arguments. 
The result of a function is expected to be in R0. 
The processor supports the following instructions:
```
    "IM n"     // load the constant value n into R0
    "AR n"     // load the n-th input argument into R0
    "SW"       // swap R0 and R1
    "PU"       // push R0 onto the stack
    "PO"       // pop the top value off of the stack into R0
    "AD"       // add R1 to R0 and put the result in R0
    "SU"       // subtract R1 from R0 and put the result in R0
    "MU"       // multiply R0 by R1 and put the result in R0
    "DI"       // divide R0 by R1 and put the result in R0
```
So, one possible return value from pass3 given the Abstract Syntax Tree shown above from pass2 is:
```
    [ "IM 10", "SW", "AR 0", "AD" ]
```
Here is a simulator for the target machine. 
It takes an array of assembly instructions and an array of arguments and returns the result.
```java
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
           switch (code) {
              case "IM": r0 = Integer.parseInt(ins.substring(2).trim()); break;
              case "AR": r0 = argv[Integer.parseInt(ins.substring(2).trim())]; break;
              case "SW": int tmp = r0; r0 = r1; r1 = tmp; break;
              case "PU": stack.addLast(r0); break;
              case "PO": r0 = stack.removeLast(); break;
              case "AD": r0 += r1; break;
              case "SU": r0 -= r1; break;
              case "MU": r0 *= r1; break;
              case "DI": r0 /= r1; break;
           }
        }
        return r0;
    }
}
```
## The bit I was missing

I needed to process the "expression" as BODMAS.  
Found an algorithm for [Shunting yard algorithm on wikipedia](https://en.wikipedia.org/wiki/Shunting_yard_algorithm).
I made a simplified version as we only have B-DMAS and all operators are all left-associative ...

* while there are tokens to be read:
  * read a token
    * if the token is:
      * a number:
      * put it into the output queue
    * an operator o1:
      * while (there is an operator o2 other than the left parenthesis at the top of the operator stack, and (o2 has greater or equal precedence than o1 as o1 is left-associative)):
        * pop o2 from the operator stack into the output queue
      * push o1 onto the operator stack
      * a left parenthesis - bra (i.e. "("):
        * push it onto the operator stack
      * a right parenthesis (i.e. ")"):
        * while (the operator at the top of the operator stack is not a left parenthesis):
          * pop the operator from the operator stack into the output queue
        * pop the left parenthesis from the operator stack and discard it
* while there are tokens on the operator stack:
  * pop the operator from the operator stack onto the output queue
