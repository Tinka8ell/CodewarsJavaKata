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
        System.out.println("Ones: " + ones);
        System.out.println("Zeros: " + zeros);
        if (ones.get(0) == 0){
            ones.remove(0);
            zeros.remove(0);
            System.out.println("Ones: " + ones);
            System.out.println("Zeros: " + zeros);
        }
        if (zeros.get(0) == 0){
            zeros.remove(0);
            System.out.println("Ones: " + ones);
            System.out.println("Zeros: " + zeros);
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
        /*
         * shortest = x - a
         * longest = 3 * (x + a)
         * diff = 2 * x + 4 * a
         */
        /*
        int a = 0;
        int x = shortest + a;
        while (3 * (x + a) < longest){
            a++;
            x = shortest + a;
        }
        a--;
         */
        /*
         * longest = (x + a)
         * shortest = (x - a) / 3
         * 3 * diff = 3 * x + 3 * a - x + a
         *          = 2 * x + 4 * a
         */
        /*
        int a = 0;
        int x = longest - a;
        while ((x - a) / 3.0 > shortest){
            a++;
            x = longest - a;
        }
        // a--;
         */
        double sum = 0;
        double dCount = 0;
        for (int index: frequencies) {
            Integer number = frequency.get(index);
            if (number == null)
                break;
            dCount += number;
            sum += number * index;
        }
        double x = sum / dCount;
        double a = 1.0;
        StringBuilder message = new StringBuilder(
                shortest + " " + longest + " : " + (x - a) + " " + (3 * (x + a)) + " : " + x + " " + a);
                // shortest + " " + longest + " : " + ((x - a) / 3.0) + " " + (x + a) + " : " + x + " " + a);
        for (int each = shortest; each <= longest; each++) {
            int count = frequency.getOrDefault(each, 0);
            message.append("\n")
                    .append(each)
                    .append(" -> ")
                    .append(count);
        }

        message.append("\n   ");
        //message = new StringBuilder();
        for (int i = 0; i < ones.size(); i++) {
            int count = ones.get(i);
            if (count < x + a + 1)
                message.append(".");
            else
                message.append("-");
            // message.append(count);
            if (i < zeros.size()){
                count = zeros.get(i);
                if (count > x + a){
                    if (count < 3 * (x + a) + 1)
                        message.append(" ");
                    else
                        message.append("   ");
                    // message.append(count);
                }
                // else
                // message.append("[").append(count).append("]");

            }
        }
        message.append("\n   .... . -.--   .--- ..- -.. .");
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
        return Arrays.stream(morseCode.trim().split(" {3}"))
                .map(MorseCodeDecoder::decodeWord)
                .collect(Collectors.joining(" "));
    }

    private static String decodeWord(String word) {
        return Arrays.stream(word.split(" ")).map(MorseCode::get).collect(Collectors.joining());
    }

    public static void main(String[] argv){
        String test = "";
        System.out.println("Test: '" + test +
                "' - " +
                Arrays.toString(Arrays.stream(test.split("0+")).mapToInt(String::length).toArray()) +
                " - " +
                Arrays.toString(Arrays.stream(test.split("1+")).mapToInt(String::length).toArray()));
        test = "0";
        System.out.println("Test: '" + test +
                "' - " +
                Arrays.toString(Arrays.stream(test.split("0+")).mapToInt(String::length).toArray()) +
                " - " +
                Arrays.toString(Arrays.stream(test.split("1+")).mapToInt(String::length).toArray()));
        test = "000";
        System.out.println("Test: '" + test +
                "' - " +
                Arrays.toString(Arrays.stream(test.split("0+")).mapToInt(String::length).toArray()) +
                " - " +
                Arrays.toString(Arrays.stream(test.split("1+")).mapToInt(String::length).toArray()));
        test = "1";
        System.out.println("Test: '" + test +
                "' - " +
                Arrays.toString(Arrays.stream(test.split("0+")).mapToInt(String::length).toArray()) +
                " - " +
                Arrays.toString(Arrays.stream(test.split("1+")).mapToInt(String::length).toArray()));
        test = "111";
        System.out.println("Test: '" + test +
                "' - " +
                Arrays.toString(Arrays.stream(test.split("0+")).mapToInt(String::length).toArray()) +
                " - " +
                Arrays.toString(Arrays.stream(test.split("1+")).mapToInt(String::length).toArray()));
        test = "01110";
        System.out.println("Test: '" + test +
                "' - " +
                Arrays.toString(Arrays.stream(test.split("0+")).mapToInt(String::length).toArray()) +
                " - " +
                Arrays.toString(Arrays.stream(test.split("1+")).mapToInt(String::length).toArray()));
        test = "0011100";
        System.out.println("Test: '" + test +
                "' - " +
                Arrays.toString(Arrays.stream(test.split("0+")).mapToInt(String::length).toArray()) +
                " - " +
                Arrays.toString(Arrays.stream(test.split("1+")).mapToInt(String::length).toArray()));
        test = "000111000";
        System.out.println("Test: '" + test +
                "' - " +
                Arrays.toString(Arrays.stream(test.split("0+")).mapToInt(String::length).toArray()) +
                " - " +
                Arrays.toString(Arrays.stream(test.split("1+")).mapToInt(String::length).toArray()));
        test = "101";
        System.out.println("Test: '" + test +
                "' - " +
                Arrays.toString(Arrays.stream(test.split("0+")).mapToInt(String::length).toArray()) +
                " - " +
                Arrays.toString(Arrays.stream(test.split("1+")).mapToInt(String::length).toArray()));
        test = "010";
        System.out.println("Test: '" + test +
                "' - " +
                Arrays.toString(Arrays.stream(test.split("0+")).mapToInt(String::length).toArray()) +
                " - " +
                Arrays.toString(Arrays.stream(test.split("1+")).mapToInt(String::length).toArray()));
        test = "0101";
        System.out.println("Test: '" + test +
                "' - " +
                Arrays.toString(Arrays.stream(test.split("0+")).mapToInt(String::length).toArray()) +
                " - " +
                Arrays.toString(Arrays.stream(test.split("1+")).mapToInt(String::length).toArray()));
        test = "0010";
        System.out.println("Test: '" + test +
                "' - " +
                Arrays.toString(Arrays.stream(test.split("0+")).mapToInt(String::length).toArray()) +
                " - " +
                Arrays.toString(Arrays.stream(test.split("1+")).mapToInt(String::length).toArray()));
        test = "1010";
        System.out.println("Test: '" + test +
                "' - " +
                Arrays.toString(Arrays.stream(test.split("0+")).mapToInt(String::length).toArray()) +
                " - " +
                Arrays.toString(Arrays.stream(test.split("1+")).mapToInt(String::length).toArray()));
        test = "0100";
        System.out.println("Test: '" + test +
                "' - " +
                Arrays.toString(Arrays.stream(test.split("0+")).mapToInt(String::length).toArray()) +
                " - " +
                Arrays.toString(Arrays.stream(test.split("1+")).mapToInt(String::length).toArray()));
        test = "000111";
        System.out.println("Test: '" + test +
                "' - " +
                Arrays.toString(Arrays.stream(test.split("0+")).mapToInt(String::length).toArray()) +
                " - " +
                Arrays.toString(Arrays.stream(test.split("1+")).mapToInt(String::length).toArray()));
        test = "0111111000000";
        System.out.println("Test: '" + test +
                "' - " +
                Arrays.toString(Arrays.stream(test.split("0+")).mapToInt(String::length).toArray()) +
                " - " +
                Arrays.toString(Arrays.stream(test.split("1+")).mapToInt(String::length).toArray()));
        test = "0000010000110001110000";
        System.out.println("Test: '" + test +
                "' - " +
                Arrays.toString(Arrays.stream(test.split("0+")).mapToInt(String::length).toArray()) +
                " - " +
                Arrays.toString(Arrays.stream(test.split("1+")).mapToInt(String::length).toArray()));
    }
}