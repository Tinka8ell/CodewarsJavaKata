import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class ObservedPin {

    /*
The keypad has the following layout:
┌───┬───┬───┐
│ 1 │ 2 │ 3 │
├───┼───┼───┤
│ 4 │ 5 │ 6 │
├───┼───┼───┤
│ 7 │ 8 │ 9 │
└───┼───┼───┘
    │ 0 │
    └───┘
*/
    private static final String[][] alts = {
            {"0", "8"},
            {"1", "2", "4"},
            {"1", "2", "3", "5"},
            {"2", "3", "6"},
            {"1", "4", "5", "7"},
            {"2", "4", "5", "6", "8"},
            {"3", "5", "6", "9"},
            {"4", "7", "8"},
            {"5", "7", "8", "9", "0"},
            {"6", "8", "9"}
    };

    public static List<String> getPINs(String observed) {
        String[][] options = new String[observed.length()][];
        for (int pos = 0; pos < observed.length(); pos++) {
            options[pos] = alts[observed.charAt(pos) - '0'];
        }
        List<String> list = Arrays.stream(options[0]).collect(Collectors.toList());
        for (int i = 1; i < observed.length(); i++) {
            List<String> newList = new ArrayList<>();
            for (String prefix: list) {
                newList.addAll(Arrays.stream(options[i]).map(s -> prefix + s).collect(Collectors.toList()));
            }
            list = newList;
        }
        return list;
    } // getPINs

} // ObservedPin
