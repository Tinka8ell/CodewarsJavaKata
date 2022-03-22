public class Square {
    public static boolean isSquare(int n) {
        boolean result = false;
        if (n >= 0) {
            double sqrt = Math.sqrt(n);
            result = (sqrt - (Math.floor(sqrt))) == 0;
        }
        return result;
    }
}