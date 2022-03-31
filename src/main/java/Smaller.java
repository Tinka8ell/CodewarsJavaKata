import java.util.Arrays;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class Smaller {

    public static int[] smaller(int[] unsorted) {
        int len = unsorted.length;
        int[] counted = new int[len];
        TreeMap<Integer, Integer> toRight =  new TreeMap<>();
        for (int p = len - 1; p >= 0; p--) {
            int value = unsorted[p];
            counted[p] = toRight
                    .keySet()
                    .stream()
                    .filter(i -> i < value)
                    .map(toRight::get)
                    .reduce(0, Integer::sum);
            toRight.put(value, toRight.getOrDefault(value, 0) + 1);
        }
        return counted;
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