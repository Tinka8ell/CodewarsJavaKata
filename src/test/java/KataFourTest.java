import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class KataFourTest {

    @Test
    public void checkSpecial(){
        assertEquals(144, KataFour.nextSmaller(414));
    }
    @Test
    public void basicTests() {
        assertEquals(12, KataFour.nextSmaller(21));
        assertEquals(790, KataFour.nextSmaller(907));
        assertEquals(513, KataFour.nextSmaller(531));
        assertEquals(-1, KataFour.nextSmaller(1027));
        assertEquals(414, KataFour.nextSmaller(441));
        assertEquals(123456789, KataFour.nextSmaller(123456798));
    }
}