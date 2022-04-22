import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class MorseCodeDecoderTest {

    @Test
    public void testExampleFromDescriptionVerebose() {
        MatcherAssert.assertThat(
                MorseCodeDecoderVerbose.decode(".... . -.--   .--- ..- -.. ."),
                is("HEY JUDE"));
    }

    @Test
    public void testExampleFromDescriptionShort() {
        MatcherAssert.assertThat(
                MorseCodeDecoder.decodeMorse(".... . -.--   .--- ..- -.. ."),
                is("HEY JUDE"));
    }

    @Test
    public void testExampleFromDescription() throws Exception {
        MatcherAssert.assertThat(
                MorseCodeDecoder.decodeMorse(
                        MorseCodeDecoder.decodeBits(
                                "1100110011001100000011000000111111001100111111001111110000000000000011001111110011111100111111000000110011001111110000001111110011001100000011")),
                is("HEY JUDE"));
    }

    @Test
    public void testSomething() {
        String morseCode;
        String bits;
        /*
        bits = "";
        System.out.println(bits + " -> " + MorseCodeDecoder.decodeBitsAdvanced(bits));
        bits = "000";
        System.out.println(bits + " -> " + MorseCodeDecoder.decodeBitsAdvanced(bits));
        bits = "111";
        System.out.println(bits + " -> " + MorseCodeDecoder.decodeBitsAdvanced(bits));
        bits = "010";
        System.out.println(bits + " -> " + MorseCodeDecoder.decodeBitsAdvanced(bits));
        bits = "00011110001111110000000";
        System.out.println(bits + " -> " + MorseCodeDecoder.decodeBitsAdvanced(bits));
        */

        bits = "0000000011011010011100000110000001111110100111110011111100000000000111011111111011111011111000000101100011111100000111110011101100000100000";
        // bits = "1100110011001100000011000000111111001100111111001111110000000000000011001111110011111100111111000000110011001111110000001111110011001100000011";
        morseCode = MorseCodeDecoder.decodeBitsAdvanced(bits);
        System.out.println("Morse -> " + morseCode);
        // System.out.println(MorseCodeDecoder.decodeMorse(morseCode));
        System.out.println("text  -> " + MorseCodeDecoder.decodeMorse(morseCode));
        // System.out.println(MorseCodeDecoder.decodeMorse(".....- .- - .- - - ..- - ..."));
        /*
         * ".....- .- - .- - - ..- - ..."
         * ".... . -.--   .--- ..- -.. ."
         */
        assertEquals("HEY JUDE",
                MorseCodeDecoder.decodeMorse(
                        morseCode));
    }

    /*
     * THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG
     * 00000000000111111100000011010001110111000000001110000000000000000001111111011111100001101111100000111100111100011111100000001011100000011111110010001111100110000011111100101111100000000000000111111100001111010110000011000111110010000011111110001111110011111110000010001111110001111111100000001111111101110000000000000010110000111111110111100000111110111110011111110000000011111001011011111000000000000111011111011111011111000000010001001111100000111110111111110000001110011111100011111010000001100001001000000000000000000111111110011111011111100000010001001000011111000000100000000101111101000000000000011111100000011110100001001100000000001110000000000000001101111101111000100000100001111111110000000001111110011111100011101100000111111000011011111000111111000000000000000001111110000100110000011111101111111011111111100000001111110001111100001000000000000000000000000000000000000000000000000000000000000
     * One: 2.25, Two: 4.5, Three: 6.75, Five: 11.25, Seven: 15.75
     * - .... .   --....- .. -...-..   -......---.-- -.   ..-...- ....   .... ....- .-.. ...   -.- .... . ...   - .... .   .....- --..-..-   -..--- -..
     */

}