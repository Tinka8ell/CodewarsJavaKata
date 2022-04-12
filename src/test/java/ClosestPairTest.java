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

        List<Point> result = Kata.closestPair(points);
        List<Point> expected = Arrays.asList(new Point(6, 3), new Point(7, 4));
        verify(expected, result);
    }

    @Test
    public void test02_TwoPoints() {

        List<Point> points = Arrays.asList(
                new Point(2, 2),
                new Point(6, 3)
        );

        List<Point> result = Kata.closestPair(points);
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

        List<Point> result = Kata.closestPair(points);
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


    @Test
    public void test10() {

        List<Point> points = Arrays.asList(
                new Point(2, 2), new Point(2, 8), new Point(5, 5), new Point(6, 3),
                new Point(6, 7), new Point(7, 4), new Point(7, 9)
        );

        List<Point> result = Kata.closestPair(points);
        List<Point> expected = Arrays.asList(new Point(7, 4), new Point(6,3));
        verify(expected, result);
    }

    @Test
    public void test11() {

        List<Point> points = Arrays.asList(
                new Point(2, 2), new Point(6, 3)
        );

        List<Point> result = Kata.closestPair(points);
        List<Point> expected = Arrays.asList(new Point(2, 2), new Point(6, 3));
        verify(expected, result);
    }

    @Test
    public void test12() {

        List<Point> points = Arrays.asList(
                new Point(2, 2), new Point(2, 8), new Point(5, 5), new Point(5, 5),
                new Point(6, 3), new Point(6, 7), new Point(7, 4), new Point(7, 9)
        );

        List<Point> result = Kata.closestPair(points);
        List<Point> expected = Arrays.asList(new Point(5, 5), new Point(5,5));
        verify(expected, result);
    }

    @Test
    public void test13() {

        List<Point> points = Arrays.asList(
                new Point(0.004477, 0.010088), new Point(0.001750, 0.008304), new Point(0.008387, 0.012612),
                new Point(0.012745, 0.010152), new Point(0.009958, 0.008218), new Point(0.000754, 0.003441),
                new Point(0.015654, 0.007540), new Point(0.004860, 0.001948), new Point(0.006219, -0.000205),
                new Point(0.001911, 0.001342), new Point(-0.001413, 0.007705), new Point(0.014316, 0.000104),
                new Point(0.015812, 0.004134), new Point(-0.001768, 0.008126), new Point(0.006425, -0.005368),
                new Point(0.008052, -0.003760), new Point(0.013636, 0.003723), new Point(0.012671, 0.005624),
                new Point(0.011769, 0.013250), new Point(0.004954, -0.002518), new Point(0.003360, 0.004874),
                new Point(0.012482, -0.001965), new Point(0.009075, 0.003953), new Point(0.017167, 0.001999),
                new Point(0.007016, 0.007622), new Point(0.009736, 0.000464)
        );

        List<Point> result = Kata.closestPair(points);
        List<Point> expected = Arrays.asList(new Point(-0.001768, 0.008126), new Point(-0.001413, 0.007705));
        verify(expected, result);
    }


}