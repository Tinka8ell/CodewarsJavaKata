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
 * The final value of 'd' before 'S' becomes entry is the shortest distance and the contents of 'S' with be the pair!
 *
 */
public class Kata {

    private static final Random random = new Random();
    public static final double FACTOR = 1.5;

    private double shortest = Double.MAX_VALUE;

    /**
     * ClosestPair is really a set of closer points.
     * Methods on it will calculate filters and apply them.
     */
    public Kata() {
        points = new HashSet<>(); // create empty
    }

    public Kata(Set<Point> initial) {
        points = initial; // use set to initialise
    }

    // bounding box dimensions S - Smallest, L - Largest, x and y
    private double boxSx = Double.MAX_VALUE;
    private double boxLx = -boxSx;
    private double boxSy = boxSx;
    private double boxLy = boxLx;
    // keep the points as they come
    private final Set<Point> points;

    /**
     * @return List of the points remaining
     */
    public List<Point> toList(){
        return new ArrayList<>(points);
    }

    /**
     * Add the point and ensure the box contains it.
     *
     * @param p the point to add
     * @param x its x coordinate
     * @param y its y coordinate
     */
    public void addPoint(Point p, double x, double y){
        points.add(p);
        // expand the box if necessary
        if (x > boxLx)
            boxLx = x;
        if (x < boxSx)
            boxSx = x;
        if (y > boxLy)
            boxLy = y;
        if (y < boxSy)
            boxSy = y;
    }

    /**
     * Chose and remove a random point
     *
     * @return chosen Point
     */
    private Point removeRandom() {
        // chose a point to start from
        int choose = random.nextInt(points.size());
        Point chosen = null; // assuming we have some points we will get one!
        for (Point point : points) {
            chosen = point;
            if (choose-- <= 0)
                break;
        }
        points.remove(chosen);
        return chosen;
    }

    /**
     * Get a smaller set of points that are closer together
     *
     * @return smaller set
     */
    public Kata smaller() {
        // so we don't redo the tst with same points!
        if (points.size() <= 2) // can't be smaller!
            return this; // stop looking!

        Kata closer = new Kata();
        Point chosen = removeRandom(); // take one of them

        // get the shortest distance
        // will be square of the shortest distance
        closer.shortest = shortest;
        // Chosen point
        double x = chosen.x;
        double y = chosen.y;
        for (Point nextPoint:  points) {
            double nx = nextPoint.x;
            double ny = nextPoint.y;
            double distance = (x - nx)*(x - nx) + (y - ny) * (y - ny);
            if (closer.shortest > distance)
                closer.shortest = distance;
            closer.addPoint(nextPoint, nx, ny);
        }
        // add ourselves
        closer.addPoint(chosen, x, y);
        closer.removeLoneOnes();
        // System.out.println(closer.points.size());
        if (closer.points.size() < 2) // gone too far!
            return this;
        return closer.smaller(); // keep trying
    }

    /**
     * The heart of the filtering: remove all lonely points.
     *
     * For each point, create a map of occupied grid places.
     * For any occupied grid places, if they only contain one
     * point check if they are really alone by checking for
     * non-empty grid places in their "Moore neighborhood".
     * If it's lonely remove it.
     *
     * Grid size (gs) is sqrt(shortest) / 2
     * Coordinate is [round((x-minX)/gs), round((y-minY)/gs)] (rounded to nearest ints)
     */
    private void removeLoneOnes() {
        /* create map points to coordinate
         * create map of coordinates with counts of neighbours
         * for each point
         *    add its coordinate
         *    inc its neighbour counts (including itself)
         */
        double gs = Math.sqrt(shortest) / FACTOR;
        int gw = (int) Math.ceil((boxLx - boxSx) / gs);
        Map<Point, Integer> coordinates = new HashMap<>();
        Set<Integer> neighbours = new HashSet<>();
        for (Point p : points) {
            // convert point into an integer "coordinate"
            int key = (int) Math.round(p.x / gs) + gw * (int) Math.round(p.y / gs);
            coordinates.put(p, key);
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 && j != 0){
                        // convert "coordinate" into neighbour "coordinate"
                        int neighbour = key + i + j * gw;
                        neighbours.add(neighbour);
                    }
                }
            }
        }
        for (Point p: coordinates.keySet()) {
            int key = coordinates.get(p);
            if (!neighbours.contains(key)) { // no neighbours so remove it
                points.remove(p);
            }
        }
    }


    public static List<Point> closestPair(List<Point> points) {
        // first shortcut: any duplicates must be closest!
        Point firstDuplicate = null;
        Set<Point> uniquePoints = new HashSet<>();
        for (Point p : points) {
            if (!uniquePoints.add(p)) {
                firstDuplicate = p; // found one!
                break;
            }
        }
        if (firstDuplicate != null) {
            List<Point> duplicates = new ArrayList<>();
            duplicates.add(firstDuplicate);
            duplicates.add(firstDuplicate);
            return duplicates;
        }
        return new Kata(uniquePoints).smaller().toList();
    }

}


