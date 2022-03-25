import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LargeFactorialsTest {

    @Test
    public void BasicTests()
    {
        assertEquals("1", LargeFactorials.Factorial(0));
        assertEquals("1", LargeFactorials.Factorial(1));
        assertEquals("120", LargeFactorials.Factorial(5));
        assertEquals("362880", LargeFactorials.Factorial(9));
        assertEquals("1307674368000", LargeFactorials.Factorial(15));
    }

}