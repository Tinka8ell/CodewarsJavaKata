import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BefungeInterpreterTest {
    @Test
    public void tests() {
        assertEquals(
                "123456789",
                new BefungeInterpreter().interpret(">987v>.v\nv456<  :\n>321 ^ _@"));

    }

}