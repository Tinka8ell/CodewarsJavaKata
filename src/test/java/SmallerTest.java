import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SmallerTest {
    @Test
    public void initialTests() {
        assertArrayEquals(new int[] {4, 3, 2, 1,0}, Smaller.smaller(new int[] {5,4,3,2,1}));
        assertArrayEquals(new int[] {0,0,0}, Smaller.smaller(new int[] {1,2,3}));
        assertArrayEquals(new int[] {1,1,0}, Smaller.smaller(new int[] {1,2,0}));
        assertArrayEquals(new int[] {0,1,0}, Smaller.smaller(new int[] {1,2,1}));
        assertArrayEquals(new int[] {3,3,0,0,0}, Smaller.smaller(new int[] {1,1,-1,0,0}));
        assertArrayEquals(new int[] {4, 1, 5, 5, 0, 0, 0, 0, 0}, Smaller.smaller(new int[] {5, 4, 7, 9, 2, 4, 4, 5, 6}));

    }

    @Test
    public void bigTest() {
        int[] start;
        int[] expect;
        int[] result;
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
        expect = Smaller.oldSmaller(start);
        // System.out.println("Using: " + Arrays.toString(start));

        result = Smaller.smaller(start);
        // System.out.println("Gives: " + Arrays.toString(result));

        // System.out.println("Want:  " + Arrays.toString(expect));
        assertArrayEquals(expect, result);
        // System.out.println();

        start = new int[] { 277, 951, 860, 452, 458, 356, 241, 461, 835, 917};
        expect = Smaller.oldSmaller(start);
        // expect = new int[] {1, 8, 6, 2, 2, 1, 0, 0, 0, 0};
        // System.out.println("Using: " + Arrays.toString(start));

        result = Smaller.smaller(start);
        // System.out.println("Gives: " + Arrays.toString(result));

        // System.out.println("Want:  " + Arrays.toString(expect));
        assertArrayEquals(expect, result);
        // System.out.println();

    }

}