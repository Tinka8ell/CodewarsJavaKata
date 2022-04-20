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
        String bits = "0000000011011010011100000110000001111110100111110011111100000000000111011111111011111011111000000101100011111100000111110011101100000100000";
        // bits = "1100110011001100000011000000111111001100111111001111110000000000000011001111110011111100111111000000110011001111110000001111110011001100000011";
        String morseCode = MorseCodeDecoder.decodeBitsAdvanced(
                bits);
        System.out.println(morseCode);
        System.out.println(MorseCodeDecoder.decodeMorse(morseCode));
        // System.out.println(MorseCodeDecoder.decodeMorse(".....- .- - .- - - ..- - ..."));
        /*
         * ".....- .- - .- - - ..- - ..."
         * ".... . -.--   .--- ..- -.. ."
         */
        assertEquals("HEY JUDE",
                MorseCodeDecoder.decodeMorse(
                        morseCode));
    }

}