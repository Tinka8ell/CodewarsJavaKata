import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toCollection;

class SumDigPower {

    private static boolean isEureka(int number){
        long[] digits = Integer
                .toString(number) // to string of digits
                .chars() // to stream of chars (ints)
                .mapToObj(Character::toString) // to string of 1 digit
                .mapToLong(Long::parseLong) // to long
                .toArray();
        return number == IntStream
                .range(0, digits.length)
                .mapToLong((a) -> (long) Math.pow(digits[a], a + 1))
                .sum();
    }

    public static List<Long> sumDigPow(long a, long b) {
        return IntStream
                .rangeClosed((int) a, (int) b)
                .filter(SumDigPower::isEureka)
                .mapToObj(value -> (long)value)
                .collect(toCollection(ArrayList<Long>::new));
    }
}