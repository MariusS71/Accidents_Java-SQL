package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
       FXMLLoader fxmlLoader = new FXMLLoader(sample.Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 307);
        primaryStage.setTitle("Baza de date accidente Login");
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}