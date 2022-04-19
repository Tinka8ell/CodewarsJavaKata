import java.util.HashMap;
import java.util.Map;

public class MorseCode {
    // placeholder for the real function
    private static final Map<String, String> decode = new HashMap<>();
    static {
        decode.put("....", "H");
        decode.put(".", "E");
        decode.put("-.--", "Y");
        decode.put(".---", "J");
        decode.put("..-", "U");
        decode.put("-..", "D");
    }

    public static String get(String code) {
        return decode.get(code);
    }
}
