import java.util.ArrayList;

public class Interval {

    public static int sumIntervals(int[][] intervals) {
        int count = 0;
        if (intervals != null && intervals.length > 0){
            ArrayList<int[]> list = new ArrayList<>();
            for (int[] range: intervals) {
                int lower = range[0];
                int upper = range[1];
                for (int i = 0; i < list.size(); i ++) {
                    int[] pair = list.get(i);
                    if (upper < pair[0]) {
                        // must be earlier, so new pair
                        list.add(i, range); // insert before
                        upper = lower;
                        break;
                    } else if (lower < pair[0]){
                        // must be overlap
                        pair[0] = lower;
                        if (upper > pair[1]) {
                            pair[1] = upper;
                        }
                        //  replace pair
                        list.remove(i);
                        list.add(i, pair); // extend lower limit
                        upper = lower;
                        checkForMerge(list, i);
                        break;
                    } else if (lower < pair[1]){
                        // overlap
                        if (upper > pair[1]){
                            pair[1] = upper;
                            //  replace pair
                            list.remove(i);
                            list.add(i, pair); // extend lower limit
                            upper = lower;
                            checkForMerge(list, i);
                            break;
                        } else {
                            // enclosed
                            upper = lower;
                            break;
                        }
                    }
                    // if not done break, keep looking
                }
                if (upper > lower){
                    // no math so add at end
                    list.add(range);
                }
            }
            for (int[] range: list) {
                count += range[1] - range[0];
            }
        }
        return count;
    }

    private static void checkForMerge(ArrayList<int[]> list, int i) {
        while (i > 0 && list.get(i)[0] < list.get(i - 1)[1]){
            // we merge existing
            int[] pair = list.get(i);
            if (pair[0] < list.get(i - 1)[0]){
                // just swallow previous one
                list.remove(i - 1);
            } else {
                pair[0] = list.get(i - 1)[0];
                // replace it
                list.remove(i);
                list.remove(i - 1);
                list.add(i - 1, pair);
            }
        }
        while (i+1 < list.size() && list.get(i)[1] > list.get(i + 1)[0]) {
            // we merge existing
            int[] pair = list.get(i);
            if (pair[1] > list.get(i + 1)[1]) {
                // just swallow previous one
                list.remove(i + 1);
            } else {
                pair[1] = list.get(i + 1)[1];
                // replace it
                list.remove(i + 1);
                list.remove(i);
                list.add(i, pair);
            }
        }
    }
}
