import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BatmanQuotesTest {

    static String[] quotes = { "Quote Hidden in example test",
            "Holy haberdashery, Batman!",
            "Quote Hidden in example test"
    };

    @Test
    public void test1(){
        assertEquals("Robin: Holy haberdashery, Batman!", BatmanQuotes.getQuote(quotes, "Rob1n"));
    }

}