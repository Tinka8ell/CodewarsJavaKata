import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    public enum OpType{
        CONSTANT, // a number
        NAME, // a variable name
        SUM, // + / - two values
        PRODUCT, // * two values
        QUOTIENT, // / two values
        CHAIN // function of function
    }

    /**
     * Represent the prefix notation
     */
    public static class Function {
        private static final Function MINUS_ONE = new Function("-1");
        private static final Function ZERO = new Function("0");
        private static final Function ONE = new Function("1");

        private String name;
        private boolean isSimple = true;
        private Function left;
        private Function right;
        private Double constant;
        private OpType opType;

        /**
         * Create an end node of the function tree
         * This is a constant or a variable name (probably 'x')
         *
         * @param action a String name or a number as a String
         */
        public Function(String action){
            name = action; // constant or variable
            try {
                constant = Double.parseDouble(action);
                opType = OpType.CONSTANT;
            }
            catch (NumberFormatException e){
                // it wasn't a number
                opType = OpType.NAME;
            }
        }

        /**
         * Create a function that takes one argument
         *
         * @param action name of the function
         * @param leftPart a function for the one argument
         */
        public Function(String action, Function leftPart){
            name = action; // constant or variable
            left = leftPart;
            opType = OpType.CHAIN;
            isSimple = false;
        }

        /**
         * Create an expression combining two arguments
         * For commutative operations the two arguments can be swapped
         *
         * @param action the operation code
         * @param leftPart the argument the operation is applied to
         * @param rightPart the argument that is applied in the operation
         */
        public Function(String action, Function leftPart, Function rightPart){
            name = action;
            left = leftPart;
            right = rightPart;
            switch (action){
                case "+":
                case "-":
                    opType = OpType.SUM;
                    break;
                case "*":
                case "/":
                    opType = OpType.PRODUCT;
                    break;
                default: // "^"
                    opType = OpType.CHAIN;
            }
            isSimple = false;
        }

        private static Character getNext(List<Character> rest) {
            return rest.size() > 0 ? rest.remove(0) : ')';
        }

        private static Character getNextNoneBlank(List<Character> rest) {
            Character test = getNext(rest);
            while (test == ' ')
                test = getNext(rest);// skip blanks
            return test;
        }

        private static String getNextWord(List<Character> rest) {
            Character test = getNextNoneBlank(rest);
            while (test == ' ')
                test = getNext(rest);// skip blanks
            StringBuilder word = new StringBuilder();
            while (test != ' ' && test != ')'){
                word.append(test);
                test = getNext(rest);
            }
            // as we aren't using it, put it back!
            rest.add(0, test);
            return word.toString();
        }

        public static Function parse(List<Character> rest) {
            Character test = getNextNoneBlank(rest);
            if (test == '(') {
                // starting an expression
                String action = getNextWord(rest);
                if (action.length() == 1) {
                    if ("+-*/^".indexOf(action) != -1) {
                        // has two arguments and closing ket
                        Function leftPart = Function.parse(rest);
                        Function rightPart = Function.parse(rest);
                        while (test != ')') // lazy as starts as '(' ...
                            test = getNext(rest);// skip to end ket
                        return new Function(action, leftPart, rightPart);
                    } else {
                        // we have wrapped variable or constant so unwrap it
                        return parse(rest);
                    }
                } else {
                    // otherwise, it is a function with one argument
                    Function leftPart = Function.parse(rest);
                    return new Function(action, leftPart);
                }
            } else {
                // got a var or const
                rest.add(0, test); // put test back so we can read it as a word...
                String action = getNextWord(rest);
                return new Function(action);
            }
        }

        @Override
        public String toString() {
            if (right == null){
                if (left == null){
                    return name;
                } else {
                    return "( " + name + " " + left.toString() + " )";
                }
            } else {
                if (left == null){
                    return "( " + name + " " + right.toString() + " )";
                } else {
                    return "( " + name + " " + left.toString() + " " + right.toString() + " )";
                }
            }
        }

        private String getNumber(Double number) {
            if (number == number.longValue())
                return Long.toString(number.longValue());
            return number.toString();
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
         *
         * @return simplified function
         */
        public Function simplify(){
            // short circuit if we can
            if (isSimple)
                return this;

            // not so simple so at least normalise
            if (left == null)
                left = ZERO;
            else if (!left.isSimple)
                left = left.simplify();
            if (right == null)
                right = ZERO;
            else if (!right.isSimple)
                right = right.simplify();

            // now go deeper
            Function simple = this;
            switch (opType){
                /* these are already excluded
                case NAME:
                case CONSTANT:
                    // as simple as it gets
                    break;
                 */
                case SUM:
                    if (right.opType == OpType.CONSTANT){
                        if (left.opType == OpType.CONSTANT){
                            // if sum of two numbers, then do the arithmetic
                            double result = right.constant;
                            if (name.equals("-"))
                                result = - result;
                            result += left.constant;
                            simple = new Function(getNumber(result));
                        } else if (name.equals("+")){
                            // else put constant first
                            Function swap = right;
                            right = left;
                            left = swap;
                        }
                    }
                    if (left.opType == OpType.CONSTANT && left.constant == 0)
                        simple = right; // adding 0 does nothing!
                    break;
                case PRODUCT:
                    // similar to sum
                    if (right.opType == OpType.CONSTANT){
                        if (left.opType == OpType.CONSTANT){
                            // if product of two numbers, then do the arithmetic
                            double result = left.constant * right.constant;
                            simple = new Function(getNumber(result));
                        } else if (name.equals("+")){
                            // else put constant first
                            Function swap = right;
                            right = left;
                            left = swap;
                        }
                        if (left.opType == OpType.CONSTANT && left.constant == 0)
                            simple = right; // adding 0 does nothing!
                    }
                    if (left.opType == OpType.CONSTANT) {
                        if (left.constant == 0)
                            simple = ZERO; // nothing times anything is nothing!
                        else if (left.constant == 1)
                            simple = right; // one times something is just something!
                    }
                    break;
            }
            return simple;
        }

        /**
         * Differentiate the given function
         *
         * Derivation follows these rule:
         * Sum rule:
         *   ( a.f(x) + b.g(x) ) ' = a.f'(x) + b.g'(x)
         *
         * Product rule:
         *   ( f(x)∙g(x) ) ' = f'(x).g(x) + f(x).g'(x)
         *
         * Derivative quotient rule
         *   ( f(x)/g(x) )' = ( f'(x).g(x) - f(x).g'(x) )/g^2(x)
         *
         * Derivative chain rule
         *   f(g(x))' = f'(g(x))∙g'(x)
         *
         *  Actually:
         *   ( f(x)/g(x) )' = ( f(x).(g(x)^-1) )'
         *  product rule so = f'(x).(g(x)^-1) + f(x).(g(x)^-1)'
         *  chain rule   so = f'(x).(g(x)^-1) + f(x).( -1.(g(x))^-2 ).(g'(x))
         *  reorder      or = f'(x).g(x).(g(x)^-2) - f(x).g'(x).(g(x)^-2)
         *               or = (f'(x).g(x) - f(x)g'(x)).(g(x)^-2)
         *                  = (f'(x).g(x) - f(x).g'(x))/g^2(x)
         *
         * with the following arithmetic rules:
         * Constant:    const -> 0
         * Linear:      x     -> 1
         * Power:       x^a   -> a.x^(a-1)
         * Exponential: e^x -> e^x
         * Natural log: ln(x) -> 1/x
         * Sine:        sin(x) -> cos(x)
         * Cosine:      cos(x) -> -sin(x)
         * Tangent:     tan(x) -> 1 / cos^2(x)
         *
         * @return differentiated function
         */
        public Function differentiate (){
            Function diff = null;
            switch (opType){
                case CONSTANT: // a number
                    diff = ZERO; // end of the line
                    break;
                case NAME: // a variable
                    diff = ONE; // replace with a one
                    break;
                case CHAIN: // a real function
                    // Derivative chain rule
                    //   f(g(x))' = f'(g(x))∙g'(x)
                    // f -> name (with right), g -> left
                    //   f(g(x))' -> new name = "*", new left = f'(g(x)), new right = g'(x)
                    Function newLeft = null; //f'(g(x))
                    switch (name){ // derive left
                        case "^": // to a power: x^a   -> a.x^(a-1)
                            // x = left, a = right
                            // => (* a (^ x, (+ a -1)))
                            Function newPower = new Function("+", right, MINUS_ONE);
                            Function newRight = new Function("^", left, newPower);
                            newLeft = new Function("*", right, newRight);
                            break;
                    }
                    diff = new Function("*", newLeft, left.differentiate() );
                    break;
                case SUM: // a sum
                    // Sum rule:
                    //   ( a.f(x) + b.g(x) ) ' = a.f'(x) + b.g'(x)
                    // remember to keep the name (+ / -)!
                    diff = new Function(name, left.differentiate(), right.differentiate() );
            }
            return diff.simplify();
        }
    }

    private static  Pattern starter = Pattern.compile("^[ (]+(.*)$"); // start expressions characters
    private static  Pattern ender = Pattern.compile("^[ )]+(.*)$"); // end expression characters
    private static  Pattern argument = Pattern.compile("^([^ ()]+)(.*)$"); // argument characters
    private static  Pattern expressionPattern = Pattern.compile("^ *\\( *([-+/*^a-z0-9]+)(.*) *\\) *$");
    private static  Pattern constantPattern = Pattern.compile("^ *([a-z0-9]+)(.*) *$");
    private static  Pattern variablePattern = Pattern.compile("^ *([a-z]+)(.*) *$");

    /**
     * Starting from f(x) generate f'(x).
     *
     * f(x) is in th form of:
     *   constant, variable name (expect "x") or an expression :
     *   "(" [" "] <action> [ " " <expression> [ " " <expression> ] ] [" "] ")"
     * where action is function keyword, and operator or variable name (expect "x")
     * so matches regex: "[a-z0-9+-*^/]+" - lowercase alpha, digit or op symbols
     * " " - spaces separate everywhere they are required and
     * bra and ket ("(", ")") are always paired.  What could go wrong!
     *
     * @param expr String representing of f(x)
     * @return String representing of f'(x)
     */
    public String diff(String expr) {
        // parse it
        Function function = Function.parse(expr.chars().mapToObj(e -> (char)e).collect(Collectors.toList()));
        // differentiate
        function = function.differentiate();
        // return to string
        return function.toString();
    }
}

