package sample;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent intro = FXMLLoader.load(getClass().getClassLoader().getResource("fxml_file/Intro.fxml"));
        Stage introStage = new Stage();
        introStage.setScene(new Scene(intro));
        introStage.setTitle("Introduction");

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml_file/Login.fxml"));
        primaryStage.setTitle("EmailApp");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        introStage.show();

        //show intro in 2.5s
        PauseTransition delay = new PauseTransition(Duration.millis(2500));
        delay.setOnFinished( event -> introStage.close() );
        delay.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
