import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.Is.is;


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
                        MorseCodeDecoder.decodeBits("1100110011001100000011000000111111001100111111001111110000000000000011001111110011111100111111000000110011001111110000001111110011001100000011")),
                is("HEY JUDE"));
    }

}