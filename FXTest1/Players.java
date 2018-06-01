import java.util.*;

class Players {
    private ArrayDeque<Player> list;
    private ArrayList<Player> nonQList;
    private int size;

    public Players(int a) {
        this.list = new ArrayDeque<Player>();
        this.nonQList = new ArrayList<Player>(a);
        this.size = a;
    }

    public void addPlayers(String[] playerList) {//makes a new player for each string in playerList
        for (int i = 0; i < playerList.length; i++) {
            Player currP = new Player(playerList[i]);
            list.addFirst(currP);
            nonQList.add(currP);
        }
    }

    public void shuffle() {//shuffles order of players in list
        ArrayList<Player> pList = new ArrayList<Player>(this.size);
        while (this.list.isEmpty() != true) {
            pList.add(list.poll());
        }
        while (pList.isEmpty() != true) {
            int i = RandomNum(0, pList.size());
            this.list.addFirst(pList.get(i));
            pList.remove(i);
        }

    }

    public Player getCurrent() {//returns the current player
        return list.getFirst();
    }

    public Player nextCurrent() {//places current at end of queue, return new top
        Player last = this.list.poll();
        this.list.addLast(last);
        return this.list.getFirst();
    }

    public void reset() {//places each player back at the trailers
        for (int i = 0; i < nonQList.size(); i++) {
            Player curr = this.nonQList.get(i);
            curr.remFromCard();
            curr.changeLocation(10);
        }
    }

    public int getSize() {//returns number of players
        return this.size;
    }

    public ArrayList<Player> getPlayers() {//returns arrayList of player
        return this.nonQList;
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