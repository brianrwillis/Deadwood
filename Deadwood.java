import javafx.stage.*;
import javafx.application.*;

public class Deadwood extends Application {
    private static Display display = new Display(); //View/Controller

    public Deadwood() {
    }

    public static void main(String[] args) {
        launch();                   //Launch the game
    }

    @Override
    public void start(Stage stage) throws InterruptedException {
        display.displayInit();      //Create parallel game thread and GUI elements

        stage.setTitle("Deadwood");
        stage.setScene(display.getScene());
        stage.show();               //Start displaying the game
    }
}