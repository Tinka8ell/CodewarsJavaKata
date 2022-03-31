import java.util.Arrays;
import java.util.List;

/**
 * Closest pair of points in linearithmic time Kata
 *
 * Given a number of points on a plane, your task is to find two points with the smallest distance between them in linearithmic O(n log n) time.
 *
 * Example
 *   1  2  3  4  5  6  7  8  9
 * 1
 * 2    . A
 * 3                . D
 * 4                   . F
 * 5             . C
 * 6
 * 7                . E
 * 8    . B
 * 9                   . G
 * For the plane above, the input will be:
 *
 * [
 *   [2,2], // A
 *   [2,8], // B
 *   [5,5], // C
 *   [6,3], // D
 *   [6,7], // E
 *   [7,4], // F
 *   [7,9]  // G
 * ]
 * => the closest pair is: [[6,3],[7,4]] or [[7,4],[6,3]]
 * (both answers are valid)
 * The two points that are closest to each other are D and F.
 * Expected answer should be an array with both points in any order.
 *
 * Goal
 * The goal is to come up with a function that can find two closest points for any arbitrary array of points, in a linearithmic time.
 *
 * Point class is provided for us.
 *
 * Implementation Notes:
 * Looking on the wiki, I will try this approach for set 'S' of points:
 * Khuller & Matias (1995) goes through two phases:
 * Repeat the following steps, until 'S' becomes empty:
 *   Choose a point 'p' uniformly at random from 'S'
 *   Compute the distances from 'p' to all the other points of 'S' and let 'd' be the minimum such distance
 *   Round the input points to a square grid of size 'd' / 2, and delete from 'S' all points whose
 *      "Moore neighborhood" has no other points
 * "Moore neighborhood" is the 8 grid squares that surround a grid square
 * The final value of 'd' before 'S' becomes entry is the shortest distance and the contenss of 'S' with be the pair!
 *
 */
public class ClosestPair {
    public static List<Point> closestPair(List<Point> points) {
        return Arrays.asList();
    }
}