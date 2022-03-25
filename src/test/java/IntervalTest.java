import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IntervalTest {

    @Test
    public void checkFailingtest(){
        /*
        For:
        [5701, 9560], [875, 3275], [-9225, 3764], [-9026, -5656], [-6724, -4565], [-6942, -4207], [-7370, 3303], [4984, 5150], [6709, 9359], [-9623, 8520], [-831, 6796], [-2804, 2000], [1632, 3336], [-6458, 4167], [-7844, -6949], [2112, 6635], [877, 4423], [48, 5695], [-385, 6914], [-4590, -2676], [-6357, -1656], [-1813, 1759]
        expected: <19183>
        but was:<22002>
         */
        int[][] testcase = new int[][]{
                {5701, 9560},
                {875, 3275},
                {-9225, 3764},
                {-9026, -5656},
                {-6724, -4565},
                {-6942, -4207},
                {-7370, 3303},
                {4984, 5150},
                {6709, 9359},
                {-9623, 8520},
                {-831, 6796},
                {-2804, 2000},
                {1632, 3336},
                {-6458, 4167},
                {-7844, -6949},
                {2112, 6635},
                {877, 4423},
                {48, 5695},
                {-385, 6914},
                {-4590, -2676},
                {-6357, -1656},
                {-1813, 1759}
        };
        int expected = 19183;
        assertEquals(expected, Interval.sumIntervals(testcase));
    }

    @Test
    public void shouldHandleNullOrEmptyIntervals() {
        assertEquals(0, Interval.sumIntervals(null));
        assertEquals(0, Interval.sumIntervals(new int[][]{}));
        assertEquals(0, Interval.sumIntervals(new int[][]{{4, 4}, {6, 6}, {8, 8}}));
    }

    @Test
    public void shouldAddDisjoinedIntervals() {
        assertEquals(9, Interval.sumIntervals(new int[][]{{1, 2}, {6, 10}, {11, 15}}));
        assertEquals(11, Interval.sumIntervals(new int[][]{{4, 8}, {9, 10}, {15, 21}}));
        assertEquals(7, Interval.sumIntervals(new int[][]{{-1, 4}, {-5, -3}}));
        assertEquals(78, Interval.sumIntervals(new int[][]{{-245, -218}, {-194, -179}, {-155, -119}}));
    }

    @Test
    public void shouldAddAdjacentIntervals() {
        assertEquals(54, Interval.sumIntervals(new int[][]{{1, 2}, {2, 6}, {6, 55}}));
        assertEquals(23, Interval.sumIntervals(new int[][]{{-2, -1}, {-1, 0}, {0, 21}}));
    }

    @Test
    public void shouldAddOverlappingIntervals() {
        assertEquals(7, Interval.sumIntervals(new int[][]{{1, 4}, {7, 10}, {3, 5}}));
        assertEquals(6, Interval.sumIntervals(new int[][]{{5, 8}, {3, 6}, {1, 2}}));
        assertEquals(19, Interval.sumIntervals(new int[][]{{1, 5}, {10, 20}, {1, 6}, {16, 19}, {5, 11}}));
    }

    @Test
    public void shouldHandleMixedIntervals() {
        assertEquals(13, Interval.sumIntervals(new int[][]{{2, 5}, {-1, 2}, {-40, -35}, {6, 8}}));
        assertEquals(1234, Interval.sumIntervals(new int[][]{{-7, 8}, {-2, 10}, {5, 15}, {2000, 3150}, {-5400, -5338}}));
        assertEquals(158, Interval.sumIntervals(new int[][]{{-101, 24}, {-35, 27}, {27, 53}, {-105, 20}, {-36, 26},}));
    }

}