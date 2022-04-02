import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class Smaller {

    public static int[] smaller(int[] unsorted) {
        int len = unsorted.length;
        int[] lessThan = new int[len];
        int[] equal = new int[len];
        TreeMap<Integer, Integer> toRight =  new TreeMap<>();
        for (int p = len - 1; p >= 0; p--) {
            int key = unsorted[p];
            int value = 0;
            Map.Entry<Integer, Integer> next = toRight.floorEntry(key);
            if (next != null) {
                int nextPos = next.getValue();
                int nextKey = next.getKey();
                if (key == nextKey) { //
                    equal[p] = 1 + equal[nextPos]; // keep a tally of matching items
                } else {
                    // count the nextPos and any that nextPos it
                    value += 1 + equal[nextPos];
                }
                // count any we skipped to get to nextPos
                next = toRight.lowerEntry(nextKey);
                while (next != null) {
                    int last = nextPos;
                    nextPos = next.getValue();
                    value += 1 + equal[nextPos]; // count it and it's
                    nextKey = next.getKey();
                    next = toRight.lowerEntry(nextKey);
                }
            }
            lessThan[p] = value;
            toRight.put(key, p);
        }
        return lessThan;
    }

    public static int[] oldSmaller(int[] unsorted) {
        return IntStream.range(0, unsorted.length)
                .map(pos -> (int) Arrays.stream(unsorted)
                        .skip(pos + 1)
                        .filter(n -> n < unsorted[pos])
                        .count())
                .toArray();
    }

    public static void main(String[] args){
        int[] start;
        int[] expect;
        int[] result;
        /*
        start = new int[] {5, 4, 3, 2, 1};
        expect = oldSmaller(start);
        // expect = new int[] {4, 3, 2, 1, 0};
        System.out.println("Using: " + Arrays.toString(start));
        result = Smaller.smaller(start);
        System.out.println("Gives: " + Arrays.toString(result));
        System.out.println("Want:  " + Arrays.toString(expect));
        System.out.println();

        start = new int[] {1, 2, 3};
        expect = oldSmaller(start);
        // expect = new int[] {0, 0, 0};
        System.out.println("Using: " + Arrays.toString(start));
        result = Smaller.smaller(start);
        System.out.println("Gives: " + Arrays.toString(result));
        System.out.println("Want:  " + Arrays.toString(expect));
        System.out.println();

        start = new int[] {1, 2, 0};
        expect = oldSmaller(start);
        // expect = new int[] {1, 1, 0};
        System.out.println("Using: " + Arrays.toString(start));
        result = Smaller.smaller(start);
        System.out.println("Gives: " + Arrays.toString(result));
        System.out.println("Want:  " + Arrays.toString(expect));
        System.out.println();

        Random rnd = new Random();
        List<Integer> list = new ArrayList<>();
        int len = 100;
        for (int i = 0; i < len; i++) {
            list.add(rnd.nextInt(1000));
        }
        start = new int[len];
        for (int i = 0; i < list.size(); i++) {
            start[i] = list.get(i);
        }
        expect = oldSmaller(start);
        System.out.println("Using: " + Arrays.toString(start));
        result = Smaller.smaller(start);
        System.out.println("Gives: " + Arrays.toString(result));
        System.out.println("Want:  " + Arrays.toString(expect));
        System.out.println();
         */
        start = new int[] { 277, 951, 860, 452, 458, 356, 241, 461, 835, 917};
        expect = oldSmaller(start);
        // expect = new int[] {1, 8, 6, 2, 2, 1, 0, 0, 0, 0};
        System.out.println("Using: " + Arrays.toString(start));
        result = Smaller.smaller(start);
        System.out.println("Gives: " + Arrays.toString(result));
        System.out.println("Want:  " + Arrays.toString(expect));
        System.out.println();

    }
}