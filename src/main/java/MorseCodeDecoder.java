import java.util.Arrays;
import java.util.stream.Collectors;

public class MorseCodeDecoder {

    public static String decodeBits(String bits) {
        return ".";
    }

    public static String decodeMorse(String morseCode) {
        return Arrays.stream(morseCode.trim().split(" {3}"))
                .map(MorseCodeDecoder::decodeWord)
                .collect(Collectors.joining(" "));
    }

    private static String decodeWord(String word) {
        return Arrays.stream(word.split(" ")).map(MorseCode::get).collect(Collectors.joining());
    }

}