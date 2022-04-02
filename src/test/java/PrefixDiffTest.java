import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrefixDiffTest {


    @Test
    public void SimpleTests() {
        PrefixDiff diff = new PrefixDiff();

        assertEquals("0", diff.diff("5"), "constant should return 0");
        assertEquals(diff.diff("x"), "x should return 1", "1");
        assertEquals("2", diff.diff("(+ x x)"), "x+x should return 2");
        assertEquals("0", diff.diff("(- x x)"), "x-x should return 0");
        assertEquals("2", diff.diff("(* x 2)"), "2*x should return 2");
        assertEquals("0.5", diff.diff("(/ x 2)"), "x/2 should return 0.5");
        assertEquals("(* 2 x)", diff.diff("(^ x 2)"), "x^2 should return 2*x");
        assertEquals("(* -1 (sin x))", diff.diff("(cos x)"), "cos(x) should return -1 * sin(x)");
        assertEquals("(cos x)", diff.diff("(sin x)"), "sin(x) should return cos(x)");

        String result = diff.diff("(tan x)");
        assertTrue((result.equals("(+ 1 (^ (tan x) 2))") || result.equals("(^ (cos x) -2)") || result.equals("(/ 1 (^ (cos x) 2))")),
                "tan(x) should return (+ 1 (^ (tan x) 2)) or (^ (cos x) -2) or (/ 1 (^ (cos x) 2)) but got " + result
                );

        assertEquals("(exp x)", diff.diff("(exp x)"), "exp(x) should return exp(x)");
        assertEquals( "(/ 1 x)", diff.diff("(ln x)"), "ln(x) should return 1/x");
    }

    @Test
    public void NestedExpressions() {
        PrefixDiff diff = new PrefixDiff();

        assertEquals("3", diff.diff("(+ x (+ x x))"), "x+(x+x) should return 3");
        assertEquals("1", diff.diff("(- (+ x x) x)"), "(x+x)-x should return 1");
        assertEquals("2", diff.diff("(* 2 (+ x 2))"), "2*(x+2) should return 2");
        assertEquals("(/ -2 (^ (+ 1 x) 2))", diff.diff("(/ 2 (+ 1 x))"), "2/(1+x) should return -2/(1+x)^2");
        assertEquals("(* -1 (sin (+ x 1)))", diff.diff("(cos (+ x 1))"), "cos(x+1) should return -1 * sin(x+1)");

        String result = diff.diff("(cos (* 2 x))");
        assertTrue((result.equals("(* 2 (* -1 (sin (* 2 x))))") || result.equals("(* -2 (sin (* 2 x)))")),
                "Expected (* 2 (* -1 (sin (* 2 x)))) or (* -2 (sin (* 2 x))) but got " + result);

        assertEquals("(cos (+ x 1))", diff.diff("(sin (+ x 1))"), "sin(x+1) should return cos(x+1)");
        assertEquals("(* 2 (cos (* 2 x)))", diff.diff("(sin (* 2 x))"), "sin(2*x) should return 2*cos(2*x)");

        result = diff.diff("(tan (* 2 x))");
        assertTrue((result.equals("(* 2 (+ 1 (^ (tan (* 2 x)) 2)))") || result.equals("(* 2 (^ (cos (* 2 x)) -2))") || result.equals("(/ 2 (^ (cos (* 2 x)) 2))")),
                "Expected (* 2 (+ 1 (^ (tan (* 2 x)) 2))) or (* 2 (^ (cos (* 2 x)) -2)) or (/ 2 (^ (cos (* 2 x)) 2)) but got " + result);

        assertEquals("(* 2 (exp (* 2 x)))", diff.diff("(exp (* 2 x))"), "exp(2*x) should return 2*exp(2*x)");
    }

    @Test
    public void SecondDerivatives() {
        PrefixDiff diff = new PrefixDiff();

        assertEquals("(* -1 (sin x))", diff.diff(diff.diff("(sin x)")), "Second deriv. sin(x) should return -1 * sin(x)");
        assertEquals("(exp x)", diff.diff(diff.diff("(exp x)")), "Second deriv. exp(x) should return exp(x)");

        String result = diff.diff(diff.diff("(^ x 3)"));
        assertTrue((result.equals("(* 3 (* 2 x))") || result.equals("(* 6 x)")),
                "Expected (* 3 (* 2 x)) or (* 6 x) but got " + result);
    }


}