import java.util.ArrayList;

public class Connect4 {

    private static final int debug = 0;
    private final StringBuilder history = new StringBuilder();
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

    @Override
    public String toString() {
        StringBuilder response = new StringBuilder();
        for (int row = 0; row < 6; row++) {
            response.append("|");
            for (int col = 0; col < 7; col++) {
                char p = ' ';
                if (board[col].size() > row)
                    p = board[col].get(row) == Player.RED ? 'X' : 'O';
                response.append(p);
            }
            response.append("|\n");
        }
        return response.toString();
    }

    public String play(int column) {
        history.append(column);
        if (debug > 1){
            history.append(", ");
            System.err.println(history);
        }
        if (finished) {
            if (debug > 0){
                System.err.println(history);
                System.err.println(this + "Game has finished!");
            }
            return "Game has finished!";
        }
        int top = board[column].size();
        if (top >= 6) {
            if (debug > 0) {
                System.err.println(history);
                System.err.println(this + "Column full!");
            }
            return "Column full!";
        }
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

        // if not already won, check horizontal line
        int row = top - 1;
        if (!finished){
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
        // if not already won, check upward diagonal
        if (!finished) {
            // find a possible start
            int start = column;
            int r = row - 1;
            for (int col = column - 1; col >= 0 ; col--) {
                if (r < 0)
                    break;
                if (board[col].size() > r && board[col].get(r) == next){
                    start = col;
                }
                else
                    break;
                r--;
            }
            int last = start + 4;
            if (last <= 7){
                finished  = true;
                r = row + 1;
                for (int col = column + 1; col < last; col++){
                    finished = finished && (board[col].size() > r) && (board[col].get(r) == next);
                    if (!finished)
                        break;
                    r++;
                }
            }
        }
        // if not already won, check downward diagonal
        if (!finished) {
            // find a possible start
            int start = column;
            int r = row + 1;
            for (int col = column - 1; col >= 0 ; col--) {
                if (board[col].size() > r && board[col].get(r) == next){
                    start = col;
                }
                else
                    break;
                r++;
            }
            int last = start + 4;
            if (last <= 7){
                finished  = true;
                r = row - 1;
                for (int col = column + 1; col < last; col++){
                    finished = finished && (r >= 0) && (board[col].size() > r) && (board[col].get(r) == next);
                    if (!finished)
                        break;
                    r--;
                }
            }
        }
        if (finished) {
            if (debug > 0) {
                System.err.println(history);
                System.err.println(this + playerName() + " wins!");
            }
            return playerName() + " wins!";
        }
        String response = playerName() + " has a turn";
        if (debug > 1) {
            if (history.length() >= 20) {
                System.err.println(history);
                System.err.println(this + "Not finished, so " + response);
            }
        }
        if (debug > 1) {
            if (history.length() >= 42) {
                System.err.println(history);
                System.err.println(this + "Full, but " + response);
            }
        }
        next = next == Player.RED ? Player.YELLOW : Player.RED;
        return response;
    }

    private String playerName() {
        return "Player " + (next == Player.RED ? "1" : "2");
    }

}