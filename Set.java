import java.util.*;

class Set extends Room {
    private SceneCard card;
    private ArrayList<PlayerSpot> offCard;
    private int totalTakes;
    private int remainingTakes;

    public Set() {
        super(null, 8, null);
        this.totalTakes = 0;
        this.remainingTakes = 0;
        this.offCard = new ArrayList<PlayerSpot>(1);
        this.store=new RoomDataStore();
    }

//     //Changes room name
//     public void ChangeRoom(String name) {
//         this.RoomName = name;
//         this.store.setName(name);
//     }

    //Adds player spot
    public void addPlayerSpot(ArrayList<PlayerSpot> playerSpot) {
        this.offCard = playerSpot;
    }

    //Returns scene card
    public SceneCard getCard() {
        return this.card;
    }

    //Returns list of spots off the card
    public ArrayList<PlayerSpot> getRanksOffCard() {
        return this.offCard;
    }

    public void reset() {//resets room to state at beginning of day
        super.reset();
        this.remainingTakes = this.totalTakes;
        this.store.setShots(Integer.toString(totalTakes));
        this.card.resetCard();
        for (int i = 0; i < offCard.size(); i++) {
            PlayerSpot curr = offCard.get(i);
            curr.resetPlayerSpot();
        }
    }

    public void adjustTakes() {//once a take is complete, remove 1 from takes amount
        this.remainingTakes = this.remainingTakes - 1;
        this.store.setShots(Integer.toString(this.remainingTakes));
    }

    public void addCard(SceneCard card) {//adds a card to the set
        this.card = card;
    }

    public int getRemainingTakes() {//returns takes remaining
        return remainingTakes;
    }

    public HashMap<Player, Integer> getPlayers() {//returns HashMap of <Player, Integer rank of spot> if rank is above 10, player was on card(used for determining who gets what reward).
        // If rank was below 10, player was off card(player gets reward equal to rank)
        HashMap<Player, Integer> part = this.card.getPlayers();
        for (int i = 0; i < this.offCard.size(); i++) {
            PlayerSpot curr = this.offCard.get(i);
            ArrayList<Player> currSpotList = curr.getPlayers();
            int currSpotRank = curr.getRank();
            for (int j = 0; j < currSpotList.size(); j++) {
                part.put(currSpotList.get(j), currSpotRank);
            }
        }
        return part;
    }

    public void setTakes(int i) {//initializes number of takes
        this.totalTakes = i;
        this.remainingTakes = i;
        this.store.setShots(Integer.toString(i));
    }
}