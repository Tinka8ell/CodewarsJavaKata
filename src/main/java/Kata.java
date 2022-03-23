import java.util.stream.Stream;

public class Kata {

    private static class StringScore implements Comparable<StringScore> {

        String word;
        int score;

        public StringScore(String text) {
            word = text;
            score = word.chars().map((c) -> c - 'a' + 1).sum();
        }

        @Override
        public int compareTo(StringScore otherWord) {
            // sort in reverse of score value
            return Integer.compare(otherWord.getScore(), getScore());
        }

        private int getScore() {
            return score;
        }

        public String toWord() {
            return word;
        }
    }

    public static String high(String s) {
        return Stream.of(s.split(" "))
                .map(StringScore::new)
                .sorted()
                .findFirst()
                .get().toWord();
    }

}