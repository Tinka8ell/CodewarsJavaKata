import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class BreakPiecesTest {

    @Test
    public void minimalTest() {
        String shape = String.join("\n", new String[] {"+------------+",
                "|            |",
                "|            |",
                "|            |",
                "+------------+"});
        String[] expected = {shape};
        String[] actual = BreakPieces.process(shape);
        Arrays.sort(expected);
        Arrays.sort(actual);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void simpleTest() {
        String shape = String.join("\n", new String[] {"+------------+",
                "|            |",
                "|            |",
                "|            |",
                "+------+-----+",
                "|      |     |",
                "|      |     |",
                "+------+-----+"});
        String[] expected = {String.join("\n", new String[] {"+------------+",
                "|            |",
                "|            |",
                "|            |",
                "+------------+"}),
                String.join("\n", new String[] {"+------+",
                        "|      |",
                        "|      |",
                        "+------+"}),
                String.join("\n", new String[] {"+-----+",
                        "|     |",
                        "|     |",
                        "+-----+"})};
        String[] actual = BreakPieces.process(shape);
        assertNotNull(actual);
        System.out.println("Boxes: " + actual.length);
        for (String box: actual) {
            System.out.println(box);
        }
        Arrays.sort(expected);
        Arrays.sort(actual);
        assertArrayEquals(expected, actual);
    }
}