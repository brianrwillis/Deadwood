import java.util.*;

class Work {
    public Work() {
    }

    public boolean workRole(Players players, Board board) {//returns true if acting results in no more takes left on scene card.
        Player currentP = players.getCurrent();
        int loc = currentP.getLocation();
        Room location = board.getRoom(loc);
        SceneCard card = location.getCard();
        int budget = card.getCost();
        ArrayList<PlayerSpot> offCard = location.getRanksOffCard();
        boolean on = true;
        for (int i = 0; i < offCard.size(); i++) {
            PlayerSpot currPS = offCard.get(i);
            ArrayList<Player> PList = currPS.getPlayers();
            for (int j = 0; j < PList.size(); j++) {
                Player person = PList.get(j);
                if (person == currentP) {
                    on = false;
                }
            }
        }
        //Roll and see if player advances the scene
        int roll = RandomNum(1, 6);
        int total = roll + currentP.getRehearse();
        //System.out.println("You need to roll a " + budget + " to advance a scene.");
        //System.out.println("You rolled a " + roll + "! With your rehearsal tokens you have " + total + ".");
        if (total >= budget) {
            location.adjustTakes();
            //System.out.println("You have successfully advanced the scene!");
            if (on) {
                currentP.setFame(2);
            }
            if (!on) {
                currentP.setFame(1);
                currentP.setCredit(1);
            }
        } else {
            //System.out.println("You did not roll high enough to advance the scene.");
            if (!on) {
                currentP.setCredit(1);
            }
        }

        return location.getRemainingTakes() == 0;
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