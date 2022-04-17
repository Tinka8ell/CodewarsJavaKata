import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class WhitespaceInterpreter {

    // transforms space characters to ['s','t','n'] chars;
    public static String unbleach(String code) {
        return code != null ? code.replace(' ', 's').replace('\t', 't').replace('\n', 'n') : null;
    }

    /**
     * Convert the "whitespace" source code into a queue of tri-state codes.
     * As:
     *    ' ' is often the equivalent of binary 0, so use 0
     *    '\t' is often the equivalent of binary 1, so use 1
     *    '\n' if usually more special, so use -1
     *
     * @param source String of Whitespace code
     * @return a Queue of Integer codes
     */
    public static Queue<Integer> toCodes(String source){
        return source
                .chars()
                .filter(Character::isWhitespace)
                .map(c -> (c == ' ') ? 0 : (c == '\t') ? 1 : -1)
                .boxed().collect(Collectors.toCollection(ArrayDeque::new));
    }

    // solution
    public static String execute(String code, InputStream input) {
        StringBuilder output = new StringBuilder();
        Stack<Integer> stack = new Stack<>();
        Map<Integer,Integer> heap = new HashMap<>();
        Queue<Integer> codeQueue = toCodes(code);
        while (codeQueue.peek() != null) {
            int sign = codeQueue.poll();
            output.append((sign == 0) ? '0' : (sign > 0) ? '+' : '-');
        }
        return output.toString();
    }

}
