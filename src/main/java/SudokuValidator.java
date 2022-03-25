import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SudokuValidator {
    public static boolean checkBlock(int[] sudokuRow) {
        Set<Integer> digits = new HashSet<>();
        digits.add(0); // so 0 will fail
        return Arrays.stream(sudokuRow)
                .allMatch(digits::add);
    }

    public static boolean checkRows(int[][] sudoku) {
        return Arrays.stream(sudoku)
                .allMatch(SudokuValidator::checkBlock);
    }

    public static boolean check(int[][] sudoku) {
        boolean pass = checkRows(sudoku);
        if (pass){
            // mirror on diagonal:
            int[][] mirror = new int[9][9];
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    mirror[col][row] = sudoku[row][col];
                }
            }
            pass = checkRows(mirror);
            if (pass){
                // finally, check blocks
                int[][] blocks = new int[9][9];
                for (int i = 0; i < 9; i++) {
                    int row = 3 * (i % 3);
                    int col = 3 * (i / 3);
                    for (int j = 0; j < 9; j++) {
                        int addRow = (j % 3);
                        int addCol = (j / 3);
                        blocks[i][j] = sudoku[row + addRow][col + addCol];
                    }
                }
                pass = checkRows(blocks);
            }
        }
        return pass;
    }
}