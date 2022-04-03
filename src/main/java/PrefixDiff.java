import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Symbolic differentiation of prefix expressions
 *
 * In this kata your task is to differentiate a mathematical expression given as a string in prefix notation.
 * The result should be the derivative of the expression returned in prefix notation.
 *
 * To simplify things we will use a simple list format made up of parenthesis and spaces.
 *
 * The expression format is (func arg1) or (op arg1 arg2) where op means operator,
 * func means function and arg1, arg2 are arguments to the operator or function.
 * For example (+ x 1) or (cos x)
 *
 *   The expressions will always have balanced parenthesis and with spaces between list items.
 *   Expression operators, functions and arguments will all be lowercase.
 *   Expressions are single variable expressions using x as the variable.
 *   Expressions can have nested arguments at any depth for example (+ (* 1 x) (* 2 (+ x 1)))
 *
 * Examples of prefix notation in this format:
 * * (+ x 2)        // prefix notation version of x+2
 * * (* (+ x 3) 5)  // same as 5 * (x + 3)
 * * (cos (+ x 1))  // same as cos(x+1)
 * * (^ x 2)        // same as x^2 meaning x raised to power of 2
 *
 *  The operators and functions you are required to implement are + - * / ^ cos sin tan exp ln where
 *  ^ means raised to power of,
 *  exp is the exponential function (same as e^x) and
 *  ln is the natural logarithm (base e).
 *
 * Example of input values and their derivatives:
 * * (* 1 x) => 1
 * * (^ x 3) => (* 3 (^ x 2))
 * * (cos x) => (* -1 (sin x))
 *
 *  In addition to returning the derivative your solution must also do some simplifications of the result
 *  but only what is specified below.
 * * The returned expression should not have necessary 0 or 1 factors.
 *   For example it should not return (* 1 (+ x 1)) but simply the term (+ x 1)
 *   similarly it should not return (* 0 (+ x 1)) instead it should return just 0
 * * Results with two constant values such as for example (+ 2 2)
 *   should be evaluated and returned as a single value 4
 * * Any argument raised to the zero power should return 1
 *   and if raised to 1 should return the same value or variable.
 *   For example (^ x 0) should return 1 and (^ x 1) should return x
 *
 * No simplification are expected for functions like cos, sin, exp, ln...
 * (but their arguments might require a simplification).
 *
 * Think recursively and build your answer according to
 * the rules of derivation (http://www.rapidtables.com/math/calculus/derivative.htm) and sample test cases.
 *
 * If you need to diff any test expressions you can use
 * Wolfram Alpha (http://www.wolframalpha.com/) however remember we use prefix format in this kata.
 *
 * Best of luck !
 *
 * Rules (where: a and b are constants, f and g arr functions and "'" means differential)
 *
 * Derivative sum rule
 *   ( a f (x) + bg(x) ) ' = a f ' (x) + bg' (x)
 *
 * Derivative product rule
 *   ( f (x) ∙ g(x) ) ' = f ' (x) g(x) + f (x) g' (x)
 *
 * Derivative quotient rule
 *   ( f(x) / g(x) )' = f'(x)g(x)-f(x)g'(x) / g^2(x)
 *
 * Derivative chain rule
 *   f(g(x))' = f'(g(x)) ∙ g'(x)
 *
 * Constant:
 *   const -> 0
 * Linear:
 *   x -> 1
 * Power:
 *   x^a -> a.x^(a-1)
 * Exponential:
 *   e^x -> e^x
 * Natural logarithm:
 *   ln(x) -> 1/x
 * Sine:
 *   sin(x) -> cos(x)
 * Cosine:
 *   cos(x) -> -sin(x)
 * Tangent:
 *   tan(x) -> 1 / cos^2(x)
 */
class PrefixDiff {

    /**
     * Represent the prefix notation
     */
    public static class Expression {
        private String function;
        private Expression left;
        private Expression right;
        private Double constant;

        /**
         * Create an expression representing either:
         * a variable (usually "x") or
         * a constant (may be a long or a double)
         *
         * @param variable as a String
         */
        public Expression(String variable) {
            this(variable, null, null);
        }

        /**
         * Create an expression representing a function with a single operand
         *
         * @param function a lowercase String name of the function
         * @param left an expression representing the only parameter
         */
        public Expression(String function, Expression left) {
            this(function, left, null);
        }

        /**
         * Create an expression representing a binary operator
         * with two arguments left and right as some operators are ordered (e.g. '-', '/' & '^')
         *
         * @param function a single character String for the operator
         * @param left an expression representing the first parameter
         * @param right an expression representing the second parameter
         */
        public Expression(String function, Expression left, Expression right) {
            this.function = function;
            this.left = left;
            this.right = right;
            constant = null;
            try {
                constant = Double.parseDouble(this.function);
            } catch (NumberFormatException e){
                constant = null;
            }
        }

        public static Expression parse(String expression){
                expression = expression.replaceAll("\\(]", " \\( ")
                        .replaceAll("\\)]", " \\) ")
                        .replaceAll("  +]", " ");
                List<String> parts = Arrays.asList(expression.split(" "));
            List<Expression> result = new ArrayList<>();
            List<String> op = new ArrayList<>();
            List<Integer> expected = new ArrayList<>();
            List<Object> arg = new ArrayList<>();
            int depth = -1;
            while (parts.size() > 0) {
                String part = parts.remove(0);
                switch (part) {
                    case "(": {
                        depth++;
                        break;
                    }
                    case ")": {
                        int count = expected.remove(0);
                        Object right = null;
                        if (count > 0) {
                            right = arg.remove(0);
                            count--;
                        }
                        Object left = null;
                        if (count > 0) {
                            left = arg.remove(0);
                            count--;
                        }
                        arg.add(0, new Expression(op.remove(0), (Expression) left, (Expression) right));
                        break;
                    }
                    case "+":
                    case "-":
                    case "*":
                    case "/":
                    case "^":{
                        op.add(0, part);
                        expected.add(0, 2);
                        break;
                    }
                    case "sin":
                    case "cos":
                    case "tan":
                    case "exp":
                    case "ln": {
                        op.add(0, part);
                        expected.add(0, 1);
                        break;
                    }
                    default: { // variable or constant
                        op.add(0, part);
                        expected.add(0, 0);
                        break;
                    }
                }
            }
            return (Expression) arg.remove(0);
        }

        public boolean isConstant() {
            return (constant != null);
        }

        public boolean isMultiple() {
            return isConstant() ||
                    ( (function == "*") &&
                            left.isConstant() &&
                            right.isFunction()
                    );
        }

        public boolean isFunction() {
            return isName() ||
                    ( (function == "^") &&
                            left.isName() &&
                            right.isSum()
                    );
        }

        public double ofOrder(){
            double order = 0;
            if (!isConstant()){
                if (isSum())
                    order = left.ofOrder(); // lowest order multiple
                else if (isMultiple())
                        order = right.ofOrder(); // order of function part
            }
            return order;
        }

        public String ofName(){
            String name = "";
            if (!isConstant()){
                if ("+-/*^".indexOf(function) != -1){
                    name = function; // a variable
                    if (left != null)
                        name = "(" + function + " " + left.ofName() + ")"; // a function
                } else if (isMultiple()){
                    name = right.ofName();
                } else if (function == "*"){ // a product
                    name = "(* " + left.ofName() + " " + right.ofName() + ")";
                } else {
                    name = left.ofName(); // of first argument
                    if (name.isEmpty())
                        name = right.ofName(); // of second
                    else if (right.ofName() != name)
                        name = "(+ " + name + " " + right.ofName() + ")"; // of both
                }
            }
            return name;
        }

        public boolean isName() {
            return !isConstant() ||
                    ( (function == "*") &&
                            left.isName() &&
                            right.isName() // should really be isName or named sum!
                    );
        }

        public boolean isSum(){
            return isMultiple() ||
                    ( (function == "+") && 
                            left.isMultiple() && 
                            right.isSum()
                    );
        }

        /**
         * Simplify the expressions.
         * A simplified expression is basically an ordered sum of multiples:
         *
         * sum = <multiple> | '(' '+' <multiple> <sum> ')' and is of order of the multiple
         * multiple = <constant> | '(' * <constant> <function> ')' and is of order of the function
         * constant is nonzero number (Long, Double) and is of order 0
         * function = <name> | '(' '^' <name> <sum> ')' and is of order sum
         * name = <named function> | <product>
         * product = '(' * <named function> <named function> ')'
         * named function = <variable name> | '(' <'cos' | 'sin' | 'tan' | 'exp' | 'ln'> <variable sum> ')'
         * variable name = 'x' - basically the unitary function on 'x'
         * variable sum = a sum containing a variable name
         *
         * multiples are "ordered" by their name and order of the power ('^') they are raised to
         * 
         * zero constants remove themselves and any multiple they are part of
         * powers of 1 can be folded to the name they represent
         * product of two equal <named function> is '(' '^' <named function> '2' ')'  
         * '-' is not commutative so: 
         *   '(' '-' <arg1> <arg2> ')' 
         * becomes: 
         *   '(' '+' <arg1> <minus multiple> ')' were 
         *   minus multiple = '(' '*' '-1' <arg2> ')'
         * '/' is not commutative so: 
         *   '(' '/' <arg1> <arg2> ')' 
         * becomes: 
         *   '(' '*' <arg1> <negative power> ')' were 
         *   negative power = '(' '^' <arg2> '-1' ')'
         */
        public void simplify(){
            // remove the non-commutative operators we need to be commutative
            if (function == "-"){
                function = "+";
                right = new Expression("*", new Expression("-1"), right);
            }
            if (function == "/"){
                function = "*";
                right = new Expression("^", right, new Expression("-1"));
            }

        }
    }

    public String diff(String expr) {

        // Method required, return the resulting expression as String

        return "";
    }
}

