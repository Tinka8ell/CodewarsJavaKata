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
 *   For example, it should not return (* 1 (+ x 1)) but simply the term (+ x 1)
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
        CHAIN, // function of function
        UNKNOWN // not yet identified
    }

    /**
     * Operations are one of + - * / ^
     * and take two arguments
     */
    public static String operations = "+-*/^";

    /**
     * Function names are one of:
     *    cos, sin, tan, exp or ln
     * and take one argument
     */
    public enum FunctionName{
        cos, sin, tan, exp, ln, x, n, o
    }

    /**
     * Represent the prefix notation
     */
    public static class Function {
        private static final Function MINUS_ONE = new Function("-1");
        private static final Function ZERO = new Function("0");
        private static final Function ONE = new Function("1");

        private String name = "0";
        private Double constant = 0.0;
        private int intValue = 0;
        private FunctionName functionName = FunctionName.x;
        private OpType opType = OpType.UNKNOWN;
        private Function left = null;
        private Function right = null;
        private boolean isConstant = false;
        private boolean isVariable = false;

        /**
         * Return the "power" of this function
         * Constant power = 0 base = ZERO
         * Variable power = 1 base = ONE
         *
         * @return an integer power level
         */
        public int getPower(){
            int power;
            switch (functionName){
                case n:
                    power = 0;
                    break;
                case o:
                    if (name.equals("+") || name.equals("-"))
                        power = left.getPower();
                    else if (name.equals("*") || name.equals("/"))
                        power = right.getPower();
                    else
                        power = right.intValue;
                    break;
                default:
                    power = 1;
            }
            return power;
        }

        /**
         * Return the base of this function
         * What it's power is based upon (function or variable or constant)
         * Constant order = 0 base = ZERO
         * Variable order = 1 base = ONE
         *
         * @return the base of this expression
         */
        public Function getBase(){
            Function order;
            switch (functionName){
                case n:
                    order = ZERO;
                    break;
                case x:
                    order = ONE;
                    break;
                case o:
                    if (name.equals("+") || name.equals("-"))
                        order = left.getBase();
                    else if (name.equals("*") || name.equals("/"))
                        order = right.getBase();
                    else
                        order = left;
                    break;
                default:
                    order = this;
            }
            return order;
        }

        /**
         * Create an empty function
         */
        public Function() {
        }

        /**
         * Create a constant or variable function
         *
         * @param name a String name or a number as a String
         */
        public Function(String name){
            setName(name);
        }

        public void setName(String name){
            this.name = name; // constant or variable
            try {
                // try for an int first
                intValue = (int) Long.parseLong(name);
                isConstant = true;
                opType = OpType.CONSTANT;
                functionName = FunctionName.n;
            }
            catch (NumberFormatException e){
                try {
                    // then for a double
                    constant = Double.parseDouble(name);
                    isConstant = true;
                    opType = OpType.CONSTANT;
                    functionName = FunctionName.n;
                }
                catch (NumberFormatException nfe){
                    // it wasn't a number
                    if (name.length() == 1) {
                        // is it a binary operator
                        functionName = FunctionName.n;
                        //noinspection EnhancedSwitchMigration
                        switch (name){
                            case "+":
                            case "-":
                                opType = OpType.SUM;
                                break;
                            case "*":
                            case "/":
                                opType = OpType.PRODUCT;
                                break;
                            case "^":
                                opType = OpType.CHAIN;
                                break;
                            default: // variable?
                                isVariable = true;
                                opType = OpType.NAME;
                        }
                    } else {
                        functionName = FunctionName.valueOf(name);
                    }
                }
            }
        }

        /**
         * Create a function that takes one argument
         *
         * @param action name of the function
         * @param leftPart a function for the one argument
         */
        public Function(String action, Function leftPart){
            setName(action);
            left = leftPart;
            opType = OpType.CHAIN;
            isVariable = false;
            functionName = FunctionName.valueOf(name);
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
            setName(action);
            left = leftPart;
            right = rightPart;
        }

        //
        // Parsing routines
        //

        private static Character getNext(List<Character> rest) {
            return rest.size() > 0 ? rest.remove(0) : 0;
        }

        private static Character getNextNoneBlank(List<Character> rest) {
            Character test = getNext(rest);
            while (test == ' ')
                test = getNext(rest);// skip blanks
            return test;
        }

        private static Character getLast(List<Character> rest) {
            return rest.size() > 0 ? rest.remove(rest.size() - 1) : 0;
        }

        private static Character getLastNoneBlank(List<Character> rest) {
            Character test = getLast(rest);
            while (test == ' ')
                test = getNext(rest);// skip blanks
            return test;
        }

        private static String getNextWord(List<Character> rest) {
            Character test = getNextNoneBlank(rest);
            while (test == ' ')
                test = getNext(rest);// skip blanks
            StringBuilder word = new StringBuilder();
            while (test != ' ' && test != 0){
                word.append(test);
                test = getNext(rest);
            }
            // as we aren't using it, put it back!
            if (test != 0)
                rest.add(0, test);
            return word.toString();
        }

        /**
         * Populate the Function from the next part of the rest.
         * Return the unused part of rest.
         * @param rest characters to process
         * @return remaining characters to process
         */
        public static List<Character>  parse(Function toPopulate, List<Character> rest) {
            Character test = getNextNoneBlank(rest);
            if (test == '(') { // start with a bra then:
                test = getLastNoneBlank(rest); // must be matching closing ket
                if (test != ')')
                    throw new Error("Expression does not have matching brackets");
                // starting an expression
                String name = getNextWord(rest);
                toPopulate.setName(name);
                if (toPopulate.functionName == FunctionName.o) {
                    // has two arguments
                    test = getNextNoneBlank(rest);
                    if (test != ',')
                        throw new Error("Missing comma between operator and first argument");
                    toPopulate.left = new Function();
                    rest = parse(toPopulate.left, rest);
                    test = getNextNoneBlank(rest);
                    if (test != ',')
                        throw new Error("Missing comma between first and second arguments");
                    toPopulate.right = new Function();
                    rest = parse(toPopulate.right, rest);
                } else if (toPopulate.functionName == FunctionName.x) {
                    // has one argument
                    test = getNextNoneBlank(rest);
                    if (test != ',')
                        throw new Error("Missing comma between operator and argument");
                    toPopulate.left = new Function();
                    rest = parse(toPopulate.left, rest);
                } // otherwise, it is a constant or variable
            } else {
                // can only be a variable or constant
                String name = getNextWord(rest);
                toPopulate.setName(name);
                if (toPopulate.functionName != FunctionName.x && toPopulate.functionName != FunctionName.n)
                    throw new Error("Function or operator without brackets");
            }
            return rest;
        }

        /**
         * Basic simplification.
         *
         * isConstant or isVar => as simple as it can be
         * if left then make left simple then
         * if function and left is constant => do the sum!
         * if right then make right simple then
         * if operator and both left and right are constant => do the sum!
         *
         * if '-' change right = Function("*", right, -1) and name = '+'
         *
         * if '+' then
         *    if left.getPower() > right.getPower() then
         *       left and right get swapped
         *    else if == then combine left and right into same "power"
         *
         * if '÷' and right.isConstant then
         *    name = '*'
         *    right = 1 ÷ right.value
         *
         * if '*' then
         *    if right.isConstant and not left.isConstant
         *       left and right get swapped
         *    if left.base == right.base then
         *       combine powers: left = left.power + right.power and right = left.base
         *
         */
        private void makeMoreSimple(){
            if (isVariable || isConstant)
                return; // as simple as it gets
            if (left != null)
                left.makeMoreSimple();
            if (opType == OpType.CHAIN) {
                //noinspection ConstantConditions
                if (left.isConstant) {
                    calculate();
                    return;
                }
            }
            if (right != null)
                right.makeMoreSimple();
            if (functionName == FunctionName.o){
                calculate();
            }
            // try and make commutative!
            if (name.equals("-")){
                setName("+");
                right = new Function("*", MINUS_ONE, right); // - X => + -1 * X!
            }
            if (name.equals("+")){
                //noinspection ConstantConditions
                if (left.getBase() == right.getBase()){
                    if(left.getPower() > right.getPower()){ // swap the order
                        Function swap = left;
                        left = right;
                        right = swap;
                    } else if (left.getPower() == right.getPower()){
                        addLeftAndRight();
                    }
                }
            }
            if (name.equals("+") &&
                    left.isConstant &&
                    left.constant == 0){
                // it adds nothing ...
                setName(right.name);
                left = right.left;
                right = right.right;
                return;
            }
            if (name.equals("*")) {
                //noinspection ConstantConditions
                if (right.isConstant && !left.isConstant){
                    Function swap = left;
                    left = right;
                    right = swap;
                }
                //noinspection ConstantConditions
                if (left.getBase() == right.getBase()) {
                    int sum = left.getPower() + right.getPower();
                    left = new Function(Integer.toString(sum));
                    right = right.getBase();
                    setName("^");
                }
            }
            if (name.equals("*") && left.isConstant) {
                if (left.constant == 0) {
                    // it is nothing ...
                    setName("0");
                    left = null;
                    right = null;
                    return;
                }
                if (left.constant == 1) {
                    // it changes nothing ...
                    setName(right.name);
                    left = right.left;
                    right = right.right;
                    return;
                }
            }
            //noinspection ConstantConditions
            if (name.equals("^") && left.isConstant) {
                if (left.constant == 0) {
                    // it is one!
                    setName("1");
                    left = null;
                    right = null;
                    return;
                }
                if (left.constant == 1) {
                    // it changes nothing ...
                    setName(left.name);
                    right = left.right;
                    left = left.left;
                    //noinspection UnnecessaryReturnStatement
                    return;
                }
            }
        }

        /**
         * Add the left and right together as they have the same base...
         * '+' , ('+' , x , y) , ('+', p, q)
         * '+' , ('*' , x , y) , ('*', p, q)
         * etc.,
         */
        private void addLeftAndRight() {
        }

        /**
         * Calculate the function or operator applied to a constant or constants
         * Should convert this into a new constant!
         */
        private void calculate() {
            Double sum = null;
            switch (functionName){
                case ln:
                    sum = Math.log(left.constant);
                    break;
                case exp:
                    sum = Math.exp(left.constant);
                    break;
                case sin:
                    sum = Math.sin(left.constant);
                    break;
                case cos:
                    sum = Math.cos(left.constant);
                    break;
                case tan:
                    sum = Math.tan(left.constant);
                    break;
                case o:
                    //noinspection EnhancedSwitchMigration
                    switch (name){
                        case "+":
                            sum = left.constant + right.constant;
                            break;
                        case "-":
                            sum = left.constant - right.constant;
                            break;
                        case "*":
                            sum = left.constant * right.constant;
                            break;
                        case "/":
                            sum = left.constant / right.constant;
                            break;
                        case "^":
                            sum = Math.pow(left.constant, right.constant);
                            break;
                    }
                    break;
                default:
                    // should not come here!
            }
            if (sum != null){
                setName(sum.toString());
                left = null;
                right = null;
            }
        }


        @Override
        public String toString() {
            if (right == null){
                if (left == null){
                    return name;
                } else {
                    return "( " + name + " " + left + " )";
                }
            } else {
                if (left == null){
                    return "( " + name + " " + right + " )";
                } else {
                    return "( " + name + " " + left + " " + right + " )";
                }
            }
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
            //noinspection EnhancedSwitchMigration
            switch (opType){
                case CONSTANT: // a number
                    diff = ZERO; // end of the line
                    break;
                case NAME: // a variable
                    diff = ONE; // replace with a one
                    break;
                case SUM: // a sum
                    // Sum rule:
                    //   ( a.f(x) + b.g(x) ) ' = a.f'(x) + b.g'(x)
                    // remember to keep the name (+ / -)!
                    diff = new Function(name, left.differentiate(), right.differentiate() );
                    break;
                case PRODUCT: // a product
                    // Product rule:
                    //    ( f(x)∙g(x) ) ' = f'(x).g(x) + f(x).g'(x)
                    Function newLeft = new Function("*", left.differentiate(), right);
                    Function newRight = new Function("*", left, right.differentiate());
                    diff = new Function("+", newLeft, newRight);
                    break;
                case CHAIN: // a real function
                    // Derivative chain rule
                    //   f(g(x))' = f'(g(x))∙g'(x)
                    // f -> name (with right), g -> left
                    //   f(g(x))' -> new name = "*", new left = f'(g(x)), new right = g'(x)
                    Function newLeft2 = null; //f'(g(x))
                    //noinspection EnhancedSwitchMigration
                    switch (name){ // derive left
                        case "^": // to a power: x^a   -> a.x^(a-1)
                            // x = left, a = right
                            // => (* a (^ x, (+ a -1)))
                            Function newPower = new Function("+", right, MINUS_ONE);
                            Function newRight2 = new Function("^", left, newPower);
                            newLeft2 = new Function("*", right, newRight2);
                            break;
                    }
                    diff = new Function("*", newLeft2, left.differentiate() );
                    break;
            }
            //noinspection ConstantConditions
            diff.makeMoreSimple();
            return diff;
        }
    }


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
        Function function = new Function();
        List<Character> rest = Function.parse(function, expr.chars().mapToObj(e -> (char)e).collect(Collectors.toList()));
        if (Function.getNextNoneBlank(rest) != 0) // a none blank remains?
            throw new Error("left over input after parsing the expression");
        function.makeMoreSimple();
        // differentiate
        function = function.differentiate();
        function.makeMoreSimple();
        // return to string
        return function.toString();
    }
}

