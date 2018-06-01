import java.util.ArrayList;

class Calculator {
    private Players players;

    public Calculator(Players players) {
        this.players = players;
    }

    //Calculates the player-dependant rules
    public int[] calcRules(int playerCnt) {
        int[] rules = new int[3];

        //Set starting ranks
        if (playerCnt >= 7) {
            rules[0] = 2;
        } else {
            rules[0] = 1;
        }

        //Set starting fame
        if (playerCnt < 5) {
            rules[1] = 0;
        } else if (playerCnt == 5) {
            rules[1] = 2;
        } else {
            rules[1] = 4;
        }

        //Set number of days to play
        if (playerCnt <= 3) {
            rules[2] = 3;
        } else {
            rules[2] = 4;
        }

        return rules;
    }

    //Returns the player with the highest score,
    //or the player who went earlier if two players tied
    public Player calcWinner(Players playersOrdered) {
        ArrayList<Player> playersList = this.players.getPlayers();
        Player winner = playersList.get(0);
        ArrayList<Player> orderedPlayerList = playersOrdered.getPlayers();
        Player orderedPlayer;
        int topScore = 0;
        for (int i = 0; i < playersList.size(); i++) {
            int newScore = playersList.get(i).getScore();
            if (newScore > topScore) {
                winner = playersList.get(i);
                topScore = newScore;
            } else if (newScore == topScore) {
                for (int j = 0; j < playersList.size(); j++) {
                    orderedPlayer = orderedPlayerList.get(j);
                    if (orderedPlayer.getName().equals(winner.getName())) {
                        //Winner is un-updated
                    } else if (orderedPlayer.getName().equals(playersList.get(i).getName())) {
                        winner = playersList.get(i);    //Winner is updated
                    }
                }
            }
        }
        return winner;
    }
}
