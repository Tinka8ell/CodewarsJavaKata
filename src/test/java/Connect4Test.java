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

    // verifies the response after the last move
    void runTheTest(int[] moves, String lastExpected) {
        String response = null;
        for (int move : moves) {
            response = game.play(move);
        }
        // System.out.println(game);
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
    void testContinueVerticalWin() {
        int[] moves = new int[] { 1, 2, 1, 2, 1, 2, 1};
        runTheTest(moves, "Player 1 wins!");
    }

    @Test
    void testContinueHorizontalWin() {
        int[] moves = new int[] { 1, 1, 2, 2, 3, 3, 4};
        runTheTest(moves, "Player 1 wins!");
    }

    @Test
    void testContinueUpWin() { // r  y  r  y  r  y  r  y  r  y  r
        int[] moves = new int[] { 1, 2, 2, 2, 3, 3, 3, 4, 4, 4, 4};
        runTheTest(moves, "Player 1 wins!");
    }

    @Test
    void testContinueDownWin(){ //r  y  r  y  r  y  r  y  r  y  r
        int[] moves = new int[] { 2, 1, 1, 1, 1, 2, 2, 2, 4, 3, 3};
        runTheTest(moves, "Player 1 wins!");
    }

    @Test
    void testFullColumn() {
        int[] moves = new int[] { 4, 4, 4, 4, 4, 4, 4 };
        runTheTest(moves, "Column full!");
    }

    @Test
    void testWin1() { //          r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r
        int[] moves = new int[] { 3, 3, 3, 2, 4, 2, 2, 4, 4, 0, 6, 3, 4, 3, 4, 4, 2, 3, 6, 0, 0, 0, 6, 6, 0, 0, 2, 2, 6, 6, 5 };
        runTheTest(moves, "Player 1 wins!");
    }

    @Test
    void testWin2() { //          r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r
        int[] moves = new int[] { 3, 3, 4, 2, 3, 2, 4, 4, 5, 6, 3, 4, 0, 4, 4, 3, 0, 1, 1, 2, 2};
        //                        334232445634044301122
        runTheTest(moves, "Player 1 wins!");
    }

    @Test
    void testWin3() { //          r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r
        int[] moves = new int[] { 2, 2, 1, 3, 1, 1, 0, 3, 0, 4, 0, 0};
        //
        runTheTest(moves, "Player 2 wins!");
    }

    @Test
    void testWin4() { //          r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r
        int[] moves = new int[] { 2, 5, 4, 6, 3, 6, 1};
        //
        runTheTest(moves, "Player 1 wins!");
    }

    @Test
    void testWin5() { //          r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r  y  r
        int[] moves = new int[] { 2, 1, 5, 0, 2, 4, 6, 0, 4, 2, 6, 6, 4, 1, 1, 3, 0, 5, 5, 2, 3};
        //
        runTheTest(moves, "Player 1 has a turn");
    }

}