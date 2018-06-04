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
import java.util.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Display {
    private static Button moveButton;
    private static Button workButton;
    private static Button rehearseButton;
    private static Button upgradeButton;
    private static Button onCardButton;
    private static Button offCardButton;
    private static Button noRoleButton;
    private static Label output;
    private static TextField input;
    private static Scene scene;
    private static int actionSel = 0;
    private static String inputText;
    private static String outputText = "Welcome to Deadwood! Number of Players?";
    private static int playerCnt;
    private static String[] playerNames;
    private static int playerNamesCnt = 0;
    private static TableView<Person> stats;
    private static TableView onCard;
    private static TableView offCard;
    private static Players players = null;
    private static Deck deck = null;
    private static Board board = null;
    private static Moderator moderator = null;
    private static Calculator calculator = null;


    public enum PState {
        PLAYER_CNT,
        PLAYER_NAMES,
        MOVE_WORK
    }
    public PState pState = PState.PLAYER_CNT;

    public Thread inputThread;
    public Thread buttonThread;

    public Display() {
    }

    public void displayInit() throws InterruptedException, ParserConfigurationException {
        final int BOARD_HEIGHT = 900;
        final int WINDOW_WIDTH = 1800;
        final int WINDOW_HEIGHT = 1000;


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
                                        moderator = new Moderator(players, deck, board);
                                        calculator = new Calculator(players);

                                        playerNames = new String[playerCnt];
                                        outputText = "Player 1's name?";
                                        pState = PState.PLAYER_NAMES;
                                    }
                                    break;

                                case PLAYER_NAMES:
                                    if(playerNamesCnt < playerCnt - 1){
                                        playerNames[playerNamesCnt] = inputText;
                                        playerNamesCnt++;
                                        outputText = ("Player " + (playerNamesCnt + 1) + "'s name?");
                                    }
                                    else {                    //Collected all player names, add all to players list
                                        players.addPlayers(playerNames);

                                        int[] rules = calculator.calcRules(playerCnt);      //Calculate starting parameters
                                        int daysLeft = rules[2];                            //Set remaining days

                                        //Set player starting attributes
                                        ArrayList<Player> playersList = players.getPlayers();
                                        for (int i = 0; i < playerCnt; i++) {
                                            moderator.giveFunds(playersList.get(i), 0, rules[1]);
                                            moderator.increaseRank(playersList.get(i), rules[0] - 1);
                                        }
                                        pState = PState.MOVE_WORK;
                                        output.setText("change dis");
                                        inputThread.stop();                     //Stop input scanning thread
                                        buttonThread.start();                   //Begin button input thread
                                        synchronized (buttonThread) {           //Alert buttonThread that buttons are now being used
                                            buttonThread.notify();
                                        }
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


        //~~~~~~~~~~Stats~~~~~~~~~~//
        Player p1=new Player("Sam1");
        Player p2=new Player("Sam2");
        Person pp1=p1.getPerson();
        Person pp2=p2.getPerson();
               ObservableList<Person> data1 = FXCollections.observableArrayList(pp1, pp2);
        //       ArrayList<Player> pList=this.players.getPlayers();
        //       for(int i=0; i<pList.size();i++){
        //          data.add(pList.get(i));
        //          }
        p1.setRank(3);
        p2.setCredit(5);
        stats=new TableView<Person>();
        stats.setEditable(true);
        TableColumn PNameCol=new TableColumn("Name");
        PNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
        TableColumn PCreditCol=new TableColumn("Credits");
        PCreditCol.setCellValueFactory(new PropertyValueFactory<Person, String>("credit"));
        TableColumn PFameCol=new TableColumn("Fame");
        PFameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("fame"));
        TableColumn PRankCol=new TableColumn("Rank");
        PRankCol.setCellValueFactory(new PropertyValueFactory<Person, String>("rank"));
        TableColumn PRehearCol=new TableColumn("Rehearsals");
        PRehearCol.setCellValueFactory(new PropertyValueFactory<Person, String>("rehearsals"));
        stats.setLayoutX(0);
        stats.setLayoutY(0);
        stats.setMinWidth(100);
        stats.setMinHeight(100);
        stats.setMaxWidth(300);
        stats.setItems(data1);
        stats.getColumns().addAll(PNameCol, PCreditCol, PFameCol, PRankCol, PRehearCol);        


        //~~~~~~~~~~On Card Roles~~~~~~~~~~//Need to make object with Number, Rank, Availability and Name from ArrayList of PlayerSpots.
//         final Label onCardLabel=new Label("On Card Roles:");
//         //onCardLabel.setFont(new Font("Times New Roman", 15));
//         VBox onCardBox=new VBox();
//         onCard=new TableView();
//         onCard.setEditable(true);
//         TableColumn OnNum=new TableColumn("Number");
//         TableColumn OnRank=new TableColumn("Rank");
//         TableColumn OnAvail=new TableColumn("Availability");
//         TableColumn OnName=new TableColumn("Name");
//         onCardBox.setLayoutX(300);
//         onCardBox.setLayoutY(300);
//         onCard.setMinWidth(100);
//         onCard.setMinHeight(100);
//         onCard.getColumns().addAll(OnNum, OnRank, OnAvail, OnName);
//         onCardBox.setSpacing(5);
//         onCardBox.setPadding(new Insets(10, 0, 0, 10));
//         onCardBox.getChildren().addAll(onCardLabel, onCard);
//         onCardBox.setVisible(true);
         Board tester=new Board();
         Deck dTester=new Deck();
         Room RTest=tester.getRoom(0);
         RTest.addCard(dTester.getTopofOrder());
         CardTableMaker pTester= new CardTableMaker();
         TableView onCard=pTester.getOnCard(tester, 0);
         TableView offCard=pTester.getOffCard(tester, 0);
         onCard.setVisible(true);
         onCard.setLayoutX(0);
         onCard.setLayoutY(400);        
         onCard.setMaxHeight(200); 


        //~~~~~~~~~Off Card Roles~~~~~~~~~//Need to make object with Number, Rank, Availability and Name from ArrayList of PlayerSpots.
        final Label offCardLabel=new Label("Off Card Roles:");
        //offCardLabel.setFont(new Font("Times New Roman", 15));        
//         offCard=new TableView();
//         offCard.setEditable(true);
//         TableColumn OffNum=new TableColumn("Number1");
//         TableColumn OffRank=new TableColumn("Rank");
//         TableColumn OffAvail=new TableColumn("Availability");
//         TableColumn OffName=new TableColumn("Name");
        offCard.setLayoutX(0);
        offCard.setLayoutY(600);
        //offCard.setMinWidth(100);
        offCard.setMaxHeight(200);
//         offCard.getColumns().addAll(OffNum, OffRank, OffAvail, OffName);
//         offCard.setVisible(false);


        //~~~~~~~~~~~~~~~~~~~~~~~~~~Begin Button Making~~~~~~~~~~~~~~~~~~~~~~~~~~//
        //~~~~~~~~~~Initialize buttons~~~~~~~~~~//
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

        //On Card button
        onCardButton = new Button();
        onCardButton.setMaxSize(100, 200);
        onCardButton.setStyle("-fx-font: 16 arial;");
        onCardButton.setText("On Card");
        //Position
        onCardButton.setLayoutX(WINDOW_WIDTH/2+boardImageView.getBoundsInParent().getWidth()/2+10);
        onCardButton.setLayoutY(WINDOW_HEIGHT/2-boardImageView.getBoundsInParent().getHeight()/2+160);
        //Initialize as invisible
        onCardButton.setVisible(false);

        //Off Card button
        offCardButton = new Button();
        offCardButton.setMaxSize(100, 200);
        offCardButton.setStyle("-fx-font: 16 arial;");
        offCardButton.setText("Off Card");
        //Position
        offCardButton.setLayoutX(WINDOW_WIDTH/2+boardImageView.getBoundsInParent().getWidth()/2+10);
        offCardButton.setLayoutY(WINDOW_HEIGHT/2-boardImageView.getBoundsInParent().getHeight()/2+200);
        //Initialize as invisible
        offCardButton.setVisible(false);

        //No Role button
        noRoleButton = new Button();
        noRoleButton.setMaxSize(100, 200);
        noRoleButton.setStyle("-fx-font: 16 arial;");
        noRoleButton.setText("No Role");
        //Position
        noRoleButton.setLayoutX(WINDOW_WIDTH/2+boardImageView.getBoundsInParent().getWidth()/2+10);
        noRoleButton.setLayoutY(WINDOW_HEIGHT/2-boardImageView.getBoundsInParent().getHeight()/2+240);
        //Initialize as invisible
        noRoleButton.setVisible(false);

        //~~~~~~~~~~Create button effects~~~~~~~~~~//
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

        //Rehearse button hover shadow
        rehearseButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        rehearseButton.setEffect(shadow);
                    }
                });
        rehearseButton.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        rehearseButton.setEffect(null);
                    }
                });

        //Upgrade button hover shadow
        upgradeButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        upgradeButton.setEffect(shadow);
                    }
                });
        upgradeButton.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        upgradeButton.setEffect(null);
                    }
                });

        //On Card button hover shadow
        onCardButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        onCardButton.setEffect(shadow);
                    }
                });
        onCardButton.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        onCardButton.setEffect(null);
                    }
                });

        //Off Card button hover shadow
        offCardButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        offCardButton.setEffect(shadow);
                    }
                });
        offCardButton.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        offCardButton.setEffect(null);
                    }
                });

        //No Role button hover shadow
        noRoleButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        noRoleButton.setEffect(shadow);
                    }
                });
        noRoleButton.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        noRoleButton.setEffect(null);
                    }
                });


        //~~~~~~~~~~Create button actions~~~~~~~~~~//
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
        rehearseButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
