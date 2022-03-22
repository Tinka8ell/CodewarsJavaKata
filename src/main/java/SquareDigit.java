import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SquareDigit {

    public int squareDigits(int n) {
        return Integer.parseInt(String.valueOf(n)
                .chars()
                .mapToObj(Character::getNumericValue)
                .map(i -> i * i)
                .map(i -> Integer.toString(i))
                .collect(Collectors.joining())
        );
    }

}