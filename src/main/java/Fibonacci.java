import java.math.BigInteger;

/**
 * Calculate arbitrary Fibonacci numbers.
 *
 * The year is 1214. One night, Pope Innocent III awakens to find the the archangel Gabriel floating before him.
 * Gabriel thunders to the pope:
 *
 *   Gather all of the learned men in Pisa, especially Leonardo Fibonacci.
 *   In order for the crusades in the holy lands to be successful,
 *   these men must calculate the millionth number in Fibonacci's recurrence.
 *   Fail to do this, and your armies will never reclaim the holy land. It is His will.
 *
 * The angel then vanishes in an explosion of white light.
 *
 * Pope Innocent III sits in his bed in awe. How much is a million? he thinks to himself.
 * He never was very good at math.
 *
 * He tries writing the number down, but because everyone in Europe is still using Roman numerals
 * at this moment in history, he cannot represent this number. If he only knew about the invention
 * of zero, it might make this sort of thing easier.
 *
 * He decides to go back to bed. He consoles himself, The Lord would never challenge me thus;
 * this must have been some deceit by the devil. A pretty horrendous nightmare, to be sure.
 *
 * Pope Innocent III's armies would go on to conquer Constantinople (now Istanbul),
 * but they would never reclaim the holy land as he desired.
 *
 * This uses a matrix solution for calculating Fibonacci numbers using where it can be proved
 * the nth number is:
 * _( 1, 1 )_ raised to the (n-1)th power and taking the top left number!
 *  ( 1, 0 )
 *
 *  Added to that is the observation that if n < 0 the numbers are the same as for -n, but with a sign
 *  dependent on whether n is odd or even.
 *
 *  To reduce the computation time it is observed that:
 *  a^(2n) = a^n * a^n and:
 *  a^(2n + 1) = a * a^(2n)
 *
 */
public class Fibonacci {

    public static final BigInteger[][] M = new BigInteger[][]{
            {BigInteger.ONE, BigInteger.ONE},
            {BigInteger.ONE, BigInteger.ZERO}
    };

    /**
     * Generate the nth Fibonacci number
     * @param n order of the number (can be signed)
     * @return the nth Fibonacci number
     */
    public static BigInteger fib(BigInteger n) {
        if (n.equals(BigInteger.ZERO)){
            return n;
        }
        int sign = n.signum();
        if (sign < 0){
            n = n.negate();
            if (n.mod(BigInteger.TWO).longValue() == 0)
                return fib(n).negate(); // negate the even ones
            return fib(n); // else use the abs value
        }
        return power(n.longValue() - 1)[0][0];
    }

    /**
     * Simple 2x2 matrix multiplication with BigIntegers
     * Sets a = a * b
     * @param a initial matrix, and will be return value (modified)
     * @param b multiplier (unmodified)
     */
    static void multiply(BigInteger[][] a, BigInteger[][] b)
    {
        BigInteger x =  a[0][0].multiply(b[0][0]).add(a[0][1].multiply(b[1][0]));
        BigInteger y =  a[0][0].multiply(b[0][1]).add(a[0][1].multiply(b[1][1]));
        BigInteger z =  a[1][0].multiply(b[0][0]).add(a[1][1].multiply(b[1][0]));
        BigInteger w =  a[1][0].multiply(b[0][1]).add(a[1][1].multiply(b[1][1]));

        a[0][0] = x;
        a[0][1] = y;
        a[1][0] = z;
        a[1][1] = w;
    }


    /**
     * Return the nth power of the "Fibonacci" matrix;
     *
     * @param n power ( >= 0)
     * @return BigInteger matrix of the result
     */
    static BigInteger[][] power(long n)
    {
        BigInteger [][] F = null; // result matrix
        // copy M - the "Fibonacci" matrix
        BigInteger [][] P = new BigInteger[2][2];
        P[0][0] = M[0][0];
        P[0][1] = M[0][1];
        P[1][0] = M[1][0];
        P[1][1] = M[1][1];
        while (n > 0){
            if (n % 2 == 1){
                if (F == null){ // not yet set
                    // copy the current power
                    F = new BigInteger[2][2];
                    F[0][0] = P[0][0];
                    F[0][1] = P[0][1];
                    F[1][0] = P[1][0];
                    F[1][1] = P[1][1];
                } else {
                    // multiply on this power
                    multiply(F, P);
                }
            }
            multiply(P, P); // calculate the next power - square it
            n = n / 2; // next power of 2
        }
        if (F == null) { // must have been power of 0
            // hard code the value of 1!
            F = new BigInteger[2][2];
            F[0][0] = BigInteger.ONE;
        }
        return F;
    }

    public static void main(String[] args) {
        BigInteger n = new BigInteger("0");
        System.out.println(n + " => " + fib(n));
        n = new BigInteger("1");
        System.out.println(n + " => " + fib(n));
        n = new BigInteger("2");
        System.out.println(n + " => " + fib(n));
        n = new BigInteger("3");
        System.out.println(n + " => " + fib(n));
        n = new BigInteger("4");
        System.out.println(n + " => " + fib(n));
        n = new BigInteger("5");
        System.out.println(n + " => " + fib(n));
        n = new BigInteger("6");
        System.out.println(n + " => " + fib(n));
        n = new BigInteger("-1");
        System.out.println(n + " => " + fib(n));
        n = new BigInteger("-2");
        System.out.println(n + " => " + fib(n));
        n = new BigInteger("-3");
        System.out.println(n + " => " + fib(n));
        n = new BigInteger("-4");
        System.out.println(n + " => " + fib(n));
        n = new BigInteger("-5");
        System.out.println(n + " => " + fib(n));
        n = new BigInteger("-6");
        System.out.println(n + " => " + fib(n));
        n = new BigInteger("1000000");
        System.out.println(n + " => " + fib(n));
    }
}