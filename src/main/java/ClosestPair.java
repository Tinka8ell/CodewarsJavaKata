import java.util.*;

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
        // first shortcut: any duplicates must be closest!
        Point firstDuplicate = null;
        HashSet<Point> uniquePoints = new HashSet<>();
        for (Point p: points) {
            if (!uniquePoints.add(p)){
                firstDuplicate = p; // found one!
                break;
            }
        }
        if (firstDuplicate != null){
            List<Point> duplicates = new ArrayList<>();
            duplicates.add(firstDuplicate);
            duplicates.add(firstDuplicate);
            return duplicates;
        }
        // No duplicates and assuming that the given list is unsorted and random,
        // then taking the first is as good as any to pick a random point!
        Random random = new Random();
        while (points.size() > 2){ // until last two points!
            BoundingBox box = new BoundingBox();
            int choose = random.nextInt(points.size());
            List<Point> lookAt = new ArrayList<>();
            Point selected = points.get(choose); // always works and probably random
            for (int index = 0; index < points.size(); index++) {
                if (index != choose)
                    lookAt.add(points.get(index));
            }
            // shortest ths time ('d')
            double shortest = Double.MAX_VALUE; // will be square of the shortest distance
            // this point
            double x = selected.x;
            double y = selected.y;
            box.addPoint(selected, x, y);
            for (Point nextPoint: lookAt) {
                double nx = nextPoint.x;
                double ny = nextPoint.y;
                box.addPoint(nextPoint, nx, ny);
                double distance = (x - nx)*(x - nx) + (y - ny) * (y - ny);
                if (shortest > distance)
                    shortest = distance;
            }
            box.setGrid(shortest);
            lookAt.add(selected);
            box.removeLoneOnes(lookAt);
            points = lookAt;
        }
        return points;
    }

    /**
     * Represents the smallest box containing all the points
     */
    private static class BoundingBox{

        // box dimensions S - Smallest, L - Largest, x and y
        private double boxSx = Double.MAX_VALUE;
        private double boxSy = boxSx;
        private double boxLx = Double.MIN_VALUE;
        private double boxLy = boxLx;
        // keep the points as they come
        private final List<Point> points = new ArrayList<>();
        // when we set the grid, calculate the size of each side ... and get size
        private double gridDelta;

        /**
         * Add the point and ensure the box contains it.
         *
         * @param p the point to add
         * @param x it's x coordinate
         * @param y int's y coordinate
         */
        public void addPoint(Point p, double x, double y){
            points.add(p);
            // expand the box if necessary
            if (boxLx < x)
                boxLx = x;
            if (boxSx > x)
                boxSx = x;
            if (boxLy < y)
                boxLy = y;
            if (boxSy > y)
                boxSy = y;
        }

        /**
         * Set up the grid size based on the squared shortest distance.
         *
         * @param d2 square of the shortest distance
         */
        public void setGrid(double d2){
            gridDelta = Math.sqrt(d2) / 2.0; // using the logic we have been given
        }

        /**
         * Calculate the grid containing this point
         * @param p point to locate
         * @return a grid coordinate
         */
        private int[] getCoordinate(Point p) {
            return new int[]{
                    (int) Math.floor(p.x / gridDelta),
                    (int) Math.floor(p.y / gridDelta)
            };
        }

        /**
         * The heart of the filtering: remove all lonely points.
         *
         * For each point, create a map of occupied grid places.
         * For any occupied grid places, if they only contain one
         * point check if they are really alone by checking for
         * non-empty grid places in their "Moore neighborhood".
         * If it's lonely remove it from the given list.
         *
         * @param everyone List of all the points
         */
        public void removeLoneOnes(List<Point> everyone) {
            // fill occupied:
            Map<int[], List<Point>> occupied = new HashMap<>();
            for (Point p : points) {
                int[] key = getCoordinate(p);
                List<Point> list = occupied.getOrDefault(key, new ArrayList<>());
                list.add(p);
                occupied.put(key, list);
            }
            for (int[] key: occupied.keySet()) {
                if (occupied.get(key).size() < 2) {
                    // may be lonely check "Moore neighborhood"
                    boolean lonely = !occupied.containsKey(new int[]{key[0]-1, key[1]});
                    lonely = lonely && !occupied.containsKey(new int[]{key[0]+1, key[1]});
                    lonely = lonely && !occupied.containsKey(new int[]{key[0]-1, key[1]-1});
                    lonely = lonely && !occupied.containsKey(new int[]{key[0]+1, key[1]-1});
                    lonely = lonely && !occupied.containsKey(new int[]{key[0]-1, key[1]+1});
                    lonely = lonely && !occupied.containsKey(new int[]{key[0]+1, key[1]+1});
                    lonely = lonely && !occupied.containsKey(new int[]{key[0]-1, key[1]-1});
                    lonely = lonely && !occupied.containsKey(new int[]{key[0]-1, key[1]+1});
                    if(lonely)
                        everyone.remove(occupied.get(key).get(0)); // remove it as it is lonely
                }
            }
        }
    }

}