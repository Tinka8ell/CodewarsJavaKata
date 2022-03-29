import org.junit.jupiter.api.Test;

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

}