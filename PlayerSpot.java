import java.util.*;

class PlayerSpot {
    private ArrayList<Player> onCard;
    private int NeededRank;
    private int maxOccupants;
    private String partName;
    private String line;
    public PlayerSpot() {
        this.NeededRank = 0;
        this.maxOccupants = 1;
        this.onCard = new ArrayList<Player>(1);
        this.partName = null;
        this.line = null;
    }

    //Adds player spot to board
    public void AddToPlayerSpot(int rank, String partName) {
        this.NeededRank = rank;
        this.partName = partName;

    }


    public void AddToPlayerSpot(String line) {//makes line into line
        this.line = line;
    }

    public boolean addPlayer(Player player) {//checks if player can be added, adds and returns true if possible
        if (this.onCard.size() < this.maxOccupants && player.getRank() >= this.NeededRank) {
            onCard.add(player);
            return true;
        }
        return false;
    }

    public ArrayList<Player> getPlayers() {//returns the players on the spot
        return this.onCard;
    }

    public void resetPlayerSpot() {//deletes all players from spot
        while (this.onCard.isEmpty() != true) {
            this.onCard.remove(0);
        }
    }

    //Returns needed rank of role
    public int getRank() {
        return this.NeededRank;
    }

    //Returns true if player can be added to role
    public boolean checkToAdd(Player player) {
        if (this.onCard.size() < this.maxOccupants && player.getRank() >= this.NeededRank) {
            return true;
        }
        return false;
    }

    //Returns name of role
    public String getName() {
        return this.partName;
    }

    //Returns line of role
    public String getLine() {
        return this.line;
    }
}