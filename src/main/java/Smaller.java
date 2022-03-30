import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;


import java.util.Arrays;

public class Smaller {

    public static int[] smaller(int[] unsorted) {
        int len = unsorted.length;
        int[] counted = new int[len];
        int[] biggest = new int[len];
        int[] smallest = new int[len];
        int last = len - 1;
        // initialise last cell
        counted[last] = 0; // nothing to the right so nothing samller to right
        biggest[last] = len; // nothing to the right, so no bigger
        smallest[last] = len; // nothing to the right, so no smaller
        for (int p = last - 1; p >= 0; p--) {
            int q = p + 1; // next to look at
            int jump = q;
            biggest[p] = jump;
            while (jump < len && unsorted[p] > unsorted[jump]){
                jump = biggest[jump];
                biggest[p] = jump;
            }
            jump = q;
            smallest[p] = jump;
            while (jump < len && unsorted[p] < unsorted[jump]){
                jump = smallest[jump];
                smallest[p] = jump;
            }
            while (q < len) { // to the end of ints to right
                if (unsorted[p] == unsorted[q]) { // matching int so we can shortcut:
                    counted[p] += counted[q]; // add all smaller ints right to right of q
                    q = len; // done
                } else if (unsorted[p] > unsorted[q]) { // this int is smaller, so look for next biggest
                    int n = biggest[q]; // next biggest
                    if (n < len) {
                        if (unsorted[p] > unsorted[n]) { // this int is smaller, so count all the ones in between
                            counted[p] += n - q;
                            q = n; // and move there
                        } else { // this int is bigger, so can't use it
                            counted[p]++; // add one for this int
                            q++;  // and try next
                        }
                    } else {
                        // all the rest are smaller, so add them
                        counted[p] += len - q;
                        q = len;
                    }
                } else { // this int is bigger, so look for next smallest
                    int n = smallest[q]; // next smallest
                    if (n < len) {
                        if (unsorted[p] < unsorted[n]) { // this int is bigger, so count all the ones in between
                            // counted[p] += n - q;
                            q = n; // and move there
                        } else { // this int is bigger, so can't use it
                            q++;  // and try next
                        }
                    }
                    else {
                        // nothing smaller to add, skip to end
                        q = len;
                    }
                }
            }
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
         */

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
        // expect = new int[] {1, 1, 0};
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