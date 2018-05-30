import java.util.ArrayList;
import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.input.*;
import java.lang.*;
import javafx.event.* ;

public class Display extends Application{

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
        Label output = new Label();
        output.setLayoutX(WINDOW_WIDTH/2-boardImageView.getBoundsInParent().getWidth()/2);
        output.setLayoutY(WINDOW_HEIGHT/2+boardImageView.getBoundsInParent().getHeight()/2);
        output.setMinWidth(boardImageView.getBoundsInParent().getWidth()/2);
        output.setMinHeight(30);
        output.setStyle("-fx-border-color: blue; -fx-font: 14 arial;");
        output.setText("Welcome to Deadwood!");


        //~~~~~~~~~Buttons~~~~~~~~~//
        //Move button
        Button moveButton = new Button("Move");
        moveButton.setMaxSize(100, 200);
        moveButton.setStyle("-fx-font: 16 arial;");
        //Position
        moveButton.setLayoutX(WINDOW_WIDTH/2+boardImageView.getBoundsInParent().getWidth()/2+10);
        moveButton.setLayoutY(WINDOW_HEIGHT/2-boardImageView.getBoundsInParent().getHeight()/2);

        //Work button
        Button workButton = new Button("Work");
        workButton.setMaxSize(100, 200);
        workButton.setStyle("-fx-font: 16 arial;");
        //Position
        workButton.setLayoutX(WINDOW_WIDTH/2+boardImageView.getBoundsInParent().getWidth()/2+10);
        workButton.setLayoutY(WINDOW_HEIGHT/2-boardImageView.getBoundsInParent().getHeight()/2+40);

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
                output.setStyle("-fx-border-color: blue; -fx-font: 14 arial;");
                output.setText("Move");
            }
        });

        //Work button
        workButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                output.setStyle("-fx-border-color: red; -fx-font: 14 arial;");
                output.setText("Work");
            }
        });


        //Create scene
        root.getChildren().add(boardImageView);
        root.getChildren().add(moveButton);
        root.getChildren().add(workButton);
        root.getChildren().add(output);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setTitle("Deadwood");
        stage.setScene(scene);

        stage.show();
    }

    public void launchDisplay(){
        launch();
    }
}
