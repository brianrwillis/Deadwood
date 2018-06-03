import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.input.*;
import java.lang.*;
import javafx.event.*;

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

    private static Players players = null;
    private static Deck deck = null;
    private static Board board = null;

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

        //Create input action
        input.setOnAction(new EventHandler<ActionEvent>() {
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
                                } catch(Exception ex){}

                                //Create moderator and calculator instances
                                Moderator moderator = new Moderator(players, deck, board);
                                Calculator calculator = new Calculator(players);

                                playerNames = new String[playerCnt];
                                outputText = "Player 1's name?";
                                pState = PState.PLAYER_NAMES;
                            }
                            break;

                        case PLAYER_NAMES:
                            if(playerNamesCnt < playerCnt){
                                playerNames[playerNamesCnt] = inputText;
                                playerNamesCnt++;
                                outputText = ("Player " + (playerNamesCnt + 1) + "'s name?");
                            } else {                    //Collected all player names, add all to players list
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


        //~~~~~~~~~Buttons~~~~~~~~~//
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
        moveButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                moveButton.setEffect(shadow);
            }
        });
        moveButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                moveButton.setEffect(null);
            }
        });

        //Work button hover shadow
        workButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                workButton.setEffect(shadow);
            }
        });
        workButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                workButton.setEffect(null);
            }
        });

        //Create button actions
        //Move button
        moveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                actionSel = 1;
            }
        });

        //Work button
        workButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                actionSel = 2;
            }
        });


        //Create scene
        root.getChildren().add(boardImageView);
        root.getChildren().add(moveButton);
        root.getChildren().add(workButton);
        root.getChildren().add(output);
        root.getChildren().add(input);
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
                } else {
                    outputText = ("Out of bounds. Please try again.");
                    output.setStyle("-fx-border-color: red;");
                    valid = false;
                }
            } catch (Exception e) {
                outputText = ("Non-number. Please try again.");
                output.setStyle("-fx-border-color: red;");
                valid = false;
            }
        return valid;
    }
}
