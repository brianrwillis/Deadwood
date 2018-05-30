import java.util.*;
public class Controller{
   Board board;
   Players players;
   Display display;
   
   public Controller(Players players, Board board){
      this.players=players;
      this.board=board;
      //this.display=new Display();
      //this.display.launchDisplay();
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
   
   public int getMoveChoice(){//1=move, 2=work
      int i=0;
      //i=display.getMoveChoice();
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
   
   public int getLocation(){//0 to 11. Index of room on board. 
      int i=0;
      //i=display.getLocation();
      return i;
   }    
   
   public void displayRankChoiceError(){
       //display.RankChoiceError();
   }
   
   public int getUpgradeChoice(){//1-6
      int i=0;
      //i=display.getUpgradeChoice();
      return i;
   }
   
   public int getUpgradeType(){//1=money, 2=fame, 3=dont upgrade
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
      
   public int getOnOffCard(){//1=onCard, 2=offCard, 3=not enroll
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
   
   public int getRoleChoice(){//integer of index in the arrayList of strings.
      int i=0;
      //i=display.getRoleChoice();
      return i;
      }
      
   public void displayRoleError(){
      //display.RoleError();
      }
      
   public int getActRehearse(){//1=act, 2=rehearse
      int i=0;
      //i=display.getActRehearse();
      return i;
      }    
      
   public void displayForceWork(){
     // display.ForceWork();
       }
   
   public void sceneWrap(Room room){
      String rName=room.getName();
      for(int i=0; i<11; i++){
         Room curr=this.board.getRoom(i);
         String currName=curr.getName();
         if(rName.equals(currName)){
            //display.clearCard(i);
            }
       }  
      }
      
   public void endDay(){
      //display.endDay();
      }
      
               
}