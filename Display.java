import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.input.*;
import java.lang.*;
import javafx.event.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.*;
import javafx.animation.PauseTransition;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Display {
   private enum PState {                               //State of the program
      GET_PLAYER_CNT,
      GET_PLAYER_NAMES,
      MOVE,
      GET_MOVE_LOC,
      GET_UPGRADE_NUM,
      UPGRADE_PAY,
      GET_ROLE_TYPE,
      GET_ROLE,
      WORK,
      ACT,
      REHEARSE
   }

   //Button instances
   private static Button moveButton;
   private static Button workButton;
   private static Button doNothingButton;
   private static Button actButton;
   private static Button rehearseButton;
   private static Button onCardButton;
   private static Button offCardButton;
   private static Button noRoleButton;
   private static Button moneyButton;
   private static Button fameButton;
   private static Button noUpgradeButton;

   //Table instances
   private static TableView stats;
   private static TableView onCard;
   private static TableView offCard;
   private static TableView currPlayerTable;
   private static TableView shotsTable;

   //Other JavaFX object instances
   private static Label output;
   private static TextField input;
   private static Scene scene;

   //Display-restricted variables
   private static String inputText;
   private static int upgradeSel;
   private static int cardChoiceSel;
   private static ArrayList<PlayerSpot> spotsOn;
   private static ArrayList<PlayerSpot> spotsOff;
   private static int spotsSize;
   private static int newRank;
   private static int daysLeft;
   private static int playerCnt;
   private static String[] playerNames;
   private static int playerNamesCnt = 0;

   //Other class instances
   private static Players players = null;
   private static Players playersOrdered = null;
   private static Deck deck = null;
   private static Board board = null;
   private static Moderator moderator = null;
   private static Calculator calculator = null;

   private Thread gameThread;                          //Thread for playing the game

   private PState pState = PState.GET_PLAYER_CNT;      //Starting state of the program


   public Display() {
   }

   public void displayInit() throws InterruptedException {
      final int BOARD_HEIGHT = 900;
      final int WINDOW_WIDTH = 1800;
      final int WINDOW_HEIGHT = 1000;
      final int LOC_REF_HEIGHT = 238;
      final int LOC_REF_WIDTH = 155;
      final int BUTT_HEIGHT = 30;
   
      Group root = new Group();
   
      //~~~~~~~~~~~~~~~~~~~~~~~~~~Game Task~~~~~~~~~~~~~~~~~~~~~~~~~~//
      boolean gameOngoing = true;
      Task gameTask =
            new Task<Void>() {
               @Override
               public Void call() throws Exception {
                  while (gameOngoing) {
                     synchronized (gameThread) {
                        gameThread.wait();                   //Wait for next user input via text entry or buttons
                     }
                     Platform.runLater(
                            new Runnable() {
                               @Override
                               public void run() {
                                  Player currPlayer;
                                  int currLoc;
                                  Room currSet;
                                  SceneCard currCard;
                                  switch (pState) {
                                     case GET_PLAYER_CNT:    //Get number of players
                                        if (validNumInput(inputText, 2, 8)) {
                                           playerCnt = Integer.parseInt(inputText);
                                        
                                           players = new Players(playerCnt);     //Make players instance
                                        
                                           try {                                 //Create deck and board instances
                                              deck = new Deck();
                                              board = new Board();
                                           } 
                                           catch (ParserConfigurationException ex) {
                                              System.out.println("Error parsing XML files.");
                                           }
                                        
                                        //Create moderator and calculator instances
                                           moderator = new Moderator(players, deck, board);
                                           calculator = new Calculator(players);
                                        
                                           playerNames = new String[playerCnt];  //Create holder for player names
                                           output.setText("Player 1's name?");
                                           pState = PState.GET_PLAYER_NAMES;
                                        }
                                        break;
                                  
                                     case GET_PLAYER_NAMES:  //Get names from players
                                        playerNames[playerNamesCnt] = inputText;
                                        playerNamesCnt++;
                                        if (playerNamesCnt < playerCnt) {
                                           output.setText("Player " + (playerNamesCnt + 1) + "'s name?");
                                        } 
                                        else {                                  //Collected all player names, add all to players list
                                           players.addPlayers(playerNames);
                                           players.shuffle();                    //Randomize player order
                                           playersOrdered = players;             //Store starting order of players for calculating winner later
                                        
                                           int[] rules = calculator.calcRules(playerCnt);    //Calculate starting parameters
                                           daysLeft = rules[2];                  //Set remaining days
                                        
                                        //Set player starting attributes
                                           ArrayList<Player> playersList = players.getPlayers();
                                           for (int i = 0; i < playerCnt; i++) {
                                              moderator.giveFunds(playersList.get(i), 0, rules[1]);
                                              moderator.increaseRank(playersList.get(i), rules[0] - 1);
                                           }
                                           ObservableList<Person> data1 = FXCollections.observableArrayList();
                                           stats = new TableView<Person>();
                                           stats.setEditable(true);
                                           TableColumn PNameCol = new TableColumn("");
                                           Label NameL = new Label("Name");
                                           VBox NameBox = new VBox(NameL);
                                           NameBox.setRotate(-90);
                                           Group NG = new Group(NameBox);
                                           PNameCol.setGraphic(NG);
                                           PNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
                                           TableColumn PCreditCol = new TableColumn("");
                                           PCreditCol.setCellValueFactory(new PropertyValueFactory<Person, String>("credit"));
                                           Label CreditL = new Label("Credit");
                                           VBox CreditBox = new VBox(CreditL);
                                           CreditBox.setRotate(-90);
                                           Group CG = new Group(CreditBox);
                                           PCreditCol.setGraphic(CG);
                                           TableColumn PFameCol = new TableColumn("");
                                           PFameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("fame"));
                                           Label FameL = new Label("Fame");
                                           VBox FameBox = new VBox(FameL);
                                           FameBox.setRotate(-90);
                                           Group FG = new Group(FameBox);
                                           PFameCol.setGraphic(FG);
                                           TableColumn PRankCol = new TableColumn("");
                                           PRankCol.setCellValueFactory(new PropertyValueFactory<Person, String>("rank"));
                                           Label RankL = new Label("Rank");
                                           VBox RankBox = new VBox(RankL);
                                           RankBox.setRotate(-90);
                                           Group RG = new Group(RankBox);
                                           PRankCol.setGraphic(RG);
                                           TableColumn PRehearCol = new TableColumn("");
                                           PRehearCol.setCellValueFactory(new PropertyValueFactory<Person, String>("rehearsals"));
                                           Label RehearseL = new Label("Rehearsals");
                                           VBox RehearseBox = new VBox(RehearseL);
                                           RehearseBox.setRotate(-90);
                                           Group ReG = new Group(RehearseBox);
                                           PRehearCol.setGraphic(ReG);
                                           TableColumn PLocCol = new TableColumn("");
                                           PLocCol.setCellValueFactory(new PropertyValueFactory<Person, String>("location"));
                                           Label LocL = new Label("Location");
                                           VBox LocBox = new VBox(LocL);
                                           LocBox.setRotate(-90);
                                           Group LG = new Group(LocBox);
                                           PLocCol.setGraphic(LG);
                                           TableColumn PEnrCol = new TableColumn("");
                                           PEnrCol.setCellValueFactory(new PropertyValueFactory<Person, String>("enrolled"));
                                           Label EnrL = new Label("Enrolled?");
                                           VBox EnrBox = new VBox(EnrL);
                                           EnrBox.setRotate(-90);
                                           Group EG = new Group(EnrBox);
                                           PEnrCol.setGraphic(EG);
                                           PNameCol.setMinWidth(100);
                                           stats.getColumns().addAll(PNameCol, PCreditCol, PFameCol, PRankCol, PRehearCol, PLocCol, PEnrCol);
                                           stats.setLayoutX(0);
                                           stats.setLayoutY(0);
                                           stats.setMinWidth(100);
                                           stats.setMinHeight(100);
                                           stats.setMaxWidth(300);
                                           for (int i = 0; i < playersList.size(); i++) {
                                              data1.add(playersList.get(i).getPerson());
                                           }
                                           stats.setVisible(true);
                                           stats.setItems(data1);
                                           root.getChildren().add(stats);
                                        
                                           shotsTable = new TableView<RoomDataStore>();
                                           ObservableList<RoomDataStore> shotsData = FXCollections.observableArrayList();
                                           TableColumn RoomNameCol = new TableColumn("Room Name");
                                           RoomNameCol.setCellValueFactory(new PropertyValueFactory<RoomDataStore, String>("name"));
                                           TableColumn RoomShotsCol = new TableColumn("Takes");
                                           RoomShotsCol.setCellValueFactory(new PropertyValueFactory<RoomDataStore, String>("shots"));
                                           for (int i = 0; i < 10; i++) {
                                              RoomDataStore currDatStore = board.getRoom(i).getStore();
                                              shotsData.addAll(currDatStore);
                                           }
                                           shotsTable.getColumns().addAll(RoomNameCol, RoomShotsCol);
                                           shotsTable.setItems(shotsData);
                                           shotsTable.setLayoutX(1625);
                                           shotsTable.setLayoutY(0);
                                           shotsTable.setVisible(true);
                                           shotsTable.setMaxHeight(266);
                                           shotsTable.setMaxWidth(147);
                                           root.getChildren().add(shotsTable);
                                        
                                           currPlayerTable = new TableView<CurrPlayer>();
                                           CurrPlayer currPlayerOne = players.getCurrPlayer();
                                           ObservableList<CurrPlayer> CPList = FXCollections.observableArrayList();
                                           CPList.addAll(currPlayerOne);
                                           TableColumn currPCol = new TableColumn("Current Player:");
                                           currPCol.setCellValueFactory(new PropertyValueFactory<CurrPlayer, String>("currPlayer"));
                                           currPlayerTable.setLayoutX(0);
                                           currPlayerTable.setLayoutY(800);
                                           currPlayerTable.setMaxWidth(201);
                                           currPlayerTable.setMinWidth(200);
                                           currPlayerTable.setMaxHeight(52);
                                           currPlayerTable.setMinHeight(51);
                                           currPCol.setMinWidth(198);
                                           currPCol.setMaxWidth(199);
                                           currPlayerTable.setVisible(true);
                                           currPlayerTable.setItems(CPList);
                                           currPlayerTable.getColumns().addAll(currPCol);
                                        
                                           root.getChildren().add(currPlayerTable);
                                        
                                        //Distribute cards for Day 1
                                           for (int i = 0; i < 10; i++) {
                                              Room currRoom = board.getRoom(i);
                                              currCard = deck.getTopofOrder();
                                              currRoom.addCard(currCard);
                                           }
                                        
                                        //Prompt for user action for Day 1
                                           output.setText(players.getCurrent().getName() + "'s turn: Please select action.");
                                           moveButton.setVisible(true);                        //Activate buttons
                                           workButton.setVisible(true);
                                           doNothingButton.setVisible(true);
                                        }
                                        break;
                                  
                                     case MOVE:                                                  //User clicked Move button
                                     //Test action validity
                                        currPlayer = players.getCurrent();
                                        if (currPlayer.onCard()) {
                                           output.setText("Cannot move while enrolled.");
                                           output.setStyle("-fx-border-color: red;");
                                        } 
                                        else {                                                //Valid action
                                           output.setText("Please input location.");
                                           output.setStyle("-fx-border-color: green;");
                                           moveButton.setVisible(false);                       //Deactivate buttons
                                           workButton.setVisible(false);
                                           doNothingButton.setVisible(false);
                                           pState = PState.GET_MOVE_LOC;
                                        }
                                        break;
                                  
                                     case GET_MOVE_LOC:      //Get user move location
                                     //Test input bounds and move validity
                                        if (validNumInput(inputText, 1, 12)) {
                                           int moveSel = Integer.parseInt(inputText);
                                           currPlayer = players.getCurrent();
                                           currLoc = currPlayer.getLocation();
                                           boolean moveValid = board.getRoom(currLoc).isAdjacent(board.getRoom(moveSel - 1).getName());
                                           if (!moveValid) {
                                              output.setText("Choose an adjacent location.");
                                              output.setStyle("-fx-border-color: red;");
                                           } 
                                           else {                                                                //Valid location
                                              output.setStyle("-fx-border-color: green;");
                                              players.getCurrent().changeLocation(moveSel - 1);                       //Update user's location
                                              players.getCurrent().resetRehearse();                               //Reset rehearse markers
                                              stats.refresh();
                                              currPlayer = players.getCurrent();
                                              currLoc = currPlayer.getLocation();
                                              currSet = board.getRoom(currLoc);
                                              currCard = currSet.getCard();
                                              if (currLoc == 10) {                                                //Trailers
                                              //Do nothing
                                                 endTurn();                                                      //End user's turn
                                              } 
                                              else if ((currLoc != 11) && (!currCard.isActive())){              //Scene wrapped previously
                                              //Do nothing
                                                 endTurn();                                                      //End user's turn
                                              } 
                                              else if (currLoc == 11) {                                         //Casting Office
                                                 output.setText("Upgrade how many ranks?");
                                                 pState = PState.GET_UPGRADE_NUM;
                                              } 
                                              else {                                                            //Room with set
                                                 output.setText("Take a role on card or off card?");
                                                 onCardButton.setVisible(true);                                  //Activate buttons
                                                 offCardButton.setVisible(true);
                                                 noRoleButton.setVisible(true);
                                                 pState = PState.GET_ROLE_TYPE;
                                                 CardTableMaker tableMaker = new CardTableMaker();
                                                 onCard = tableMaker.getOnCard(board, players.getCurrent().getLocation());
                                                 offCard = tableMaker.getOffCard(board, players.getCurrent().getLocation());
                                                 offCard.setLayoutX(0);
                                                 offCard.setLayoutY(600);
                                                 offCard.setMaxHeight(200);
                                                 offCard.setVisible(true);
                                                 onCard.setLayoutX(0);
                                                 onCard.setLayoutY(400);
                                                 onCard.setMaxHeight(200);
                                                 onCard.setVisible(true);
                                                 root.getChildren().add(onCard);
                                                 root.getChildren().add(offCard);
                                              
                                              }
                                           }
                                        }
                                        break;
                                  
                                     case GET_ROLE_TYPE:                         //User clicked role type
                                        onCardButton.setVisible(false);         //Deactivate buttons
                                        offCardButton.setVisible(false);
                                        noRoleButton.setVisible(false);
                                        if (cardChoiceSel == 3||!board.getRoom(players.getCurrent().getLocation()).getCard().isActive()) {
                                           output.setStyle("-fx-border-color: green;");
                                           endTurn();                          //End user's turn if no role taken
                                           onCard.setVisible(false);
                                           offCard.setVisible(false);
                                        } 
                                        else {
                                           currPlayer = players.getCurrent();
                                           currLoc = currPlayer.getLocation();
                                           currSet = board.getRoom(currLoc);
                                           SceneCard currentCard = currSet.getCard();
                                           spotsOn = currentCard.getRanksOnCard();
                                           spotsOff = currSet.getRanksOffCard();
                                           if (cardChoiceSel == 1) {          //Get role selection range for user
                                              spotsSize = spotsOn.size();
                                           } 
                                           else {
                                              spotsSize = spotsOff.size();
                                           }
                                           output.setText("Choose role to take (1-" + spotsSize + ")");
                                           output.setStyle("-fx-border-color: green;");
                                           pState = PState.GET_ROLE;
                                        }
                                        break;
                                  
                                     case GET_ROLE:                           //User entered role choice
                                     //Test role choice validity
                                        if (validNumInput(inputText, 1, spotsSize)) {
                                           int roleChoiceSel = Integer.parseInt(inputText);
                                           boolean workSucc;
                                        
                                        //Add user to role
                                           if (cardChoiceSel == 1) {
                                              PlayerSpot toAddTo = spotsOn.get(roleChoiceSel - 1);
                                              workSucc = toAddTo.addPlayer(players.getCurrent());
                                              stats.refresh();
                                           } 
                                           else {
                                              PlayerSpot toAddTo = spotsOff.get(roleChoiceSel - 1);
                                              workSucc = toAddTo.addPlayer(players.getCurrent());
                                              stats.refresh();
                                           }
                                        
                                           if (workSucc) {
                                              onCard.setVisible(false);
                                              offCard.setVisible(false);
                                              players.getCurrent().putFromCard();
                                           
                                              endTurn();                                  //End user's turn
                                           } 
                                           else {
                                              output.setText("Role unavailable.");
                                              output.setStyle("-fx-border-color: red;");
                                              onCard.setVisible(true);
                                              offCard.setVisible(true);
                                              onCardButton.setVisible(true);              //Activate buttons
                                              offCardButton.setVisible(true);
                                              noRoleButton.setVisible(true);
                                              pState = PState.GET_ROLE_TYPE;
                                           }
                                        }
                                        break;
                                  
                                     case GET_UPGRADE_NUM:                                   //Get rank upgrade amount
                                     //Test input bounds and upgrade amount validity
                                        if (validNumInput(inputText, 1, 5)) {
                                           currPlayer = players.getCurrent();
                                           int upgradeNumSel = Integer.parseInt(inputText);
                                           if ((currPlayer.getRank() + upgradeNumSel) > 6) {
                                              output.setText("You can only upgrade to a max rank of 6.");
                                              output.setStyle("-fx-border-color: red;");
                                           } 
                                           else {                                                //Valid upgrade amount
                                              output.setText("Upgrade with money or fame?");
                                              output.setStyle("-fx-border-color: green;");
                                              newRank = currPlayer.getRank() + upgradeNumSel;     //Store intended upgraded rank
                                           
                                              moneyButton.setVisible(true);                       //Activate buttons
                                              fameButton.setVisible(true);
                                              noUpgradeButton.setVisible(true);
                                              pState = PState.UPGRADE_PAY;
                                           }
                                        }
                                        break;
                                  
                                     case UPGRADE_PAY:                                           //User clicked payment type
                                        moneyButton.setVisible(false);                          //Deactivate buttons
                                        fameButton.setVisible(false);
                                        noUpgradeButton.setVisible(false);
                                     
                                     //Test payment type validity
                                        if (upgradeSel == 1) {              //Money
                                           int moneyCost = 0;
                                           switch (newRank) {
                                              case (2):
                                                 moneyCost = 4;
                                                 break;
                                              case (3):
                                                 moneyCost = 10;
                                                 break;
                                              case (4):
                                                 moneyCost = 18;
                                                 break;
                                              case (5):
                                                 moneyCost = 28;
                                                 break;
                                              case (6):
                                                 moneyCost = 40;
                                                 break;
                                              default:
                                                 break;
                                           }
                                           if (players.getCurrent().getCredit() < moneyCost) {
                                              output.setText("Not enough money to perform upgrade. Upgrade how many ranks?");
                                              output.setStyle("-fx-border-color: red;");
                                              pState = PState.GET_UPGRADE_NUM;
                                           } 
                                           else {
                                           //Upgrade
                                              moderator.giveFunds(players.getCurrent(), -1 * moneyCost, 0);
                                              moderator.increaseRank(players.getCurrent(), upgradeSel);
                                              stats.refresh();
                                              endTurn();                  //End user's turn
                                           }
                                        } 
                                        else if (upgradeSel == 2) {       //Fame
                                           int fameCost = 0;
                                           switch (newRank) {
                                              case (2):
                                                 fameCost = 5;
                                                 break;
                                              case (3):
                                                 fameCost = 10;
                                                 break;
                                              case (4):
                                                 fameCost = 15;
                                                 break;
                                              case (5):
                                                 fameCost = 20;
                                                 break;
                                              case (6):
                                                 fameCost = 25;
                                                 break;
                                              default:
                                                 break;
                                           }
                                           if (players.getCurrent().getFame() < fameCost) {
                                              output.setText("Not enough fame to perform upgrade. Upgrade how many ranks?");
                                              output.setStyle("-fx-border-color: red;");
                                              pState = PState.GET_UPGRADE_NUM;
                                           } 
                                           else {
                                              moderator.giveFunds(players.getCurrent(), 0, -1 * fameCost);
                                              moderator.increaseRank(players.getCurrent(), upgradeSel);
                                              stats.refresh();
                                              endTurn();                      //End user's turn
                                           }
                                        } 
                                        else {                                //Don't upgrade
                                           endTurn();                          //End user's turn
                                        }
                                        break;
                                  
                                     case WORK:                                  //User clicked Work button
                                     //Test action validity
                                        currPlayer = players.getCurrent();
                                        currLoc = currPlayer.getLocation();
                                        currSet = board.getRoom(currLoc);
                                        currCard = currSet.getCard();
                                        if ((currLoc == 10) || (currLoc == 11)) {//makes sure player isnt at casting or trailers. 
                                           output.setText("Cannot work while in the Trailers or Casting Office.");
                                           output.setStyle("-fx-border-color: red;");
                                        } 
                                        else if ((!currPlayer.onCard())) {//makes sure player is enrolled.
                                           output.setText("Cannot work while not enrolled.");
                                           output.setStyle("-fx-border-color: red;");
                                        } 
                                        else {                                                    //Valid action
                                           moveButton.setVisible(false);                           //Deactivate buttons
                                           workButton.setVisible(false);
                                           doNothingButton.setVisible(false);
                                           output.setText("Act or rehearse?");
                                           output.setStyle("-fx-border-color: green;");
                                           actButton.setVisible(true);
                                           rehearseButton.setVisible(true);
                                        }
                                        break;
                                  
                                     case ACT:               //User clicked Act button
                                        actButton.setVisible(false);
                                        rehearseButton.setVisible(false);
                                        output.setStyle("-fx-border-color: green;");
                                        currPlayer = players.getCurrent();
                                        currLoc = currPlayer.getLocation();
                                        Room currSSet = board.getRoom(currLoc);
                                        Work currWork = new Work();
                                     
                                        boolean complete = currWork.workRole(players, board);       //Perform work
                                        if (complete) {
                                           currSet = board.getRoom(currLoc);
                                           currCard = currSet.getCard();
                                        
                                           currCard.resetCard();
                                           moderator.advanceScene(currSet);                        //Advance scene
                                           ArrayList<SceneCard> active = deck.getActiveCard();
                                           for (int i = 0; i < active.size(); i++) {
                                              SceneCard iCard = active.get(i);
                                           
                                              if (!iCard.isActive()) {                            //Remove card from set
                                                 active.remove(i);
                                              }
                                           }
                                        
                                           currPlayer.remFromCard();
                                           currSet.reset();
                                           while(currSet.getRemainingTakes()!=0){//forces takes to be 0 as reset will put takes back to original
                                              currSet.adjustTakes();
                                           }
                                           currCard.resetCard();
                                           active.trimToSize();//makes sure finished card was taken out of active card list
                                        
                                           if (active.size() == 9) {                               //Advance day
                                              daysLeft--;
                                              if (daysLeft > 1) {
                                                 output.setText("Day Completed! Days Remaining: " + daysLeft);
                                                 board.reset();
                                                 players.reset();
                                                 deck.reset();
                                                 for(int i=0; i<10; i++){
                                                   Room resetRoom=board.getRoom(i);
                                                   resetRoom.addCard(deck.getTopofOrder());
                                                   }
                                                 PauseTransition pauseDC = new PauseTransition(Duration.seconds(1));
                                              
                                              
                                                 pauseDC.setOnFinished(event ->
                                                    endTurn()
                                                    );
                                                 pauseDC.play();
                                              
                                              } 
                                              else {                                             //No days left, end the game
                                                 Player winner = calculator.calcWinner(playersOrdered);
                                                 output.setText("Winner: " + winner.getName() + "! Thank you for playing!");
                                                 stats.refresh();
                                              //Game finished here
                                              }
                                           } 
                                           else {
                                              output.setText("Scene Wrapped! Cards remaining on the board: " + active.size());
                                              PauseTransition pauseSW = new PauseTransition(Duration.seconds(1));
                                              pauseSW.setOnFinished(event ->
                                                 endTurn()
                                                 );
                                              pauseSW.play();
                                              stats.refresh();
                                           }
                                        } 
                                        else {
                                           output.setText("Work complete! Takes Remaining: " + currSSet.getRemainingTakes());
                                           PauseTransition pauseWC = new PauseTransition(Duration.seconds(1));
                                           pauseWC.setOnFinished(event ->
                                              endTurn()
                                              );
                                           pauseWC.play();
                                           stats.refresh();
                                        }
                                        break;
                                  
                                     case REHEARSE:                                              //User clicked Rehearse button
                                     //Check to see if user has max rehearse markers
                                        currPlayer = players.getCurrent();
                                        if (currPlayer.getRehearse() + currPlayer.getRank() >= 6) {
                                           output.setText("You have enough rehearse markers to guarantee success.");
                                           output.setStyle("-fx-border-color: red;");
                                           rehearseButton.setVisible(false);
                                        } 
                                        else {                                                //Valid input
                                           actButton.setVisible(false);                        //Deactivate buttons
                                           rehearseButton.setVisible(false);
                                        
                                           players.getCurrent().addRehearse();                 //Give rehearse marker
                                           output.setText("Rehearsal successful!");
                                           PauseTransition pauseR = new PauseTransition(Duration.seconds(1));
                                           pauseR.setOnFinished(event -> endTurn());
                                           pauseR.play();
                                           stats.refresh();
                                        }
                                        break;
                                  
                                     default:
                                        break;
                                  }
                               }
                            });
                  }
                  return null;
               }
            };
   
      //~~~~~~~~~Images~~~~~~~~~//
      //Board
      ImageView boardImageView = new ImageView();
      //Choose image
      Image fullBoard = new Image(Deadwood.class.getResourceAsStream("board.jpg"));
      //Set size
      boardImageView.setPreserveRatio(true);
      boardImageView.setFitHeight(BOARD_HEIGHT);
      boardImageView.setImage(fullBoard);
      //Center
      boardImageView.setX(WINDOW_WIDTH / 2 - boardImageView.getBoundsInParent().getWidth() / 2);
      boardImageView.setY(WINDOW_HEIGHT / 2 - boardImageView.getBoundsInParent().getHeight() / 2);
   
   
      //~~~~~~~~~Labels~~~~~~~~~//
      //Create main output label
      output = new Label();
      output.setLayoutX(WINDOW_WIDTH / 2 - boardImageView.getBoundsInParent().getWidth() / 2);
      output.setLayoutY(WINDOW_HEIGHT / 2 + boardImageView.getBoundsInParent().getHeight() / 2);
      output.setMinWidth(boardImageView.getBoundsInParent().getWidth() / 2);
      output.setMinHeight(30);
      output.setStyle("-fx-border-color: green; -fx-font: 14 arial;");
   
      //Create location numerical reference label
      Label locRef = new Label();
      locRef.setLayoutX(WINDOW_WIDTH / 2 + boardImageView.getBoundsInParent().getWidth() / 2);
      locRef.setLayoutY(WINDOW_HEIGHT / 2 + boardImageView.getBoundsInParent().getHeight() / 2 - LOC_REF_HEIGHT);
      locRef.setMinWidth(LOC_REF_WIDTH);
      locRef.setStyle("-fx-border-color: orange; -fx-font: 14 arial;");
      locRef.setText("[Location Reference]\r\n\n" +
                     "1: Train Station\r\n" +
                     "2: Secret Hideout\r\n" +
                     "3: Church\r\n" +
                     "4: Hotel\r\n" +
                     "5: Main Street\r\n" +
                     "6: Jail\r\n" +
                     "7: General Store\r\n" +
                     "8: Ranch\r\n" +
                     "9: Bank\r\n" +
                     "10: Saloon\r\n" +
                     "11: Trailers\r\n" +
                     "12: Casting Office");
   
   
      //~~~~~~~~~Text Input~~~~~~~~~//
      //Create main input
      input = new TextField();
      input.setLayoutX(WINDOW_WIDTH / 2);
      input.setLayoutY(WINDOW_HEIGHT / 2 + boardImageView.getBoundsInParent().getHeight() / 2);
      input.setMinWidth(boardImageView.getBoundsInParent().getWidth() / 2);
      input.setMinHeight(30);
      input.setStyle("-fx-border-color: blue; -fx-font: 14 arial;");
      input.setPromptText("Enter inputs here.");
   
      //Create input action
      input.setOnAction(
            new EventHandler<ActionEvent>() {
               @Override
               public void handle(ActionEvent e) {
                  if ((input.getText() != null && !input.getText().isEmpty())) {
                     inputText = input.getText();
                     input.clear();                              //Clear input text box
                  
                     switch (pState) {                           //Different actions based off program state
                        case GET_PLAYER_CNT:                    //States that use the input terminal
                        case GET_PLAYER_NAMES:
                        case GET_MOVE_LOC:
                        case GET_UPGRADE_NUM:
                        case GET_ROLE:
                           synchronized (gameThread) {         //Alert gameThread that input has finished parsing
                              gameThread.notify();
                           }
                           break;
                     
                        default:                                //States that do not require text input
                           break;                              //Do nothing if user enters text
                     }
                  }
               }
            });
   
   
      //~~~~~~~~~~~~~~~~~~~~~~~~~~Begin Button Making~~~~~~~~~~~~~~~~~~~~~~~~~~//
      //~~~~~~~~~~Initialize buttons~~~~~~~~~~//
      //Move button
      moveButton = new Button();
      moveButton.setMinSize(0, BUTT_HEIGHT);
      moveButton.setStyle("-fx-font: 16 arial;");
      moveButton.setText("Move");
      //Position
      moveButton.setLayoutX(WINDOW_WIDTH / 2 + boardImageView.getBoundsInParent().getWidth() / 2 + 10);
      moveButton.setLayoutY(WINDOW_HEIGHT / 2 - boardImageView.getBoundsInParent().getHeight() / 2);
      //Initialize as invisible
      moveButton.setVisible(false);
   
      //Work button
      workButton = new Button();
      workButton.setMinSize(0, BUTT_HEIGHT);
      workButton.setStyle("-fx-font: 16 arial;");
      workButton.setText("Work");
      //Position
      workButton.setLayoutX(WINDOW_WIDTH / 2 + boardImageView.getBoundsInParent().getWidth() / 2 + 10);
      workButton.setLayoutY(WINDOW_HEIGHT / 2 - boardImageView.getBoundsInParent().getHeight() / 2 + 40);
      workButton.setVisible(false);
   
      //Do Nothing button
      doNothingButton = new Button();
      doNothingButton.setMinSize(0, BUTT_HEIGHT);
      doNothingButton.setStyle("-fx-font: 16 arial;");
      doNothingButton.setText("Do Nothing");
      //Position
      doNothingButton.setLayoutX(WINDOW_WIDTH / 2 + boardImageView.getBoundsInParent().getWidth() / 2 + 10);
      doNothingButton.setLayoutY(WINDOW_HEIGHT / 2 - boardImageView.getBoundsInParent().getHeight() / 2 + 80);
      doNothingButton.setVisible(false);
   
      //Act button
      actButton = new Button();
      actButton.setMinSize(0, BUTT_HEIGHT);
      actButton.setStyle("-fx-font: 16 arial;");
      actButton.setText("Act");
      //Position
      actButton.setLayoutX(WINDOW_WIDTH / 2 + boardImageView.getBoundsInParent().getWidth() / 2 + 10);
      actButton.setLayoutY(WINDOW_HEIGHT / 2 - boardImageView.getBoundsInParent().getHeight() / 2 + 120);
      actButton.setVisible(false);
   
      //Rehearse button
      rehearseButton = new Button();
      rehearseButton.setMinSize(0, BUTT_HEIGHT);
      rehearseButton.setStyle("-fx-font: 16 arial;");
      rehearseButton.setText("Rehearse");
      //Position
      rehearseButton.setLayoutX(WINDOW_WIDTH / 2 + boardImageView.getBoundsInParent().getWidth() / 2 + 10);
      rehearseButton.setLayoutY(WINDOW_HEIGHT / 2 - boardImageView.getBoundsInParent().getHeight() / 2 + 160);
      //Initialize as invisible
      rehearseButton.setVisible(false);
   
      //On Card button
      onCardButton = new Button();
      onCardButton.setMinSize(0, BUTT_HEIGHT);
      onCardButton.setStyle("-fx-font: 16 arial;");
      onCardButton.setText("On Card");
      //Position
      onCardButton.setLayoutX(WINDOW_WIDTH / 2 + boardImageView.getBoundsInParent().getWidth() / 2 + 10);
      onCardButton.setLayoutY(WINDOW_HEIGHT / 2 - boardImageView.getBoundsInParent().getHeight() / 2 + 200);
      //Initialize as invisible
      onCardButton.setVisible(false);
   
      //Off Card button
      offCardButton = new Button();
      offCardButton.setMinSize(0, BUTT_HEIGHT);
      offCardButton.setStyle("-fx-font: 16 arial;");
      offCardButton.setText("Off Card");
      //Position
      offCardButton.setLayoutX(WINDOW_WIDTH / 2 + boardImageView.getBoundsInParent().getWidth() / 2 + 10);
      offCardButton.setLayoutY(WINDOW_HEIGHT / 2 - boardImageView.getBoundsInParent().getHeight() / 2 + 240);
      //Initialize as invisible
      offCardButton.setVisible(false);
   
      //No Role button
      noRoleButton = new Button();
      noRoleButton.setMinSize(0, BUTT_HEIGHT);
      noRoleButton.setStyle("-fx-font: 16 arial;");
      noRoleButton.setText("No Role");
      //Position
      noRoleButton.setLayoutX(WINDOW_WIDTH / 2 + boardImageView.getBoundsInParent().getWidth() / 2 + 10);
      noRoleButton.setLayoutY(WINDOW_HEIGHT / 2 - boardImageView.getBoundsInParent().getHeight() / 2 + 280);
      //Initialize as invisible
      noRoleButton.setVisible(false);
   
      //Money button
      moneyButton = new Button();
      moneyButton.setMinSize(0, BUTT_HEIGHT);
      moneyButton.setStyle("-fx-font: 16 arial;");
      moneyButton.setText("Money");
      //Position
      moneyButton.setLayoutX(WINDOW_WIDTH / 2 + boardImageView.getBoundsInParent().getWidth() / 2 + 10);
      moneyButton.setLayoutY(WINDOW_HEIGHT / 2 - boardImageView.getBoundsInParent().getHeight() / 2 + 320);
      //Initialize as invisible
      moneyButton.setVisible(false);
   
      //Fame button
      fameButton = new Button();
      fameButton.setMinSize(0, BUTT_HEIGHT);
      fameButton.setStyle("-fx-font: 16 arial;");
      fameButton.setText("Fame");
      //Position
      fameButton.setLayoutX(WINDOW_WIDTH / 2 + boardImageView.getBoundsInParent().getWidth() / 2 + 10);
      fameButton.setLayoutY(WINDOW_HEIGHT / 2 - boardImageView.getBoundsInParent().getHeight() / 2 + 360);
      //Initialize as invisible
      fameButton.setVisible(false);
   
      //No Upgrade button
      noUpgradeButton = new Button();
      noUpgradeButton.setMinSize(0, BUTT_HEIGHT);
      noUpgradeButton.setStyle("-fx-font: 16 arial;");
      noUpgradeButton.setText("No Upgrade");
      //Position
      noUpgradeButton.setLayoutX(WINDOW_WIDTH / 2 + boardImageView.getBoundsInParent().getWidth() / 2 + 10);
      noUpgradeButton.setLayoutY(WINDOW_HEIGHT / 2 - boardImageView.getBoundsInParent().getHeight() / 2 + 400);
      //Initialize as invisible
      noUpgradeButton.setVisible(false);
   
      //~~~~~~~~~~Create button effects~~~~~~~~~~//
      DropShadow shadow = new DropShadow();
      //Move button hover shadow
      moveButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
               @Override
               public void handle(MouseEvent e) {
                  moveButton.setEffect(shadow);
               }
            });
      moveButton.addEventHandler(MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
               @Override
               public void handle(MouseEvent e) {
                  moveButton.setEffect(null);
               }
            });
   
      //Work button hover shadow
      workButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
               @Override
               public void handle(MouseEvent e) {
                  workButton.setEffect(shadow);
               }
            });
      workButton.addEventHandler(MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
               @Override
               public void handle(MouseEvent e) {
                  workButton.setEffect(null);
               }
            });
   
      //Do Nothing button hover shadow
      doNothingButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
               @Override
               public void handle(MouseEvent e) {
                  doNothingButton.setEffect(shadow);
               }
            });
      doNothingButton.addEventHandler(MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
               @Override
               public void handle(MouseEvent e) {
                  doNothingButton.setEffect(null);
               }
            });
   
      //Act button hover shadow
      actButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
               @Override
               public void handle(MouseEvent e) {
                  actButton.setEffect(shadow);
               }
            });
      actButton.addEventHandler(MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
               @Override
               public void handle(MouseEvent e) {
                  actButton.setEffect(null);
               }
            });
   
      //Rehearse button hover shadow
      rehearseButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
               @Override
               public void handle(MouseEvent e) {
                  rehearseButton.setEffect(shadow);
               }
            });
      rehearseButton.addEventHandler(MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
               @Override
               public void handle(MouseEvent e) {
                  rehearseButton.setEffect(null);
               }
            });
   
      //On Card button hover shadow
      onCardButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
               @Override
               public void handle(MouseEvent e) {
                  onCardButton.setEffect(shadow);
               }
            });
      onCardButton.addEventHandler(MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
               @Override
               public void handle(MouseEvent e) {
                  onCardButton.setEffect(null);
               }
            });
   
      //Off Card button hover shadow
      offCardButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
               @Override
               public void handle(MouseEvent e) {
                  offCardButton.setEffect(shadow);
               }
            });
      offCardButton.addEventHandler(MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
               @Override
               public void handle(MouseEvent e) {
                  offCardButton.setEffect(null);
               }
            });
   
      //No Role button hover shadow
      noRoleButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
               @Override
               public void handle(MouseEvent e) {
                  noRoleButton.setEffect(shadow);
               }
            });
      noRoleButton.addEventHandler(MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
               @Override
               public void handle(MouseEvent e) {
                  noRoleButton.setEffect(null);
               }
            });
   
      //Money button hover shadow
      moneyButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
               @Override
               public void handle(MouseEvent e) {
                  moneyButton.setEffect(shadow);
               }
            });
      moneyButton.addEventHandler(MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
               @Override
               public void handle(MouseEvent e) {
                  moneyButton.setEffect(null);
               }
            });
   
      //Fame button hover shadow
      fameButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
               @Override
               public void handle(MouseEvent e) {
                  fameButton.setEffect(shadow);
               }
            });
      fameButton.addEventHandler(MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
               @Override
               public void handle(MouseEvent e) {
                  fameButton.setEffect(null);
               }
            });
   
      //No Upgrade button hover shadow
      noUpgradeButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
               @Override
               public void handle(MouseEvent e) {
                  noUpgradeButton.setEffect(shadow);
               }
            });
      noUpgradeButton.addEventHandler(MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
               @Override
               public void handle(MouseEvent e) {
                  noUpgradeButton.setEffect(null);
               }
            });
   
   
      //~~~~~~~~~~Create button actions~~~~~~~~~~//
      //No need to test for program state in handlers as each button is only clickable in proper state
      //Move button
      moveButton.setOnAction(
            new EventHandler<ActionEvent>() {
               @Override
               public void handle(ActionEvent e) {
                  pState = PState.MOVE;
                  synchronized (gameThread) {     //Alert gameThread that user clicked button
                     gameThread.notify();
                  }
               }
            });
   
      //Work button
      workButton.setOnAction(
            new EventHandler<ActionEvent>() {
               @Override
               public void handle(ActionEvent e) {
                  pState = PState.WORK;
                  synchronized (gameThread) {     //Alert gameThread that user clicked button
                     gameThread.notify();
                  }
               }
            });
   
      //Do Nothing button
      doNothingButton.setOnAction(
            new EventHandler<ActionEvent>() {
               @Override
               public void handle(ActionEvent e) {
                  endTurn();                      //End user's turn
                  output.setStyle("-fx-border-color: green;");
               }
            });
   
      //Act button
      actButton.setOnAction(
            new EventHandler<ActionEvent>() {
               @Override
               public void handle(ActionEvent e) {
                  pState = PState.ACT;
                  synchronized (gameThread) {     //Alert gameThread that user clicked button
                     gameThread.notify();
                  }
               }
            });
   
      //Rehearse button
      rehearseButton.setOnAction(
            new EventHandler<ActionEvent>() {
               @Override
               public void handle(ActionEvent e) {
                  pState = PState.REHEARSE;
                  synchronized (gameThread) {     //Alert gameThread that user clicked button
                     gameThread.notify();
                  }
               }
            });
   
      //On Card button
      onCardButton.setOnAction(
            new EventHandler<ActionEvent>() {
               @Override
               public void handle(ActionEvent e) {
                  cardChoiceSel = 1;
                  synchronized (gameThread) {     //Alert gameThread that user clicked button
                     gameThread.notify();
                  }
               }
            });
   
      //Off Card button
      offCardButton.setOnAction(
            new EventHandler<ActionEvent>() {
               @Override
               public void handle(ActionEvent e) {
                  cardChoiceSel = 2;
                  synchronized (gameThread) {     //Alert gameThread that user clicked button
                     gameThread.notify();
                  }
               }
            });
   
      //No Role button
      noRoleButton.setOnAction(
                new EventHandler<ActionEvent>() {
                   @Override
                   public void handle(ActionEvent e) {
                      cardChoiceSel = 3;
                      synchronized (gameThread) {     //Alert gameThread that user clicked button
                         gameThread.notify();
                      }
                   }
                });
   
      //Money button
      moneyButton.setOnAction(
                new EventHandler<ActionEvent>() {
                   @Override
                   public void handle(ActionEvent e) {
                      upgradeSel = 1;
                      synchronized (gameThread) {     //Alert gameThread that user clicked button
                         gameThread.notify();
                      }
                   }
                });
   
      //Fame button
      fameButton.setOnAction(
                new EventHandler<ActionEvent>() {
                   @Override
                   public void handle(ActionEvent e) {
                      upgradeSel = 2;
                      synchronized (gameThread) {     //Alert gameThread that user clicked button
                         gameThread.notify();
                      }
                   }
                });
   
      //No Upgrade button
      noUpgradeButton.setOnAction(
                new EventHandler<ActionEvent>() {
                   @Override
                   public void handle(ActionEvent e) {
                      endTurn();                      //End user's turn
                      output.setStyle("-fx-border-color: green;");
                   }
                });
      //~~~~~~~~~~~~~~~~~~~~~~~~~~End Button Making~~~~~~~~~~~~~~~~~~~~~~~~~~//
   
      //Oscar testing block
      ObservableList<Player> data =
         FXCollections.observableArrayList();
   
      //Create scene
      root.getChildren().add(boardImageView);
      root.getChildren().add(moveButton);
      root.getChildren().add(workButton);
      root.getChildren().add(doNothingButton);
      root.getChildren().add(actButton);
      root.getChildren().add(rehearseButton);
      root.getChildren().add(onCardButton);
      root.getChildren().add(offCardButton);
      root.getChildren().add(noRoleButton);
      root.getChildren().add(moneyButton);
      root.getChildren().add(fameButton);
      root.getChildren().add(noUpgradeButton);
      root.getChildren().add(output);
      root.getChildren().add(locRef);
      root.getChildren().add(input);
      scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
   
      //Create gameThread using gameTask
      gameThread = new Thread(gameTask);
      gameThread.setDaemon(true);                                 //Allow program to exit if thread is still running
      gameThread.start();                                         //Start thread immediately
      output.setText("Welcome to Deadwood! Number of Players?");  //Display starting message
   }

   //Returns scene to be displayed
   public Scene getScene() {
      return scene;
   }

   //Ends the user's turn and prompts the next user for an action
   private void endTurn() {
      currPlayerTable.refresh();
      stats.refresh();
      players.nextCurrent();                              //Change active player
      output.setText(players.getCurrent().getName() + "'s turn: Please select action.");
      moveButton.setVisible(true);                        //Activate buttons
      workButton.setVisible(true);
      doNothingButton.setVisible(true);
      shotsTable.refresh();
   }

   //validNumInput: Checks validity of user string input
   public boolean validNumInput(String s, int lowerB, int upperB) {
      boolean valid;
      try {
         int userNum = Integer.parseInt(s);
         if (userNum >= lowerB && userNum <= upperB) {
            output.setStyle("-fx-border-color: green;");
            valid = true;
         } 
         else {
            output.setText("Out of bounds. Please try again.");
            output.setStyle("-fx-border-color: red;");
            valid = false;
         }
      } 
      catch (Exception e) {
         output.setText("Non-number. Please try again.");
         output.setStyle("-fx-border-color: red;");
         valid = false;
      }
      return valid;
   }
}