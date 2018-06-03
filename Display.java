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
import java.util.*;

public class Display {
   private static Button moveButton;
   private static Button workButton;
   private static Label output;
   private static TextField input;
   private static Scene scene;
   private static int actionSel = 0;
   private static String inputText;
   private static String outputText = "Welcome to Deadwood! Number of Players?";
   private static int playerCnt;
   private static String[] playerNames;
   private static int playerNamesCnt = 0;
   private static TableView stats;
   private static Button rehearseButton;
   private static Button upgradeButton;
   private static Players players = null;
   private static Deck deck = null;
   private static Board board = null;
   private static Button onCardButton;
   private static Button offCardButton;
   private static Button noRoleButton;
   private static TableView onCard;
   private static TableView offCard;

   private enum PState {
      PLAYER_CNT,
      PLAYER_NAMES;
   }
   private PState pState = PState.PLAYER_CNT;

   public Thread inputThread;

   public Display() {
   }

   public void displayInit() throws InterruptedException {
      final int BOARD_HEIGHT = 800;
      final int WINDOW_WIDTH = 1750;
      final int WINDOW_HEIGHT = 1100;
   
   
      Group root = new Group();
   
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
      boardImageView.setX(WINDOW_WIDTH/2-boardImageView.getBoundsInParent().getWidth()/2);
      boardImageView.setY(WINDOW_HEIGHT/2-boardImageView.getBoundsInParent().getHeight()/2);
   
   
      //~~~~~~~~~Text Output~~~~~~~~~//
      //Create main output
      output = new Label();
      output.setLayoutX(WINDOW_WIDTH/2-boardImageView.getBoundsInParent().getWidth()/2);
      output.setLayoutY(WINDOW_HEIGHT/2+boardImageView.getBoundsInParent().getHeight()/2);
      output.setMinWidth(boardImageView.getBoundsInParent().getWidth()/2);
      output.setMinHeight(30);
      output.setStyle("-fx-border-color: green; -fx-font: 14 arial;");
   
   
      //~~~~~~~~~Text Input~~~~~~~~~//
      //Create main input
      input = new TextField ();
      input.setLayoutX(WINDOW_WIDTH/2);
      input.setLayoutY(WINDOW_HEIGHT/2+boardImageView.getBoundsInParent().getHeight()/2);
      input.setMinWidth(boardImageView.getBoundsInParent().getWidth()/2);
      input.setMinHeight(30);
      input.setStyle("-fx-border-color: blue; -fx-font: 14 arial;");
      input.setPromptText("Enter inputs here.");
   
      //~~~~~~~~~~Stats~~~~~~~~~~//
   //       ObservableList<Player> data =
   //             FXCollections.observableArrayList();
   //       ArrayList<Player> pList=this.players.getPlayers();
   //       for(int i=0; i<pList.size();i++){
   //          data.add(pList.get(i));
   //          }
      stats=new TableView();
      stats.setEditable(true);
      TableColumn PNameCol=new TableColumn("Name");
      PNameCol.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
      TableColumn PCreditCol=new TableColumn("Credits");
      PCreditCol.setCellValueFactory(new PropertyValueFactory<Player, String>("credit"));
      TableColumn PFameCol=new TableColumn("Fame");
      PFameCol.setCellValueFactory(new PropertyValueFactory<Player, String>("fame"));
      TableColumn PRankCol=new TableColumn("Rank");
      PRankCol.setCellValueFactory(new PropertyValueFactory<Player, String>("rank"));
      //stats.setItems(data);
      //stats.getColumns().addAll(PNameCol, PCreditCol, PFameCol, PRankCol);
      stats.setLayoutX(WINDOW_WIDTH-200);
      stats.setLayoutY(WINDOW_HEIGHT/2-boardImageView.getBoundsInParent().getHeight()/2);
      stats.setMinWidth(100);
      stats.setMinHeight(100);
      
      //~~~~~~~~~~On Card Roles~~~~~~~~~~//Need to make object with Number, Rank, Availablity and Name from ArrayList of PlayerSpots.
      onCard=new TableView();
      onCard.setEditable(true);
      TableColumn OnNum=new TableColumn("Number");
      TableColumn OnRank=new TableColumn("Rank");
      TableColumn OnAvail=new TableColumn("Availablity");
      TableColumn OnName=new TableColumn("Name");
      onCard.setLayoutX(WINDOW_WIDTH-200);
      onCard.setLayoutY(WINDOW_HEIGHT/2-boardImageView.getBoundsInParent().getHeight()/2+100);
      onCard.setMinWidth(100);
      onCard.setMinHeight(100);
      onCard.getColumns().addAll(OnNum, OnRank, OnAvail, OnName);
      
      //~~~~~~~~~Off Card Roles~~~~~~~~~//Need to make object with Number, Rank, Availablity and Name from ArrayList of PlayerSpots.
      offCard=new TableView();
      offCard.setEditable(true);
      TableColumn OffNum=new TableColumn("Number1");
      TableColumn OffRank=new TableColumn("Rank");
      TableColumn OffAvail=new TableColumn("Availablity");
      TableColumn OffName=new TableColumn("Name");
      offCard.setLayoutX(WINDOW_WIDTH-200);
      offCard.setLayoutY(WINDOW_HEIGHT/2-boardImageView.getBoundsInParent().getHeight()/2+200);
      offCard.setMinWidth(100);
      offCard.setMinHeight(100);  
      offCard.getColumns().addAll(OffNum, OffRank, OffAvail, OffName);
    
   
      //Create input action
      input.setOnAction(
         new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
               if ((input.getText() != null && !input.getText().isEmpty())) {
                  inputText = input.getText();
                  input.clear();
                  switch(pState){
                     case PLAYER_CNT:    //Get number of players from user
                        if(validNumInput(inputText, 2, 8)){
                           setPlayerCnt(Integer.parseInt(inputText));
                        
                           players = new Players(playerCnt);                   //Make players
                           players.shuffle();                                  //Randomize player order
                           Players playersOrdered = players;                   //Store starting order of players for calculating winner later
                        
                        //Create deck and board instances
                           try {
                              deck = new Deck();
                              board = new Board();
                           } 
                           catch(Exception ex){}
                        
                        //Create moderator and calculator instances
                           Moderator moderator = new Moderator(players, deck, board);
                           Calculator calculator = new Calculator(players);
                        
                           playerNames = new String[playerCnt];
                           outputText = "Player 1's name?";
                           pState = PState.PLAYER_NAMES;
                        }
                        break;
                  
                     case PLAYER_NAMES:
                        if(playerNamesCnt < playerCnt-1){
                           playerNames[playerNamesCnt] = inputText;
                           playerNamesCnt++;
                           outputText = ("Player " + (playerNamesCnt + 1) + "'s name?");
                        } 
                        else {                    //Collected all player names, add all to players list
                           players.addPlayers(playerNames);
                        }
                        break;
                  
                     default:
                        break;
                  }
                  synchronized (inputThread) {        //Alert inputThread that input has finished parsing
                     inputThread.notify();
                  }
               }
            }
         });
   
   
   //~~~~~~~~~~~~~~~~~~~~~~~~~~Begin Button Making~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   
      //Move button
      moveButton = new Button();
      moveButton.setMaxSize(100, 200);
      moveButton.setStyle("-fx-font: 16 arial;");
      moveButton.setText("Move");
      //Position
      moveButton.setLayoutX(WINDOW_WIDTH/2+boardImageView.getBoundsInParent().getWidth()/2+10);
      moveButton.setLayoutY(WINDOW_HEIGHT/2-boardImageView.getBoundsInParent().getHeight()/2);
      //Initialize as invisible
      moveButton.setVisible(false);
   
      //Work button
      workButton = new Button();
      workButton.setMaxSize(100, 200);
      workButton.setStyle("-fx-font: 16 arial;");
      workButton.setText("Work");
      //Position
      workButton.setLayoutX(WINDOW_WIDTH/2+boardImageView.getBoundsInParent().getWidth()/2+10);
      workButton.setLayoutY(WINDOW_HEIGHT/2-boardImageView.getBoundsInParent().getHeight()/2+40);
      workButton.setVisible(false);
   
      //Create button effects
      DropShadow shadow = new DropShadow();
      //Move button hover shadow
      moveButton.addEventHandler(MouseEvent.MOUSE_ENTERED, 
         new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
               moveButton.setEffect(shadow);
            }
         });
      moveButton.addEventHandler(MouseEvent.MOUSE_EXITED, 
         new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
               moveButton.setEffect(null);
            }
         });
   
      //Work button hover shadow
      workButton.addEventHandler(MouseEvent.MOUSE_ENTERED, 
         new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
               workButton.setEffect(shadow);
            }
         });
      workButton.addEventHandler(MouseEvent.MOUSE_EXITED, 
         new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
               workButton.setEffect(null);
            }
         });
      //Create button actions
      //Move button
      moveButton.setOnAction(
         new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
               actionSel = 1;
            }
         });
   
      //Work button
      workButton.setOnAction(
         new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
               actionSel = 2;
            }
         });
         
      //Rehearse button
      rehearseButton = new Button();
      rehearseButton.setMaxSize(100, 200);
      rehearseButton.setStyle("-fx-font: 16 arial;");
      rehearseButton.setText("Rehearse");
      //Position
      rehearseButton.setLayoutX(WINDOW_WIDTH/2+boardImageView.getBoundsInParent().getWidth()/2+10);
      rehearseButton.setLayoutY(WINDOW_HEIGHT/2-boardImageView.getBoundsInParent().getHeight()/2+80);
      //Initialize as invisible
      rehearseButton.setVisible(false);
      //rehearseButton hover shadow
      rehearseButton.addEventHandler(MouseEvent.MOUSE_ENTERED, 
         new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
               moveButton.setEffect(shadow);
            }
         });
      rehearseButton.addEventHandler(MouseEvent.MOUSE_EXITED, 
         new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
               moveButton.setEffect(null);
            }
         });
       ///when clicked, change actionSel probably.
      rehearseButton.setOnAction(
         new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
               actionSel = 3;
            }
         });   
   
      //Upgrade button
      upgradeButton = new Button();
      upgradeButton.setMaxSize(100, 200);
      upgradeButton.setStyle("-fx-font: 16 arial;");
      upgradeButton.setText("Upgrade");
      //Position
      upgradeButton.setLayoutX(WINDOW_WIDTH/2+boardImageView.getBoundsInParent().getWidth()/2+10);
      upgradeButton.setLayoutY(WINDOW_HEIGHT/2-boardImageView.getBoundsInParent().getHeight()/2+120);
      //Initialize as invisible
      upgradeButton.setVisible(false);
      //rehearseButton hover shadow
      upgradeButton.addEventHandler(MouseEvent.MOUSE_ENTERED, 
         new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
               moveButton.setEffect(shadow);
            }
         });
      upgradeButton.addEventHandler(MouseEvent.MOUSE_EXITED, 
         new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
               moveButton.setEffect(null);
            }
         });
       ///when clicked, change actionSel probably.
      upgradeButton.setOnAction(
         new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
               actionSel = 3;
            }
         });         
      //onCard button
      onCardButton = new Button();
      onCardButton.setMaxSize(100, 200);
      onCardButton.setStyle("-fx-font: 16 arial;");
      onCardButton.setText("On Card");
      //Position
      onCardButton.setLayoutX(WINDOW_WIDTH/2+boardImageView.getBoundsInParent().getWidth()/2+10);
      onCardButton.setLayoutY(WINDOW_HEIGHT/2-boardImageView.getBoundsInParent().getHeight()/2+160);
      //Initialize as invisible
      onCardButton.setVisible(false);
      //rehearseButton hover shadow
      onCardButton.addEventHandler(MouseEvent.MOUSE_ENTERED, 
         new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
               moveButton.setEffect(shadow);
            }
         });
      onCardButton.addEventHandler(MouseEvent.MOUSE_EXITED, 
         new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
               moveButton.setEffect(null);
            }
         });
       ///when clicked, change actionSel probably.
      onCardButton.setOnAction(
         new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
               actionSel = 3;
            }
         }); 
      //offCard button
      offCardButton = new Button();
      offCardButton.setMaxSize(100, 200);
      offCardButton.setStyle("-fx-font: 16 arial;");
      offCardButton.setText("Off Card");
      //Position
      offCardButton.setLayoutX(WINDOW_WIDTH/2+boardImageView.getBoundsInParent().getWidth()/2+10);
      offCardButton.setLayoutY(WINDOW_HEIGHT/2-boardImageView.getBoundsInParent().getHeight()/2+200);
      //Initialize as invisible
      offCardButton.setVisible(false);
      //rehearseButton hover shadow
      offCardButton.addEventHandler(MouseEvent.MOUSE_ENTERED, 
         new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
               moveButton.setEffect(shadow);
            }
         });
      offCardButton.addEventHandler(MouseEvent.MOUSE_EXITED, 
         new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
               moveButton.setEffect(null);
            }
         });
       ///when clicked, change actionSel probably.
      offCardButton.setOnAction(
         new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
               actionSel = 3;
            }
         });
         
      //NoRole button
      noRoleButton = new Button();
      noRoleButton.setMaxSize(100, 200);
      noRoleButton.setStyle("-fx-font: 16 arial;");
      noRoleButton.setText("No Role");
      //Position
      noRoleButton.setLayoutX(WINDOW_WIDTH/2+boardImageView.getBoundsInParent().getWidth()/2+10);
      noRoleButton.setLayoutY(WINDOW_HEIGHT/2-boardImageView.getBoundsInParent().getHeight()/2+240);
      //Initialize as invisible
      noRoleButton.setVisible(false);
      //rehearseButton hover shadow
      noRoleButton.addEventHandler(MouseEvent.MOUSE_ENTERED, 
         new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
               moveButton.setEffect(shadow);
            }
         });
      noRoleButton.addEventHandler(MouseEvent.MOUSE_EXITED, 
         new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
               moveButton.setEffect(null);
            }
         });
       ///when clicked, change actionSel probably.
      noRoleButton.setOnAction(
         new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
               actionSel = 3;
            }
         });                                 
   //~~~~~~~~~~~~~~~~~~~~~~~~~~End Button Making~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   
         
      //Oscar testing block
      ObservableList<Player> data =
            FXCollections.observableArrayList();
      //ArrayList<Player> pList=this.players.getPlayers();
      //for(int i=0; i<pList.size();i++){
      //   data.add(pList.get(i));
      //   }
      stats.setItems(data);
      stats.getColumns().addAll(PNameCol, PCreditCol, PFameCol, PRankCol);
      //Create scene
      root.getChildren().add(boardImageView);
      root.getChildren().add(moveButton);
      root.getChildren().add(workButton);
      root.getChildren().add(output);
      root.getChildren().add(input);
      root.getChildren().add(stats);
      root.getChildren().add(rehearseButton);
      root.getChildren().add(upgradeButton);
      root.getChildren().add(onCardButton);
      root.getChildren().add(offCardButton);
      root.getChildren().add(noRoleButton);
      root.getChildren().add(onCard);
      root.getChildren().add(offCard);
   
      scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
   }

   public void testes(){
   
   }

   public Scene getScene(){
      return scene;
   }

   public Button getMoveButton(){
      return moveButton;
   }

   public Button getWorkButton(){
      return workButton;
   }

   public Label getOutput(){
      return output;
   }
   
   public TableView getStats(){
      return stats;
   }

   public int getActionSel(){
      return actionSel;
   }

   public String getInputText(){
      return inputText;
   }

   public TextField getInput(){
      return input;
   }

   public String getOutputText(){
      return outputText;
   }

   private void setPlayerCnt(int pCnt){
      this.playerCnt = pCnt;
   }

   public void resetActionSel(){
      actionSel = 0;
   }

   //checkNumInput: Checks validity of user string input
   public boolean validNumInput(String s, int lowerB, int upperB) {
      boolean valid = false;
      try {
         int userNum = Integer.parseInt(s);
         if (userNum >= lowerB && userNum <= upperB) {
            output.setStyle("-fx-border-color: green;");
            valid = true;
         } 
         else {
            outputText = ("Out of bounds. Please try again.");
            output.setStyle("-fx-border-color: red;");
            valid = false;
         }
      } 
      catch (Exception e) {
         outputText = ("Non-number. Please try again.");
         output.setStyle("-fx-border-color: red;");
         valid = false;
      }
      return valid;
   }
}
