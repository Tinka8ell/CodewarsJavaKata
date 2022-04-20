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
        while (zeros.size() >= ones.size()){
            zeros.remove(zeros.size() - 1);
        }
        // System.out.println("Ones: " + ones + Arrays.toString(bits.split("0+")));
        // System.out.println("Zeros: " + zeros + Arrays.toString(bits.split("1+")));
        Map<Integer, Integer> frequency = new HashMap<>();
        for (int each : ones) {
            frequency.put(each, frequency.getOrDefault(each, 0) + 1);
        }
        for (int each : zeros) {
            frequency.put(each, frequency.getOrDefault(each, 0) + 1);
        }
        //noinspection SimplifyStreamApiCallChains
        List<Integer> frequencies = frequency.keySet().stream().sorted().collect(Collectors.toList());
        int shortest = frequencies.get(0);
        int longest = frequencies.get(frequencies.size() - 1);
        double x = longest  / 8.0; // 7 + 1 - smallest that can be 7 * x
        double two = 2 * x; // divide 1 and 3 times at 2
        double three = 3 * x;
        double five = 5 * x; // divide 3 and 7 times at 5
        double seven = 7 * x;
        StringBuilder message = new StringBuilder();
        message.append("\nOne: ")
                .append(x);
        message.append(", Two: ")
                .append(two);
        message.append(", Three: ")
                .append(three);
        message.append(", Five: ")
                .append(five);
        message.append(", Seven: ")
                .append(seven);
        if (Math.ceil(x) == Math.ceil(two)){
            message.append("\nFail! no seven's");
            x = longest  / 4.0; // 3 + 1 - smallest that can be 3 * x
            two = 2 * x; // divide 1 and 3 times at 2
            three = 3 * x;
            five = 5 * x; // divide 3 and 7 times at 5
            seven = 7 * x;
            message.append("\nOne: ")
                    .append(x);
            message.append(", Two: ")
                    .append(two);
            message.append(", Three: ")
                    .append(three);
            message.append(", Five: ")
                    .append(five);
            message.append(", Seven: ")
                    .append(seven);
            if (Math.ceil(x) == Math.ceil(two)){
                message.append("\nFail! no three's");
                x = longest  / 2.0; // 1 + 1 - smallest that can be x
                two = 2 * x; // divide 1 and 3 times at 2
                three = 3 * x;
                five = 5 * x; // divide 3 and 7 times at 5
                seven = 7 * x;
                message.append("\nOne: ")
                        .append(x);
                message.append(", Two: ")
                        .append(two);
                message.append(", Three: ")
                        .append(three);
                message.append(", Five: ")
                        .append(five);
                message.append(", Seven: ")
                        .append(seven);
            }
        }
        two = Math.ceil(two);
        five = Math.ceil(five);
        for (int each = shortest; each <= longest; each++) {
            int count = frequency.getOrDefault(each, 0);
            message.append("\n")
                    .append(each)
                    .append(" -> ")
                    .append(count);
        }
        System.out.println(message);
        message = new StringBuilder();
        for (int i = 0; i < ones.size(); i++) {
            int count = ones.get(i);
            if (count > two)
                message.append("-");
            else
                message.append(".");
            if (i < zeros.size()) {
                count = zeros.get(i);
                if (count > two) {
                    if (count < five)
                        message.append(" ");
                    else
                        message.append("   ");
                }
            }
        }
        //message.append("\n   .... . -.--   .--- ..- -.. .");
        //message.append("\n.... . -.--   .--- ..- -.. .");
        if (message.length() < 2)
            System.out.println("Short message: '" + message + "'");
        return message.toString();
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
        if (morseCode.isEmpty())
            return "";
        return Arrays.stream(morseCode.trim().split(" {3}"))
                .map(MorseCodeDecoder::decodeWord)
                .collect(Collectors.joining(" "));
    }

    private static String decodeWord(String word) {
        return Arrays.stream(word.split(" ")).map(MorseCode::get).collect(Collectors.joining());
    }

}