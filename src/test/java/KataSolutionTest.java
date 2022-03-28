import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KataSolutionTest {

    @Test
    public void testToString() {
        assertEquals("x+1",
                (new KataSolution.BinomialExpansion("x", new long[]{1, 1L
                })).toString());
        assertEquals("-x-1",
                (new KataSolution.BinomialExpansion("x", new long[]{-1, -1})).toString());
        assertEquals("x^2+2x+1",
                (new KataSolution.BinomialExpansion("x", new long[]{1, 2, 1})).toString());
        assertEquals("p^3-3p^2+3p-1",
                (new KataSolution.BinomialExpansion("p", new long[]{-1, 3, -3, 1})).toString());
        assertEquals("64f^6+768f^5+3840f^4+10240f^3+15360f^2+12288f+4096",
                (new KataSolution.BinomialExpansion("f", new long[]{4096, 12288, 15360, 10240, 3840, 768, 64})).toString());
        assertEquals("1", (new KataSolution.BinomialExpansion("x",
                new long[]{1})).toString());
        assertEquals("144t^2-1032t+1849",
                (new KataSolution.BinomialExpansion("t", new long[]{1849, -1032, 144})).toString());
    }

    @Test
    public void testBPositive() {
        assertEquals("1", KataSolution.expand("(x+1)^0"));
        assertEquals("x+1", KataSolution.expand("(x+1)^1"));
        assertEquals("x^2+2x+1", KataSolution.expand("(x+1)^2"));
    }

    @Test
    public void testBNegative() {
        assertEquals("1", KataSolution.expand("(x-1)^0"));
        assertEquals("x-1", KataSolution.expand("(x-1)^1"));
        assertEquals("x^2-2x+1", KataSolution.expand("(x-1)^2"));
    }

    @Test
    public void testAPositive() {
        assertEquals("625m^4+1500m^3+1350m^2+540m+81", KataSolution.expand("(5m+3)^4"));
        assertEquals("8x^3-36x^2+54x-27", KataSolution.expand("(2x-3)^3"));
        assertEquals("1", KataSolution.expand("(7x-7)^0"));
    }

    @Test
    public void testANegative() {
        assertEquals("625m^4-1500m^3+1350m^2-540m+81", KataSolution.expand("(-5m+3)^4"));
        assertEquals("-8k^3-36k^2-54k-27", KataSolution.expand("(-2k-3)^3"));
        assertEquals("1", KataSolution.expand("(-7x-7)^0"));
    }
}
