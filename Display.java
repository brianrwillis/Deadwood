import java.util.ArrayList;

class Display {
    private Players players;
    private Board board;

    public Display(Players players, Board board) {
        this.players = players;
        this.board = board;
    }

    //Displays all players' stats
    public void dispPlayers() {
        ArrayList<Player> playersList = this.players.getPlayers();

        for (int i = 0; i < playersList.size(); i++) {
            System.out.println(playersList.get(i).getName() + "'s money: " + playersList.get(i).getCredit());
            System.out.println(playersList.get(i).getName() + "'s fame: " + playersList.get(i).getFame());
            System.out.println(playersList.get(i).getName() + "'s rank: " + playersList.get(i).getRank());
            System.out.println(playersList.get(i).getName() + "'s rehearse markers: " + playersList.get(i).getRehearse());
            System.out.println(playersList.get(i).getName() + "'s location: " + this.board.getRoom(playersList.get(i).getLocation()).getName());
            if (i != playersList.size() - 1) {
                System.out.println("------------------------");
            }
        }

    }
}
