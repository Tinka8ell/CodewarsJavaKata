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

    /**
     * input value is
     * +-----------------+
     * |                 |
     * |   +-------------+
     * |   |
     * |   |
     * |   |
     * |   +-------------+
     * |                 |
     * |                 |
     * +-----------------+
     * the expected value is:
     *
     * +-----------------+
     * |                 |
     * |   +-------------+
     * |   |
     * |   |
     * |   |
     * |   +-------------+
     * |                 |
     * |                 |
     * +-----------------+
     */
    @Test
    public void suffixTest(){
        String shape = String.join("\n", new String[] {
                "+-----------------+",
                "|                 |",
                "|   +-------------+",
                "|   |",
                "|   |",
                "|   |",
                "|   +-------------+",
                "|                 |",
                "|                 |",
                "+-----------------+"
        });
        String[] expected = {shape};
        String[] actual = BreakPieces.process(shape);
        Arrays.sort(expected);
        Arrays.sort(actual);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void prefixTest(){
        String shape = String.join("\n", new String[] {
                "+-----------------+",
                "|                 |",
                "+-------------+   |",
                "              |   |",
                "              |   |",
                "              |   |",
                "+-------------+   |",
                "|                 |",
                "|                 |",
                "+-----------------+"
        });
        String[] expected = {shape};
        String[] actual = BreakPieces.process(shape);
        Arrays.sort(expected);
        Arrays.sort(actual);
        assertArrayEquals(expected, actual);
    }

    /**
     * input value is
     *            +-+
     *            | |
     *          +-+-+-+
     *          |     |
     *       +--+-----+--+
     *       |           |
     *    +--+-----------+--+
     *    |                 |
     *    +-----------------+
     * the expected value is:
     *
     * +-+
     * | |
     * +-+
     * +-----+
     * |     |
     * +-----+
     * +-----------+
     * |           |
     * +-----------+
     * +-----------------+
     * |                 |
     * +-----------------+
     */
    @Test
    public void weddingCakeTest(){
        String shape = String.join("\n", new String[] {
                "        +-+",
                "        | |",
                "      +-+-+-+",
                "      |     |",
                "   +--+-----+--+",
                "   |           |",
                "+--+-----------+--+",
                "|                 |",
                "+-----------------+"
        });
        String shape1 = String.join("\n", new String[] {
                "+-+",
                "| |",
                "+-+"
        });
        String shape2 = String.join("\n", new String[] {
                "+-----+",
                "|     |",
                "+-----+"
        });
        String shape3 = String.join("\n", new String[] {
                "+-----------+",
                "|           |",
                "+-----------+"
        });
        String shape4 = String.join("\n", new String[] {
                "+-----------------+",
                "|                 |",
                "+-----------------+"
        });
        String[] expected = {shape1, shape2, shape3, shape4};
        String[] actual = BreakPieces.process(shape);
        Arrays.sort(expected);
        Arrays.sort(actual);
        assertArrayEquals(expected, actual);
    }

    /**
     * input value is
     * +-------------------+--+
     * |                   |  |
     * |                   |  |
     * |  +----------------+  |
     * |  |                   |
     * |  |                   |
     * +--+-------------------+
     * the expected value is:
     *
     *                  +--+
     *                  |  |
     *                  |  |
     * +----------------+  |
     * |                   |
     * |                   |
     * +-------------------+
     * +-------------------+
     * |                   |
     * |                   |
     * |  +----------------+
     * |  |
     * |  |
     * +--+
     */
    @Test
    public void yingAndYangTest(){
        String shape = String.join("\n", new String[] {
                "+-------------------+--+",
                "|                   |  |",
                "|                   |  |",
                "|  +----------------+  |",
                "|  |                   |",
                "|  |                   |",
                "+--+-------------------+"
        });
        String shapeL = String.join("\n", new String[] {
                "+-------------------+",
                "|                   |",
                "|                   |",
                "|  +----------------+",
                "|  |",
                "|  |",
                "+--+"
        });
        String shapeR = String.join("\n", new String[] {
                "                 +--+",
                "                 |  |",
                "                 |  |",
                "+----------------+  |",
                "|                   |",
                "|                   |",
                "+-------------------+"
        });
        String[] expected = {shapeL, shapeR};
        String[] actual = BreakPieces.process(shape);
        Arrays.sort(expected);
        Arrays.sort(actual);
        assertArrayEquals(expected, actual);
    }

    /**
     * input value is
     *
     *    +-----+
     *    |     |
     *    |     |
     *    +-----+-----+
     *          |     |
     *          |     |
     *          +-----+
     * the expected value is:
     * +-----+
     * |     |
     * |     |
     * +-----+
     * +-----+
     * |     |
     * |     |
     * +-----+
     */
    @Test
    public void harlequinTest(){
        String shape = String.join("\n", new String[] {
                "",
                "   +-----+       ",
                "   |     |       ",
                "   |     |       ",
                "   +-----+-----+ ",
                "         |     | ",
                "         |     | ",
                "         +-----+ "
        });
        String expectedShape = String.join("\n", new String[] {
                "+-----+",
                "|     |",
                "|     |",
                "+-----+"
        });
        String[] expected = {expectedShape, expectedShape};
        String[] actual = BreakPieces.process(shape);
        Arrays.sort(expected);
        Arrays.sort(actual);
        assertArrayEquals(expected, actual);
    }
}
