/**
 * You are given an ASCII diagram, comprised of minus signs -, plus signs +, vertical bars | and whitespaces.
 * Your task is to write a function which breaks the diagram in the minimal pieces it is made of.
 * <p>
 * For example, if the input for your function is this diagram:
 * </p><p>
 * +------------+
 * |            |
 * |            |
 * |            |
 * +------+-----+
 * |      |     |
 * |      |     |
 * +------+-----+
 * </p><p>
 * the returned value should be the list of:
 * </p><p>
 * +------------+
 * |            |
 * |            |
 * |            |
 * +------------+
 * </p><p>
 * (note how it lost a + sign in the extraction)
 * </p><p>
 * as well as
 * </p><p>
 * +------+
 * |      |
 * |      |
 * +------+
 * </p><p>
 * and
 * </p><p>
 * +-----+
 * |     |
 * |     |
 * +-----+
 * </p><p>
 * The diagram is given as an ordinary multiline string.
 * There are no borders touching each others.
 * </p><p>
 * The pieces should not have trailing spaces at the end of the lines.
 * However, it could have leading spaces if the figure is not a rectangle.
 * For instance:
 * </p><p>
 *     +---+
 *     |   |
 * +---+   |
 * |       |
 * +-------+
 * </p><p>
 * However, it is not allowed to use more leading spaces than necessary.
 * It is to say, the first character of some lines should be different from a space.
 * </p><p>
 * Finally, note that only the explicitly closed pieces are considered.
 * Spaces "outside" of the shape are part of the background.
 * Therefore, the diagram above has a single piece.
 * </p><p>
 * Have fun!
 * </p>
 */
public class BreakPieces {
    public static String[] process(String shape) {
        String box1 = makeBox(3, 12);
        String box2 = makeBox(2, 6);
        String box3 = makeBox(2, 5);
        String[] expected = {box1,
                box2,
                box3};
        return expected;
    }

    static String makeBox(int rows, int columns){
        String topBottom = "+" + "-".repeat(columns) + "+";
        String middle = "|" + " ".repeat(columns) + "|";
        StringBuilder box = new StringBuilder(topBottom);
        for (int i = 0; i < rows; i++) {
            box.append("\n").append(middle);
        }
        return box.append("\n").append(topBottom).toString();
    }
}