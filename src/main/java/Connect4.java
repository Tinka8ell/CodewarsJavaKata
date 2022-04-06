public class Connect4 {

    private boolean player1 = true;

    public String play(int column) {
        player1 = ! player1;
        return "Player " + (player1 ? "2" : "1") + " has a turn";
    }

}