import java.util.ArrayList;

public class Connect4 {

    private Player next = Player.RED;
    private final ArrayList<Player>[] board = new ArrayList[7];
    private boolean finished = false;

    public Connect4() {
        for (int col = 0; col < board.length; col++) {
            board[col] = new ArrayList<>();
        }
    }

    enum Player{
        NONE,
        RED,
        YELLOW
    }

    public String play(int column) {
        if (finished)
            return "Game has finished!";
        int top = board[column].size();
        if (top >= 6)
            return "Column full!";
        board[column].add(next);
        top++;

        // check vertical line
        if (top >= 4) {
            // a column of at least 4
            finished = true;
            for (int row = top - 4; row < top; row++){
                finished = finished && (board[column].get(row) == next);
                if (!finished)
                    break;
            }
        }
        if (!finished){
            // check horizontal line
            int row = top - 1;
            // find a possible start
            int start = column;
            for (int col = column - 1; col >= 0 ; col--) {
                if (board[col].size() > row && board[col].get(row) == next)
                    start = col;
                else
                    break;
            }
            int last = start + 4;
            if (last <= 7){
                finished  = true;
                for (int col = column + 1; col < last; col++){
                    finished = finished && (board[col].size() > row) && (board[col].get(row) == next);
                    if (!finished)
                        break;
                }
            }
        }
        if (!finished) {
            // check diagonals here!
        }
        if (finished)
            return playerName() + " wins!";
        String response = playerName() + " has a turn";
        next = next == Player.RED ? Player.YELLOW : Player.RED;
        return response;
    }

    private String playerName() {
        return "Player " + (next == Player.RED ? "1" : "2");
    }

}