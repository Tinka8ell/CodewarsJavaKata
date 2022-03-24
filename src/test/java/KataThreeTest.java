import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class KataThreeTest {
    @Test
    public void exampleTests() {
        assertEquals('e', KataThree.findMissingLetter(new char[] { 'a','b','c','d','f' }));
        assertEquals('P', KataThree.findMissingLetter(new char[] { 'O','Q','R','S' }));
    }
}