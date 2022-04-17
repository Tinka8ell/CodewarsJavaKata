import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class WhitespaceInterpreter {

    // transforms space characters to ['s','t','n'] chars;
    public static String unbleach(String code) {
        return code != null ? code.replace(' ', 's').replace('\t', 't').replace('\n', 'n') : null;
    }

    // solution
    public static String execute(String code, InputStream input) {
        String output = "";
        Stack<Integer> stack = new Stack<>();
        Map<Integer,Integer> heap = new HashMap<>();
        // ... you code ...
        return output;
    }

}
