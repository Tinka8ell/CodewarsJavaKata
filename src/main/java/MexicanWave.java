import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MexicanWave {

    public static String[] wave(String str) {
        return IntStream.range(0, str.length())
                .mapToObj((i) ->
                        (str.charAt(i) == ' ') ?
                                ""
                                : str.substring(0, i) + Character.toString(str.charAt(i) + 'A' - 'a') + str.substring(i + 1))
                .filter((s) -> s.length() > 0)
                .collect(Collectors.toList())
                .toArray(String[]::new);
    }
}