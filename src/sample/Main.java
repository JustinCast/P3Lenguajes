package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jpl7.Query;

import javax.swing.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        root.setId("pane");
        primaryStage.setTitle("SpiderRobot by JustinCast");
        primaryStage.getIcons().add(new Image("./assets/icon.png"));
        Scene scene = new Scene(root, 1200, 1000);
        scene.getStylesheets().addAll(this.getClass().getResource("../StyleSheets/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
