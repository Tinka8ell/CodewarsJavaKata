import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class KataTwoTest {
    @Test
    public void sampleTests() {
        assertEquals("taxi", KataTwo.high("man i need a taxi up to ubud"));
        assertEquals("volcano", KataTwo.high("what time are we climbing up to the volcano"));
        assertEquals("semynak", KataTwo.high("take me to semynak"));
    }

    @Test
    public void edgeCaseTests() {
        assertEquals("aa", KataTwo.high("aa b"));
        assertEquals("b", KataTwo.high("b aa"));
        assertEquals("bb", KataTwo.high("bb d"));
        assertEquals("d", KataTwo.high("d bb"));
        assertEquals("aaa", KataTwo.high("aaa b"));
    }
}