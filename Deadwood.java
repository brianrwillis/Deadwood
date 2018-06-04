import javafx.stage.*;

import javax.xml.parsers.*;
import javafx.application.*;
import javafx.concurrent.*;

public class Deadwood extends Application {
    private static Display display = new Display();

    public Deadwood() {
    }

    public static void main(String[] args) {
        launch();   //Start the game
    }

    @Override
    public void start(Stage stage) throws InterruptedException {
        display.displayInit();

        stage.setTitle("Deadwood");
        stage.setScene(display.getScene());
        stage.show();
    }
}