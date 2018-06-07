import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.*;
import javafx.beans.property.SimpleStringProperty;
import java.util.*;

class Room {
    String RoomName;
    private ArrayList<String> adjacent;
    int xVal;
    int yVal;
    ArrayList<Integer> TakeX;
    ArrayList<Integer> TakeY;
    RoomDataStore store;

    public Room() {
        this.RoomName = null;
        this.adjacent = null;
        PlayerSpot forAll = new PlayerSpot();
        forAll.AddToPlayerSpot(8, null);
        this.TakeX=new ArrayList<Integer>();
        this.TakeY=new ArrayList<Integer>();
        this.store=new RoomDataStore();

    }

    //Create room with neighbors
    public Room(String name, int numOfPlayers, ArrayList<String> adjacent) {
        this.RoomName = name;
        this.adjacent = adjacent;
        PlayerSpot forAll = new PlayerSpot();
        forAll.AddToPlayerSpot(8, null);
        //addCardInfo();
        this.TakeX=new ArrayList<Integer>();
        this.TakeY=new ArrayList<Integer>();
        this.store=new RoomDataStore();
        this.store.setName(name);
    }
    public void addTakeX(int x){//adds x coord of take counter location
        this.TakeX.add(x);
    }
    public void addTakeY(int y){//adds y coord of take counter location
        this.TakeY.add(y);
    }
    public ArrayList<Integer> getXTakes(){//returns x coords of take counter
        return this.TakeX;
    }
    public ArrayList<Integer> getYTakes(){//returns y coords of take counter
        return this.TakeY;
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
        this.store.setName(name);
        addCardInfo();
    }
    //    0, Train Station
//    1, Secret Hideout
//    2, Church
//    3, Hotel
//    4, Main Street
//    5, Jail
//    6, General Store
//    7, Ranch
//    8, Bank
//    9, Saloon
//    10, Trailers
//    11, Casting Office
    //Applies the correct xVal and yVal depending on the name of the room.
    //cards are 205 pixels wide, 115 pixels high on a board with width 1200 and height 900.
    public void addCardInfo(){
        if(this.RoomName.equals("Train Station")){
            this.xVal=20;
            this.yVal=70;}
        else if(this.RoomName.equals("Secret Hideout")){
            this.xVal=30;
            this.yVal=730;}
        else if(this.RoomName.equals("Church")){
            this.xVal=620;
            this.yVal=730;}
        else if(this.RoomName.equals("Hotel")){
            this.xVal=970;
            this.yVal=740;}
        else if(this.RoomName.equals("Main Street")){
            this.xVal=970;
            this.yVal=30;}
        else if(this.RoomName.equals("Jail")){
            this.xVal=280;
            this.yVal=30;}
        else if(this.RoomName.equals("General Store")){
            this.xVal=370;
            this.yVal=280;}
        else if(this.RoomName.equals("Ranch")){
            this.xVal=250;
            this.yVal=480;}
        else if(this.RoomName.equals("Bank")){
            this.xVal=620;
            this.yVal=475;}
        else if(this.RoomName.equals("Saloon")){
            this.xVal=630;
            this.yVal=280;}
        else{
            this.xVal=0;
            this.yVal=0;}
    }
    //returns value of x coordinate at top left corner of card location
    public int getXVal(){
        return this.xVal;}

    //returns value of x coordinate at top left corner of card location
    public int getYVal(){
        return this.yVal;}

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

    public RoomDataStore getStore(){
        return this.store;
    }
}