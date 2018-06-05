import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.*;
import javafx.beans.property.SimpleStringProperty;
class Player {
    public String name;
    private int rank;
    private int credit;
    private int fame;
    private int rehearse;
    private boolean onCard;
    private int location;
    private Person person;//holds info for using tableview later.



    public Player(String name) {

        this.name = name;
        this.rank = 1;
        this.fame = 0;
        this.rehearse = 0;
        this.credit = 0;
        this.onCard = false;
        this.location = 10;
        this.person=new Person(this.name, Integer.toString(this.rank),Integer.toString(this.fame), Integer.toString(this.credit), Integer.toString(this.rehearse));
    }

    //Returns score of player
    public int getScore() {
        int score = 5 * this.rank + this.credit + this.fame;
        return score;
    }

    public int getRank() {//return rank
        return this.rank;
    }

    public int getFame() {//return player fame
        return this.fame;
    }

    public int getCredit() {//return player fame
        return this.credit;
    }

    public void setRank(int rank) {//change rank
        this.rank = this.rank + rank;
        this.person.setRank(Integer.toString(this.rank));
    }

    public void setCredit(int credit) {//change credit
        this.credit = credit + this.credit;
        this.person.setCredit(Integer.toString(this.credit));
    }

    public boolean onCard() {//return if player is on a card or not
        return this.onCard;
    }

    public void setFame(int fame) {//change fame
        this.fame = fame + this.fame;
        this.person.setFame(Integer.toString(this.fame));
    }

    public void changeLocation(int newLocation) {//changes player location
        this.location = newLocation;
        this.person.setLocation(Integer.toString(newLocation+1));
    }

    //Returns location of player
    public int getLocation() {
        return this.location;
    }

    //Adds rehearse marker to player
    public void addRehearse() {
        this.rehearse++;
        this.person.setRehearsals(Integer.toString(this.rehearse));
    }

    //Sets rehearse markers of player to 0
    public void resetRehearse() {
        this.rehearse = 0;
        this.person.setRehearsals("0");
    }

    //Returns rehearse markers of player
    public int getRehearse() {
        return this.rehearse;
    }

    //Returns rehearse markers of player
    public String getName() {
        return this.name;
    }

    //Removes player from card
    public void remFromCard() {
        this.onCard = false;
        this.person.setEnrolled("F");
    }

    //Puts player on card
    public void putFromCard() {
        this.onCard = true;
        this.person.setEnrolled("T");
    }
    
    public Person getPerson(){
      return this.person;}

}