//                        actionSel = 3;
                    }
                });

        //Rehearse button
        upgradeButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
//                        actionSel = 3;
                    }
                });

        //On Card button
        onCardButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
//                        actionSel = 3;
                    }
                });

        //Off Card button
        offCardButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
//                        actionSel = 3;
                    }
                });

        //No Role button
        noRoleButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
//                        actionSel = 3;
                    }
                });
        //~~~~~~~~~~~~~~~~~~~~~~~~~~End Button Making~~~~~~~~~~~~~~~~~~~~~~~~~~//

        //Oscar testing block
        //ObservableList<Player> data =
        //        FXCollections.observableArrayList();
        //ArrayList<Player> pList=this.players.getPlayers();
        //for(int i=0; i<pList.size();i++){
        //   data.add(pList.get(i));
        //   }
        //stats.setItems(data);
        //stats.getColumns().addAll(PNameCol, PCreditCol, PFameCol, PRankCol);

        //Create scene
        root.getChildren().add(boardImageView);
        root.getChildren().add(moveButton);
        root.getChildren().add(workButton);
        root.getChildren().add(rehearseButton);
        root.getChildren().add(upgradeButton);
        root.getChildren().add(onCardButton);
        root.getChildren().add(offCardButton);
        root.getChildren().add(noRoleButton);
        root.getChildren().add(onCard);
        //root.getChildren().add(onCard);

        root.getChildren().add(offCard);
        root.getChildren().add(output);
        root.getChildren().add(input);
        root.getChildren().add(stats);
        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);



        //~~~~~~~~~~~~~~~~~~~~~~~~~~Create Task Threads~~~~~~~~~~~~~~~~~~~~~~~~~~//
        //Get user input task
        Task inputTextTask = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                while (true) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {         //Display output message
                            output.setText(outputText);
                        }
                    });
                    synchronized (inputThread) {
                        inputThread.wait();         //Wait for next user input
                    }
                }
            }
        };
        inputThread = new Thread(inputTextTask);
        inputThread.setDaemon(true);                //Allow program to exit if thread is still running
        inputThread.start();                        //Start text input thread immediately

        //Button visibility task
        Task buttonTask = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                while (true) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            switch(pState) {        //Set visibility of buttons per program state
                                case MOVE_WORK:
                                    moveButton.setVisible(true);
                                    workButton.setVisible(true);
                                    break;

                                default:            //Set all buttons invisible by default
                                    moveButton.setVisible(false);
                                    workButton.setVisible(false);
                                    rehearseButton.setVisible(false);
                                    upgradeButton.setVisible(false);
                                    onCardButton.setVisible(false);
                                    offCardButton.setVisible(false);
                                    noRoleButton.setVisible(false);
                                    break;
                            }
                        }
                    });
                    synchronized (inputThread) {
                        inputThread.wait();         //Wait for program state
                    }
                }
            }
        };
        buttonThread = new Thread(buttonTask);
        buttonThread.setDaemon(true);               //Allow program to exit if thread is still running
    }

    public Scene getScene(){
        return scene;
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

    private void setPlayerCnt(int pCnt){
        playerCnt = pCnt;
    }

    public void resetActionSel(){
        actionSel = 0;
    }


    //validNumInput: Checks validity of user string input
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