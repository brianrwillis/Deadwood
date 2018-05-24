import java.util.*;

class Room {
    String RoomName;
    private ArrayList<String> adjacent;

    public Room() {
        this.RoomName = null;
        this.adjacent = null;
        PlayerSpot forAll = new PlayerSpot();
        forAll.AddToPlayerSpot(8, null);
    }

    //Create room with neighbors
    public Room(String name, int numOfPlayers, ArrayList<String> adjacent) {
        this.RoomName = name;
        this.adjacent = adjacent;
        PlayerSpot forAll = new PlayerSpot();
        forAll.AddToPlayerSpot(8, null);
    }

    public String getName() {//returns name of room
        return this.RoomName;
    }

    //Adds a neighbor to a room
    public void addAdj(ArrayList<String> adj) {
        this.adjacent = adj;
    }

    //Resetting a room handled by Set.java
    public void reset() {

    }

    //Change a room's name
    public void ChangeRoom(String name) {
        this.RoomName = name;
    }

    //Getting a card handled by Set.java
    public SceneCard getCard() {
        return null;
    }

    //Getting ranks off the card handled by Set.java
    public ArrayList<PlayerSpot> getRanksOffCard() {
        return null;
    }

    //Returns true if room is adjacent
    public boolean isAdjacent(String desired) {
        for (int i = 0; i < this.adjacent.size(); i++) {
            if (this.adjacent.get(i).equals(desired)) {
                return true;
            }
        }
        return false;
    }

    //Getting players handled by Set.java
    public HashMap<Player, Integer> getPlayers() {
        return null;
    }

    //Adjusting takes handled by Set.java
    public void adjustTakes() {//once a take is complete

    }

    //Getting remaining takes handled by Set.java
    public int getRemainingTakes() {//returns takes remaining
        return 1;

    }

    //Adding to card handled by Set.java
    public void addCard(SceneCard card) {

    }
}