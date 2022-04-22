import java.util.*;
import java.util.stream.Collectors;

public class MorseCodeDecoder {

    /**
     * Given a string of bits, which may or may not begin or end with '0's,
     * and which may have some variation in the length of the time unit used,
     * returns the Morse Code translation of this message.
     *
     * Accepts 0s and 1s, return dots, dashes and spaces
     *
     * @param bits String of '1's and '0's
     * @return String of '.'s and '-'s
     */
    public static String decodeBitsAdvanced(String bits) {
        System.out.println(bits);
        bits = bits.replaceAll("^0+", "");
        bits = bits.replaceAll("0+$", "");
        if (bits.isEmpty())
            return "";
        List<Integer> ones = Arrays.stream(bits.split("0+"))
                .mapToInt(String::length)
                .boxed()
                .collect(Collectors.toList());
        if (ones.size() == 0)
            return "";
        List<Integer> zeros = Arrays.stream(bits.split("1+"))
                .mapToInt(String::length)
                .boxed()
                .collect(Collectors.toList());
        if (zeros.size() > 0 && zeros.get(0) == 0){
            zeros.remove(0);
        }
        System.out.println(" Ones: " + ones);
        System.out.println("Zeros: " + zeros);
        int longestOne = ones.stream().reduce(0, (m, i) -> m = (i > m) ? i : m);
        int longest = zeros.stream().reduce(longestOne, (m, i) -> m = (i > m) ? i : m);
        int shortest = zeros.stream().reduce(longest, (m, i) -> m = (i < m) ? i : m);
        shortest = ones.stream().reduce(shortest, (m, i) -> m = (i < m) ? i : m);
        decodeMorse(".... . -.--   .--- ..- -.. .");
        String morseCode = "";
        for (int i = shortest * 6; i < 18 * shortest; i++){
            float one = (float) (i / 6.0);
            morseCode = generateMorseCode(ones, zeros, one);
            decodeMorse(morseCode);
            if (checkMorse(morseCode)) {
                System.out.println("Check is true ");
                break;
            }
        }
        //message.append("\n   .... . -.--   .--- ..- -.. .");
        //message.append("\n.... . -.--   .--- ..- -.. .");
        return morseCode;
    }

    private static String generateMorseCode(List<Integer> ones, List<Integer> zeros, float one) {
        int three = (int) Math.floor(3.0 * one);
        int seven = (int) Math.floor(7 * one);
        //seven = 99;
        System.out.println("Trying generation with one: " + one + ", three: " + three + ", seven: " + seven);
        StringBuilder message = new StringBuilder();
        StringBuilder debug = new StringBuilder();
        for (int i = 0; i < ones.size(); i++) {
            int count = ones.get(i);
            debug.append("[").append(count).append("]");
            if (count >= three)
                message.append("-");
            else
                message.append(".");
            debug.append(message.substring(message.length()-1));
            if (i < zeros.size()) {
                count = zeros.get(i);
                debug.append("(").append(count).append(")");
                if (count >= three) {
                    if (count >= seven)
                        message.append("   ");
                    else
                        message.append(" ");
                    debug.append(message.substring(message.length()-1));
                }
            }
        }
        String morseCode = message.toString();
        //System.out.println("Debug: " + debug);
        System.out.println("Generated: " + message);
        return morseCode;
    }

    public static boolean checkMorse(String morseCode) {
        return !Arrays.stream(morseCode.trim().split(" {3}"))
                .map(MorseCodeDecoder::checkWord)
                .reduce(false, (a, b) -> a || b);
    }

    private static boolean checkWord(String word) {
        return Arrays.stream(word.split(" "))
                .map(MorseCode::get)
                .map(Objects::isNull)
                .reduce(false, (a, b) -> a || b);
    }

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
        String text = "";
        if (!morseCode.isEmpty())
            text = Arrays.stream(morseCode.trim().split(" {3}"))
                .map(MorseCodeDecoder::decodeWord)
                .collect(Collectors.joining(" "));
        System.out.println("decodeMorse: " + morseCode + " -> " + text);
        return text;
    }

    private static String decodeWord(String word) {
        return Arrays.stream(word.split(" ")).map(MorseCode::get).collect(Collectors.joining());
    }

}