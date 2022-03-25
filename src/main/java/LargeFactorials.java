import java.util.Arrays;
import java.util.stream.IntStream;

public class LargeFactorials {

    /**
     * My Big Int is a way of representing very big integers.
     * They are stored exactly as an array of int values,
     * each representing the number at a number of places.
     * For convenience, I'll split the numbers every 9 digits!
     */
    private static class MyBigInt{
        long[] value;
        static final int big = 1000000000; // 10 ^ 9 or about 2 ^ 30

        public MyBigInt(String number) {
            StringBuilder rest = new StringBuilder(number);
            value = new long[(number.length() - 1) / 9 + 1]; // 1 for 0 to 9 digits, 2 for 10 to 18, ...
            for (int i = 0; i < value.length; i++) {
                int len = rest.length();
                int lastNine = len - Integer.min(9, len);
                value[i] = Integer.parseInt(rest.substring(lastNine));
                rest.delete(lastNine, len);
            }
        }

        public MyBigInt(long[] values) {
            value = values;
        }

        public MyBigInt(int number) {
            this(new long[]{number});
        }

        public MyBigInt() {
            this(1);
        }

        public MyBigInt times(MyBigInt number){
            // do cross multiplication
            long[] result = new long[value.length + number.value.length];
            for (int i = 0; i < value.length; i++) {
                long carry = 0;
                for (int j = 0; j < number.value.length; j++) {
                    result[i + j] += value[i] * number.value[j] + carry;
                    carry = result[i + j] / big;
                    result[i + j] %= big;
                }
                int next = i + number.value.length;
                while (carry > 0){
                    result[next] += carry;
                    carry = result[next] / big;
                    result[next] %= big;
                    next ++;
                }
            }
            int msI = result.length-1;
            while (msI > 0 && result[msI] == 0)
                msI --;
            value = Arrays.copyOf(result, msI + 1);
            return this;
        }

        @Override
        public String toString() {
            StringBuilder str = new StringBuilder(String.format("%d", value[value.length - 1]));
            for (int i = value.length-2; i >= 0; i--) {
                str.append(String.format("%09d", value[i]));
            }
            return str.toString();
        }
    }

    public static String Factorial(int n) {
        return IntStream.rangeClosed(1, n)
                .mapToObj(MyBigInt::new)
                .reduce(MyBigInt::times)
                .orElseGet(MyBigInt::new)
                .toString();
        // return (new MyBigInt(Integer.toString(MyBigInt.big))).toString();
    }

}
