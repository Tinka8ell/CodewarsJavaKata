import java.util.ArrayList;

public class DescendingOrder {
    public static int sortDesc(final int num) {
        int rest = num;
        ArrayList<Integer> digits = new ArrayList<>();
        while (rest > 0) {
            digits.add(rest % 10);
            rest /= 10;
        }
        digits.sort(null);
        int result = 0;
        for (int i = digits.size() - 1; i >= 0; i--) {
            result *= 10;
            result += digits.get(i);
        }
        return result;
    }
}