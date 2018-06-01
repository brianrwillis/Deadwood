import java.util.ArrayList;
import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.input.*;
import java.lang.*;
import javafx.event.*;
import javafx.embed.swing.JFXPanel;

public class Display extends Application{
    final JFXPanel fxPanel = new JFXPanel();
    private Button button1 = new Button();
    private Button button2 = new Button();
    private Label output = new Label();

    public Display() {
    }

    @Override
    public void start(Stage stage) {
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


        //~~~~~~~~~Labels~~~~~~~~~//
        //Main output
        output.setLayoutX(WINDOW_WIDTH/2-boardImageView.getBoundsInParent().getWidth()/2);
        output.setLayoutY(WINDOW_HEIGHT/2+boardImageView.getBoundsInParent().getHeight()/2);
        output.setMinWidth(boardImageView.getBoundsInParent().getWidth()/2);
        output.setMinHeight(30);
        output.setStyle("-fx-border-color: blue; -fx-font: 14 arial;");
        output.setText("Welcome to Deadwood!");


        //~~~~~~~~~Buttons~~~~~~~~~//
        //Move button
        button1.setMaxSize(100, 200);
        button1.setStyle("-fx-font: 16 arial;");
        //Position
        button1.setLayoutX(WINDOW_WIDTH/2+boardImageView.getBoundsInParent().getWidth()/2+10);
        button1.setLayoutY(WINDOW_HEIGHT/2-boardImageView.getBoundsInParent().getHeight()/2);
        //Initialize as invisible
        button1.setVisible(false);

        //Work button
        button2.setMaxSize(100, 200);
        button2.setStyle("-fx-font: 16 arial;");
        //Position
        button2.setLayoutX(WINDOW_WIDTH/2+boardImageView.getBoundsInParent().getWidth()/2+10);
        button2.setLayoutY(WINDOW_HEIGHT/2-boardImageView.getBoundsInParent().getHeight()/2+40);
        button2.setVisible(false);

        //Create button effects
        DropShadow shadow = new DropShadow();
        //Move button hover shadow
        button1.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                button1.setEffect(shadow);
            }
        });
        button1.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                button1.setEffect(null);
            }
        });

        //Work button hover shadow
        button2.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                button2.setEffect(shadow);
            }
        });
        button2.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                button2.setEffect(null);
            }
        });

        //Create button actions
        //Move button
        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                output.setStyle("-fx-border-color: blue; -fx-font: 14 arial;");
                output.setText("Move");
            }
        });

        //Work button
        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                output.setStyle("-fx-border-color: red; -fx-font: 14 arial;");
                output.setText("Work");
            }
        });


        //Create scene
        root.getChildren().add(boardImageView);
        root.getChildren().add(button1);
        root.getChildren().add(button2);
        root.getChildren().add(output);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setTitle("Deadwood");
        stage.setScene(scene);

        stage.show();
    }
    public int getMoveChoice(){
        output.setText("Please select action.");
        button1.setText("Move");
        button2.setText("Work");
        //use ActionEvent?
        return 0;
    }

    public void launchDisplay(){
        launch();
    }
}
