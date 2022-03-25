import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Greed{
    // scoring
    private static final HashMap<Integer, Integer> threes;
    private static final HashMap<Integer, Integer> ones;

    static {
        threes = new HashMap<>();
        threes.put(1, 1000);
        threes.put(6, 600);
        threes.put(5, 500);
        threes.put(4, 400);
        threes.put(3, 300);
        threes.put(2, 200);

        ones = new HashMap<>();
        ones.put(1, 100);
        ones.put(5, 50);
    }

    public static int greedy(int[] dice){
        Map<Integer, Long> counts = Arrays.stream(dice)
                .boxed()
                .collect(Collectors.groupingBy(k -> k, Collectors.counting()));
        int total = 0;
        // find only (or none) group of three or more
        for (Integer key: counts.keySet()) {
            if (counts.get(key) >= 3){
                // found it!
                total += threes.get(key);
                // remove it!
                counts.put(key, counts.get(key) - 3);
            }
        }
        for (Integer key: ones.keySet())
            total += ones.get(key) * counts.getOrDefault(key, 0L);
        return total;
    }
}