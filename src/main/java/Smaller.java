public class Smaller {

    public static int[] smaller(int[] unsorted) {
        int[] counted = new int[unsorted.length];
        for (int i = unsorted.length - 1; i >= 0; i--) {
            for (int j = i + 1; j < unsorted.length; j++) {
                if (unsorted[i] > unsorted[j]) {
                    counted[i]++; // add one for this int
                } else if (unsorted[i] == unsorted[j]) {
                    counted[i] += counted[j]; // add all ints right of here
                    break;
                }
            }
        }
        return counted;
    }
}