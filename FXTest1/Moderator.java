import java.util.*;

class Moderator {
    private Players players;
    private Deck deck;
    private Board board;

    public Moderator(Players players, Deck deck, Board board) {
        this.players = players;
        this.deck = deck;
        this.board = board;
    }

    //Give money and fame to specified player
    public void giveFunds(Player player, int money, int fame) {
        player.setCredit(money);
        player.setFame(fame);
    }

    //Give rank increase to specified player
    public void increaseRank(Player player, int rankIncrease) {//increases player rank
        player.setRank(rankIncrease);
    }

    public void advanceScene(Room set) {//performs interactions necessary when scene is wrapped
        SceneCard currCard = set.getCard();
        HashMap<Player, Integer> pList = set.getPlayers();
        int numToRoll = currCard.getCost();
        PriorityQueue<Integer> pQ = new PriorityQueue<Integer>(Collections.reverseOrder());
        ArrayList<Player> pQList = new ArrayList<Player>(6);
        for (int i = 0; i < numToRoll; i++) {
            pQ.add(RandomNum(1, 6));
        }
        for (Map.Entry<Player, Integer> entry : pList.entrySet()) {
            Player currPlayer = entry.getKey();
            currPlayer.remFromCard();               //Remove player from card
            currPlayer.resetRehearse();             //Remove rehearse markers
            int currSpotRank = entry.getValue();
            if (currSpotRank < 10) {
                currPlayer.setCredit(currSpotRank);
            } else {
                pQList.add(currSpotRank - 11, currPlayer);
            }

        }
        int left = numToRoll - 1;
        int counter = 0;
        int loopcatcher = 0;
        while (left != 0 && loopcatcher < 50) {
            if (pList.isEmpty()) {
                left = 0;
                break;
            }
            int index = 6 - counter / 6;
            try {
                Player cP = pQList.get(index - 1);
                cP.setCredit(pQ.poll());
                left--;
                counter++;
            } catch (Exception e) {
                counter++;
                loopcatcher++;
            }
        }
    }

    public void advanceDay() {//resets everything that needs to be reset for a day change.
        this.deck.reset();
        this.players.reset();
        this.board.reset();
    }

    //RandomNum:
    //in: int a int b
    //out: random int in [a,b]
    //notes: helper function to generate random numbers.
    private int RandomNum(int a, int b) {
        Random rand = new Random();
        int n = rand.nextInt(b) + a;
        return n;
    }//end RandomNum
}