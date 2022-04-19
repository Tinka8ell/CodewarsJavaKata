import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MorseCodeDecoder {

    public static String decodeBits(String bits) {
        ArrayList<Integer> pulses = new ArrayList<>();
        boolean isZero = true;
        int count = 0;
        for (char bit: bits.toCharArray()) {
            if (bit == '0' == isZero)
                count++;
            else {
                pulses.add(count);
                count = 1;
                isZero = ! isZero;
            }
        }
        pulses.add(count);
        pulses.remove(0); // leading 0's
        if (bits.endsWith("0"))
            pulses.remove(pulses.size() - 1); // and any trailing ones
        int pulseWidth = pulses
                .stream()
                .mapToInt(v -> v)
                .min().orElseThrow();
        StringBuilder decode = new StringBuilder();
        isZero = false;
        for (int pulse: pulses) {
            int size = pulse / pulseWidth;
            if (isZero) {
                if (size > 1) {
                    decode.append((size == 3) ? " " : "   ");
                }
            } else {
                decode.append((size == 1) ? "." : "-");
            }
            isZero = !isZero;
        }
        return decode.toString();
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