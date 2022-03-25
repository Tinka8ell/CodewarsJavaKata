public class HumanReadableTime {
    public static String makeReadable(int seconds) {
        int s = seconds % 60;
        seconds /= 60;
        int m = seconds % 60;
        int h = seconds /60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}