import java.util.*;

class SceneCard {
    private int sceneCost;
    private String cardName;
    private boolean active;
    private String description;
    private String picName;
    private ArrayList<PlayerSpot> players;
    int sceneNum;
    boolean flipped;

    //Returns true if card is active
    public boolean isActive() {
        return this.active;
    }
       public void AddPicName(String picName){
      this.picName=picName;
    }
    public String getPicName(){
      return this.picName;
    }
    public SceneCard(int cost, String name) {
        this.cardName = name;
        this.sceneCost = cost;
        this.sceneCost = cost;
        this.players = new ArrayList<PlayerSpot>();
        this.active = false;
    }

    //Gets cost of card
    public int getCost() {
        return this.sceneCost;
    }
    //Sets flipped to true
    public void flip(){
      this.flipped=true;
      }
    //sets flipped to false
    public void unFlip(){
      this.flipped=false;
      }
    //returns flipped
    public boolean isFlipped(){
      return this.flipped;
      }    

    public void resetCard() {//takes all players off of player spots, active is back to false
        for (int i = 0; i < players.size(); i++) {
            PlayerSpot current = this.players.get(i);
            current.resetPlayerSpot();
        }
        this.active = false;
    }

    public ArrayList<PlayerSpot> getRanksOnCard() {//returns array of integers with each one being the rank on a player spot.
        return this.players;
    }

    public void addDesc(String str) {//makes description str
        this.description = description;
    }

    public HashMap<Player, Integer> getPlayers() {//returns players located on sceneCard in HashMap. Rank is the rank+10 to signal the player was on card.
        HashMap<Player, Integer> toReturn = new HashMap<Player, Integer>();
        for (int i = 0; i < players.size(); i++) {
            PlayerSpot current = this.players.get(i);
            ArrayList<Player> onSpot = current.getPlayers();
            int fixedRank = current.getRank() + 10;
            for (int j = 0; j < onSpot.size(); j++) {
                toReturn.put(onSpot.get(j), fixedRank);
            }
        }
        return toReturn;
    }

    //Returns name of card
    public String getName() {
        return this.cardName;
    }

    //Activates card
    public void activate() {
        this.active = true;
    }

    //Deactivates card
    public void deActive() {
        this.active = false;
    }

    //Adds a player spot to a card
    public void addPSpot(PlayerSpot spot) {
        boolean curr = this.players.add(spot);
    }

    //Adds a scene number to a card
    public void addNum(int num) {
        this.sceneNum = num;
    }
}