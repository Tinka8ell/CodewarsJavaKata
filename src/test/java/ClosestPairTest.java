//import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClosestPairTest {

    @Test
    public void test01_Example() {

        List<Point> points = Arrays.asList(
                new Point(2, 2), //A
                new Point(2, 8), //B
                new Point(5, 5), //C
                new Point(6, 3), //D
                new Point(6, 7), //E
                new Point(7, 4), //F
                new Point(7, 9)  //G
        );

        List<Point> result = ClosestPair.closestPair(points);
        List<Point> expected = Arrays.asList(new Point(6, 3), new Point(7, 4));
        verify(expected, result);
    }

    @Test
    public void test02_TwoPoints() {

        List<Point> points = Arrays.asList(
                new Point(2, 2),
                new Point(6, 3)
        );

        List<Point> result = ClosestPair.closestPair(points);
        List<Point> expected = Arrays.asList(new Point(6, 3), new Point(2, 2));
        verify(expected, result);
    }

    @Test
    public void test03_DuplicatedPoint() {

        List<Point> points = Arrays.asList(
                new Point(2, 2), //A
                new Point(2, 8), //B
                new Point(5, 5), //C
                new Point(5, 5), //C
                new Point(6, 3), //D
                new Point(6, 7), //E
                new Point(7, 4), //F
                new Point(7, 9)  //G
        );

        List<Point> result = ClosestPair.closestPair(points);
        List<Point> expected = Arrays.asList(new Point(5, 5), new Point(5,5));
        verify(expected, result);
    }

    private void verify(List<Point> expected, List<Point> actual) {
        Comparator<Point> comparer = Comparator.comparingDouble(p -> p.x);

        assertNotNull(actual, "Returned array cannot be null.");
        assertEquals(2, actual.size(), "Expected exactly two points.");
        assertFalse(actual.get(0) == null || actual.get(1) == null, "Returned points must not be null.");

        expected.sort(comparer);
        actual.sort(comparer);
        boolean eq = expected.get(0).x == actual.get(0).x && expected.get(0).y == actual.get(0).y
                && expected.get(1).x == actual.get(1).x && expected.get(1).y == actual.get(1).y;
        assertTrue(eq, String.format("Expected: %s, Actual: %s", expected, actual));
    }

}