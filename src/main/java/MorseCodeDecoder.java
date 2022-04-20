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
        // System.out.println("Ones: " + ones);
        // System.out.println("Zeros: " + zeros);
        if (ones.get(0) == 0){
            ones.remove(0);
            if (ones.size() == 0)
                return "";
            if (zeros.size() > 0)
                zeros.remove(0);
            // System.out.println("Ones: " + ones);
            // System.out.println("Zeros: " + zeros);
        }
        if (zeros.size() > 0 && zeros.get(0) == 0){
            zeros.remove(0);
            System.out.println("Ones: " + ones);
            System.out.println("Zeros: " + zeros);
        }
        while (zeros.size() >= ones.size()){
            zeros.remove(zeros.size() - 1);
        }
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
        int max = frequency.values().stream().reduce(0, (m, i) -> i > m ? i : m);
        double sum = 0;
        double dCount = 0;
        int index= frequencies.get(0);
        while (true){
            Integer number = frequency.get(index);
            if (number == null)
                break;
            if (number < max / 2)
                break;
            dCount += number;
            sum += number * index;
            index++;
        }
        double x = sum / dCount;
        double a = x; //1.0;
        double two = 2.5 * x;
        double five = 5 * x;
        StringBuilder message = new StringBuilder(
                "x: " + x + ", a: " + a);
        message.append("\nOne: ")
                .append(x - a)
                .append(" -> ")
                .append(x + a);
        message.append("\nTwo: ")
                .append(two);
        message.append("\nThree: ")
                .append(3 * (x - a))
                .append(" -> ")
                .append(3 * (x + a));
        message.append("\nFive: ")
                .append(five);
        message.append("\nSeven: ")
                .append(7 * (x - a))
                .append(" -> ")
                .append(7 * (x + a));
        for (int each = shortest; each <= longest; each++) {
            int count = frequency.getOrDefault(each, 0);
            message.append("\n")
                    .append(each)
                    .append(" -> ")
                    .append(count);
        }
        System.out.println("Ones: " + ones);
        System.out.println("Zeros: " + zeros);
        System.out.println(message);
        message = new StringBuilder();
        for (int i = 0; i < ones.size(); i++) {
            int count = ones.get(i);
            // if (count < x + a + 1)
            if (count < two)
                message.append(".");
            else
                message.append("-");
            // message.append(count);
            if (i < zeros.size()) {
                count = zeros.get(i);
                // if (count > x + a) {
                if (count > two) {
                    // if (count < 3 * (x + a) + 1)
                    if (count < five)
                        message.append(" ");
                    else
                        message.append("   ");
                    // message.append(count);
                }
                // else
                // message.append("[").append(count).append("]");

            }
        }
        //message.append("\n   .... . -.--   .--- ..- -.. .");
        //message.append("\n.... . -.--   .--- ..- -.. .");
        /*
        message.append("\n");
        for (int i = 0; i < ones.size(); i++) {
            int count = ones.get(i);
            if (count > x - a - 1)
                message.append("-");
            else
                message.append(".");
            // message.append(count);
            if (i < zeros.size()){
                count = zeros.get(i);
                if (count > x - a - 1){
                    if (count < x + a + 1)
                        message.append(" ");
                    else
                        message.append("   ");
                    // message.append(count);
                }
                // else
                    // message.append("[").append(count).append("]");

            }
        }
         */
        if (message.length() < 2)
            System.out.println("Short message: '" + message + "'");
        message.append("X");
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