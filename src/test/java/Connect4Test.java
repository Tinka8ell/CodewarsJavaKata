import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class Connect4Test {

    private Connect4 game;

    @BeforeEach
    public void setup(){
        game = new Connect4();
    }

    // verifies the response for every move
    void runTheTest(int[] moves, String[] expected) {
        Connect4 game = new Connect4();
        for (int i = 0; i < moves.length; i++) {
            String response = game.play(moves[i]);
            assertEquals(expected[i], response, "Should return: '" + expected[i] + "'");
        }
    }

    // verifies the response after the last move
    void runTheTest(int[] moves, String lastExpected) {
        Connect4 game = new Connect4();
        String response = null;
        for (int i = 0; i < moves.length; i++) {
            response = game.play(moves[i]);
        }
        assertEquals(lastExpected, response, "Should return: '" + lastExpected + "'");
    }

    @ParameterizedTest
    @CsvSource({
            "Player 1 has a turn, 0",
            "Player 2 has a turn, 01",
            "Player 1 has a turn, 010",
            "Player 2 has a turn, 0101",
            "Player 1 has a turn, 01010",
            "Player 2 has a turn, 010101",
            "Player 1 wins!, 0101010"})
    void test1(String expected, String moves) {
        String response = "";
        for (int move: moves.toCharArray()) {
            response = game.play(move - '0');
        }
        assertEquals(expected, response, "Should return: '" + expected + "' after '" + moves + "'");
    }

    @ParameterizedTest
    @CsvSource({
            "Player 1 has a turn, 3",
            "Player 2 has a turn, 30",
            "Player 1 has a turn, 301",
            "Player 2 has a turn, 3010",
            "Player 1 has a turn, 30101",
            "Player 2 has a turn, 301010",
            "Player 1 has a turn, 3010101",
            "Player 2 wins!, 30101010"})
    void test2(String expected, String moves) {
        String response = "";
        for (int move: moves.toCharArray()) {
            response = game.play(move - '0');
        }
        assertEquals(expected, response, "Should return: '" + expected + "' after '" + moves + "'");
    }

    @ParameterizedTest
    @CsvSource({
            "Player 1 has a turn, 0",
            "Player 2 has a turn, 00",
            "Player 1 has a turn, 001",
            "Player 2 has a turn, 0011",
            "Player 1 has a turn, 00112",
            "Player 2 has a turn, 001122",
            "Player 1 wins!, 0011223"})
    void testHorizontal(String expected, String moves) {
        String response = "";
        for (int move: moves.toCharArray()) {
            response = game.play(move - '0');
        }
        assertEquals(expected, response, "Should return: '" + expected + "' after '" + moves + "'");
    }

    @Test
    void testContinueAfterVerticalFinish() {
        int[] moves = new int[] { 1, 2, 1, 2, 1, 2, 1, 2 };
        runTheTest(moves, "Game has finished!");
    }

    @Test
    void testContinueAfterFinish() {
        int[] moves = new int[] { 1, 1, 2, 2, 3, 3, 4, 4 };
        runTheTest(moves, "Game has finished!");
    }

    @Test
    void testFullColumn() {
        int[] moves = new int[] { 4, 4, 4, 4, 4, 4, 4 };
        runTheTest(moves, "Column full!");
    }

}