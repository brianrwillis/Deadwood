import java.util.*;

class PlayerSpotPrinter {
    public PlayerSpotPrinter() {
    }

    //Prints each role attribute of a set with availability
    public void print(ArrayList<PlayerSpot> spots, Player player) {
        for (int i = 0; i < spots.size(); i++) {
            PlayerSpot curr = spots.get(i);
            int rank = curr.getRank();
            String name = curr.getName();
            String line = curr.getLine();
            line = line.replace("\r", "");  //Remove line returns and trailing spaces
            line = line.replace("\n", "");
            line = line.trim();
            boolean okToAdd = curr.checkToAdd(player);
            if (okToAdd == true) {
                System.out.println("AVAILABLE:   " + (i + 1) + ": Spot Name: " + name + ". Needed Rank: " + rank + ". Line: " + line);
            } else {
                System.out.println("UNAVAILABLE: " + (i + 1) + ": Spot Name: " + name + ". Needed Rank: " + rank + ". Line: " + line);
            }
        }
    }
}