import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static final Pattern SPACES = Pattern.compile(" +");

    public static String[] process(String shape) {
        // System.out.println("input shape:\n" + shape);
        List<List<Section>> sections = toLines(shape);
        List<BoxSection> boxSections = new ArrayList<>();
        List<BoxSection> current = new ArrayList<>();
        for (List<Section> line: sections) {
            List<BoxSection> next = new ArrayList<>();
            for (Section section: line) {
                boolean matched = false;
                for (BoxSection box: current) {
                    matched = box.matchAndAdd(section);
                    if (matched){
                        next.add(box);
                        current.remove(box);
                        break;
                    }
                }
                if (!matched){
                    next.add(new BoxSection(section));
                }
            }
            boxSections.addAll(current);
            current = next;
        }
        boxSections.addAll(current);
        String[] boxes = new String[boxSections.size()];
        for (int i = 0; i < boxes.length; i++) {
            BoxSection box = boxSections.get(i);
            boxes[i] = makeBox(box);
        }
        return boxes;
    }

    private static List<List<Section>> toLines(String shape) {
        String[] stringLines = shape.split("\n");
        List<List<Section>> lines = new ArrayList<>();
        for (String line: stringLines) {
            List<Section> intLine = new ArrayList<>();
            Matcher matcher = SPACES.matcher(line);
            while (matcher.find()){
                int start = matcher.start();
                int end = matcher.end();
                if (start > 0 && // unless it's just an indent!
                    end < line.length()) // or trailing spaces
                {
                    intLine.add(new Section(start, end));
                }
            }
            // System.out.println(intLine);
            lines.add(intLine);
        }
        return lines;
    }

    private static class Section {
        public int start;
        public int length;

        public Section(int start, int end) {
            this.start = start;
            this.length = end - start;
        }

        @Override
        public String toString() {
            return "{" + start + ", " + length + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Section section)) return false;
            return start == section.start && length == section.length;
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, length);
        }

        public boolean overlaps(Section section) {
            int end = start + length;
            int sectionEnd = section.start + section.length;
            return ((section.start >= start && section.start < end) ||
                    (sectionEnd > start && sectionEnd <= end));
        }
    }

    private static class BoxSection {
        public List<Section> rows;
        public Section last;

        public BoxSection(Section section) {
            rows = new ArrayList<>();
            last = section;
            rows.add(last);
        }

        public boolean matchAndAdd(Section section){
            boolean pass = last.overlaps(section);
            if (pass) {
                last = section;
                rows.add(last);
            }
            return pass;
        }

        @Override
        public String toString() {
            return "[" + rows + ']';
        }
    }

    static String makeBox(BoxSection boxSection){
        // System.out.println("output box");
        // System.out.println(boxSection);
        int left = boxSection
                .rows
                .stream()
                .map(r -> r.start)
                .min(Integer::compareTo)
                .orElse(0);
        StringBuilder box = new StringBuilder();
        int previousBefore = Integer.MAX_VALUE;
        int previousAfter = 0;
        int currentBefore = Integer.MAX_VALUE;
        int currentAfter = 0;
        int nextBefore = Integer.MAX_VALUE;
        int nextAfter = 0;
        for (Section row: boxSection.rows) {
            // System.out.println(row);
            nextBefore = row.start - left;
            nextAfter = nextBefore + row.length;
            box
                    .append(drawRow(
                            previousBefore, previousAfter,
                            currentBefore, currentAfter,
                            nextBefore, nextAfter))
                    .append("\n");
            previousAfter = currentAfter;
            previousBefore = currentBefore;
            currentAfter = nextAfter;
            currentBefore = nextBefore;
        }
        // add last row
        box
                .append(drawRow(
                        previousBefore, previousAfter,
                        currentBefore, currentAfter,
                        nextBefore, nextAfter))
                .append("\n");
        previousAfter = currentAfter;
        previousBefore = currentBefore;
        currentAfter = 0;
        currentBefore = 0;
        // and border
        box
                .append(drawRow(
                        previousBefore, previousAfter,
                        currentBefore, currentAfter,
                        nextBefore, nextAfter));
        return box.toString();
    }

    private static String drawRow(
            int previousBefore, int previousAfter,
            int currentBefore, int currentAfter,
            int nextBefore, int nextAfter) {
        /*
        System.out.println(Arrays.toString(new int[]{
                previousBefore, previousAfter,
                currentBefore, currentAfter,
                nextBefore, nextAfter}));
         */
        int indent = currentBefore;
        int prefix = 0;
        int width = currentAfter - currentBefore;
        int suffix = 0;
        if (width < 0){ // first line
            indent = currentAfter = currentBefore = previousBefore = nextBefore;
            width = 0;
            previousAfter = nextAfter;
        }
        int left = Math.min(previousBefore, nextBefore);
        if (left < currentBefore){
            indent = left;
            prefix = currentBefore - left;
        }
        int right = Math.max(previousAfter, nextAfter);
        if (right > currentAfter) {
            suffix = right - currentAfter;
        }
        if (suffix > 0 && width == 0)
            suffix ++;
        String row = " ".repeat(indent) +
                (prefix > 0 ? "+" : (width > 0 ? "|" : "") ) +
                (prefix > 1 ? "-".repeat(prefix - 1) + "+" : "") +
                " ".repeat(width) +
                (suffix > 1 ? "+" + "-".repeat(suffix - 1) : "") +
                (suffix > 0 ? "+" : (width > 0 ? "|" : "") );
        // System.out.println(row);
        return row;
    }
}