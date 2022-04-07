// for the solution
import java.util.Arrays;

// additional for the main method
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// additional for the old solution used for testing
import java.util.stream.IntStream;

public class Smaller {

    /**
     * Optimised count of ints smaller than me to the right.
     *
     * This was nicked from a similar problem in javascript (without comments).
     * It uses a custom tree sort.
     * I had tried to use TreeSet and TreeMap, which were not efficient enough,
     * but didn't think of creating my own tree!
     * Strangely elegant in its simplicity, once you take the plunge!
     * Added comments to explain (as much as possible) what it is doing.
     *
     * @param unsorted array of ints
     * @return array of counts of smaller ints to the right ...
     */
    public static int[] smaller(int[] unsorted) {
        // System.out.println(Arrays.toString(unsorted) + " ===>");
        int[] result = new int[unsorted.length];
        Smaller root = null;
        for (int index = unsorted.length - 1; index >= 0; index--) {
            root = insert(root, unsorted[index], result, 0, index);
        }
        // System.out.println(Arrays.toString(result) + " <===");
        return result;
    }

    private final int val; // the number represented by this node
    private int count = 0; // count of smaller (?)
    private int dup = 1; // count of how many times it has occurred
    private Smaller left  = null; // if not null the bigger subtree
    private Smaller right = null; // if not null the smaller subtree

    public Smaller(int val) {
        this.val = val;
    }

    /**
     * Insert the next "root" of the sorted tree.
     * As a side effect populates the array of counts of smaller ints.
     *
     * The unsorted array is parsed from right to left.
     *
     * @param root current root of the tree
     * @param num int value at this index of the unsorted array
     * @param result n array of counts of smaller ints, with elements to right of index already populated
     * @param sum initially 0, but contains the count so far as this is recursively called
     * @param index of the element we are processing in the unsorted array
     * @return a new root for the rest of the tree
     */
    private static Smaller insert(Smaller root, int num, int[] result, int sum, int index) {
        if (root == null) { // no root below here so make a "zero" count leaf for this number
            result[index] = sum;
            return new Smaller(num);
        }
        if (root.val == num) { // check for duplicates
            root.dup++;
            result[index] = sum + root.count; // result count is sum so far and the count of this subtree
        } else if (root.val > num) { // new one is bigger than here in the tree
            root.count++;
            root.left = insert(root.left, num, result, sum, index); // so fill in the smaller tree
        } else {
            // fill in the bigger tree
            root.right = insert(root.right, num, result, sum + root.count + root.dup, index);
        }
        return root;
    }


    /**
     * Original simple version.
     * This is too slow, but works,
     * so used to validate a random collection.
     *
     * @param unsorted array of ints
     * @return array of counts of smaller ints to the right ...
     */
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