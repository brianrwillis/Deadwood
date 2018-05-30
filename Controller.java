import java.util.*;
public class Controller{
   Board board;
   Players players;
   Display display;
   
   public Controller(Players players, Board board){
      this.players=players;
      this.board=board;
      //this.display=new Display();
   }
   
   public void updatePlayerDisplay(){
      ArrayList<Player> pList=this.players.getPlayers();
      for(int i=0; i<pList.size(); i++){
         Player curr=pList.get(i);
         int currCredit=curr.getCredit();
         int currFame=curr.getFame();
         int currRank=curr.getRank();
         //call Display with i, currCredit, currFame, currRank
      }
   }
   
   public int getTurnChoice(){
      int i=0;
      //i=display.getTurnChoice;
      return i;
   }
      
   public void displayTrailerOfficeError(){
      //display.TrailerOfficeError();
   }
   
   public void displayNotEnrollError(){
      //display.NotEnrolledError();
   }
      
   public void displayCantMoveError(){
      //display.CantMoveError();
   }
  
   public void displayLocationError(){
      //display.LocationError();
   }
   
   public int getLocation(){
      int i=0;
      //i=display.getLocation;
      return i;
   }    
   
   public void displayRankChoiceError(){
       //display.RankChoiceError();
   }
   
   public int getUpgradeChoice(){
      int i=0;
      //i=display.getUpgradeChoice();
      return i;
   }
   
   public int getUpgradeType(){
      int i=0;
      //i=display.getUpgradeType();
      return i;
   }
      
   public void displayMoneyError(int i){//1=money, 2=fame
      if(i==1||i==2){
      //display.MoneyError(i);
      }
   }
      
   public void displayWinner(String winner){
      //display.Winner(winner);
   }
      
   public int getOnOffCard(){
      int i=0;
      //display.getOnOffCard();
      return i;
   }
      
   public void displayOptions(ArrayList<PlayerSpot> spotsOn, ArrayList<PlayerSpot> spotsOff){
      Player player=this.players.getCurrent();
      ArrayList<String> onCard=new ArrayList<String>();
      ArrayList<String> offCard=new ArrayList<String>();
      for (int i = 0; i < spotsOn.size(); i++) {
         PlayerSpot curr = spotsOn.get(i);
         String currStr=new String();
         int rank = curr.getRank();
         String name = curr.getName();
         String line = curr.getLine();
         line = line.replace("\r", "");  //Remove line returns and trailing spaces
         line = line.replace("\n", "");
         line = line.trim();
         boolean okToAdd = curr.checkToAdd(player);
         if (okToAdd == true) {
            currStr.concat("AVAILABLE:   " + (i + 1) + ": Spot Name: " + name + ". Needed Rank: " + rank + ". Line: " + line);
         } 
         else {
            currStr.concat("UNAVAILABLE: " + (i + 1) + ": Spot Name: " + name + ". Needed Rank: " + rank + ". Line: " + line);
         }
         onCard.add(currStr);
      }
      for (int i = 0; i < spotsOff.size(); i++) {
         PlayerSpot curr = spotsOff.get(i);
         String currStr=new String();
         int rank = curr.getRank();
         String name = curr.getName();
         String line = curr.getLine();
         line = line.replace("\r", "");  //Remove line returns and trailing spaces
         line = line.replace("\n", "");
         line = line.trim();
         boolean okToAdd = curr.checkToAdd(player);
         if (okToAdd == true) {
            currStr.concat("AVAILABLE:   " + (i + 1) + ": Spot Name: " + name + ". Needed Rank: " + rank + ". Line: " + line);
         } 
         else {
            currStr.concat("UNAVAILABLE: " + (i + 1) + ": Spot Name: " + name + ". Needed Rank: " + rank + ". Line: " + line);
         }
         offCard.add(currStr);
      }
      //display.showSpotChoice(onCard, offCard);
   }
   
   public int getRoleChoice(){
      int i=0;
      //i=display.getRoleChoice();
      return i;
      }
      
   public void displayRoleError(){
      //display.RoleError();
      }
      
   public int getActRehearse(){
      int i=0;
      //i=display.getActRehearse();
      return i;
      }    
}