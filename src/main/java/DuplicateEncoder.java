import java.util.stream.Collectors;

public class DuplicateEncoder {
    static String encode(String word){
        int [] translateMap = new int[256];
        word.toLowerCase()
                .chars()
                .forEach((c) -> translateMap[c] ++);
        return word.toLowerCase()
                .chars()
                .map((c) -> translateMap[c] > 1? ')': '(')
                .mapToObj(Character::toString)
                .collect(Collectors.joining());
    }
}
