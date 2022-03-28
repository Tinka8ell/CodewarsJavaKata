import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KataSolution {

    /**
     * Binomial Expansion
     *
     * The purpose of this kata is to write a program that can do some algebra.
     * Write a function expand that takes in an expression with a single,
     * one character variable, and expands it. The expression is in the form (ax+b)^n
     * where a and b are integers which may be positive or negative,
     * x is any single character variable, and n is a natural number.
     * If a = 1, no coefficient will be placed in front of the variable.
     * If a = -1, a "-" will be placed in front of the variable.
     *
     * The expanded form should be returned as a string in the form ax^b+cx^d+ex^f...
     * where a, c, and e are the coefficients of the term, x is the original
     * one character variable that was passed in the original expression and
     * b, d, and f, are the powers that x is being raised to in each term and
     * are in decreasing order.
     * If the coefficient of a term is zero, the term should not be included.
     * If the coefficient of a term is one, the coefficient should not be included.
     * If the coefficient of a term is -1, only the "-" should be included.
     * If the power of the term is 0, only the coefficient should be included.
     * If the power of the term is 1, the caret and power should be excluded.
     * 
     * Examples:
     * (x+1)^2  == x^2+2x+1
     * (p-1)^3  == p^3-3p^2+3p-1
     * (2f+4)^6  == 64f^6+768f^5+3840f^4+10240f^3+15360f^2+12288f+4096
     * (-2a-4)^0  == 1
     * (-12t+43)^2  == 144t^2-1032t+1849
     * (r+0)^203  == r^203
     * (-x-1)^2  == x^2+2x+1
     */
    public static class BinomialExpansion{
        private final long[] coefficient;
        private final String variable;

        public BinomialExpansion(String variableName, long[] coefficients) {
            coefficient = coefficients;
            variable = variableName;
        }

        @Override
        public String toString() {
            StringBuilder longForm = new StringBuilder();
            for (int power = coefficient.length - 1; power >= 0; power--) {
                long term = coefficient[power];
                if (term != 0){
                    // a term to display
                    if (term > 0 && longForm.length() > 0) // nonzero and not first value
                        longForm.append('+');
                    if (term < 0) { // negative term
                        term = -term;
                        longForm.append('-');
                    }
                    if (term != 1 || power == 0)
                        longForm.append(term);
                    if (power > 0) {
                        longForm.append(variable);
                        if (power > 1) {
                            longForm.append('^');
                            longForm.append(power);
                        }
                    }
                }
            }
            return longForm.toString();
        }

        public static BinomialExpansion times(BinomialExpansion a, BinomialExpansion b){
            // assume the variable names match!
            int aLength = a.coefficient.length;
            int bLength = b.coefficient.length;
            long[] coefficients = new long[aLength + bLength - 1];
            for (int i = 0; i < aLength; i++) {
                for (int j = 0; j < bLength; j++) {
                    coefficients[i + j] += a.coefficient[i] * b.coefficient[j];
                }
            }
            return new BinomialExpansion(a.variable, coefficients);
        }
    }

    public static String expand(String expr) {
        // split up the expression to the parts of (ax+b)^n
        BinomialExpansion result = new BinomialExpansion("?", new long[]{1}); // unity
        Matcher matcher = Pattern.compile("\\(([+-]*)(\\d*)([a-zA-Z]+)([+-]*)(\\d*)\\)\\^(\\d+)").matcher(expr);
        if (matcher.find()){
            int power1 = getSigned(expr, matcher, 1, 2);
            String variable = getGroup(expr, matcher, 3);
            int power0 = getSigned(expr, matcher, 4, 5);
            int power = Integer.parseInt(getGroup(expr, matcher, 6));
            BinomialExpansion start = new BinomialExpansion(variable, new long[]{power0, power1});
            for (int i = 0; i < power; i++) {
                result = BinomialExpansion.times(start, result);
            }
        }
        return result.toString();
    }

    private static int getSigned(String expr, Matcher matcher, int first, int second) {
        String sign = getGroup(expr, matcher, first);
        String value = getGroup(expr, matcher, second);
        if (!value.equals(""))
            value = sign + value;
        else
            value = sign + 1;
        return Integer.parseInt(value);
    }

    private static String getGroup(String expr, Matcher matcher, int group) {
        return expr.substring(matcher.start(group), matcher.end(group));
    }
}