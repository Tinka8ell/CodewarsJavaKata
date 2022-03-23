import java.util.stream.Collectors;
import java.util.stream.Stream;

class Solution{

    static String toCamelCase(String s){
        String[] words = s.split("[_-]");
        return words[0] +
                Stream.of(words)
                        .skip(1)
                        .map((word) -> word.substring(0, 1).toUpperCase() + word.substring(1))
                        .collect(Collectors.joining());
    }
}