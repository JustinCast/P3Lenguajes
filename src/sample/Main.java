package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jpl7.Query;

import javax.swing.*;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.concurrent.ThreadLocalRandom;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        String PROLOG = "consult('ScriptProlog.pl')";
        Query query = new Query(PROLOG);
        if (query.hasSolution()) {
            addNodes();

            String prueba = "find_paths(35, 0)";
            Query q4 = new Query(prueba);
            System.out.println(q4.oneSolution()); q4.close();
        } else {
            JOptionPane.showMessageDialog(null, "!!!No se pudo conectar con Prolog!!!");
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void addNodes() {
        for(int i = 0; i < 35; i++) {

            // agrega hacia adelante y hacia atras
            int randomNum = ThreadLocalRandom.current().nextInt(0, 10 + 1);
            String query = "add("+ i +", " + (i + 1) + ", " + randomNum + ")";
            String reverseQuery = "add("+ (i + 1) +", " + i + ", " + randomNum + ")";
            Query consult = new Query(query); consult.oneSolution(); consult.close();
            Query reverseConsult = new Query(reverseQuery); reverseConsult.oneSolution(); reverseConsult.close();

            // agrega hacia arriba y hacia abajo, con las excepciones
            if(i <= 9){
                query = "add("+ i +", " + (i + 6) + ", " + randomNum + ")";
                consult = new Query(query); consult.oneSolution(); consult.close();
            }
            else if(i > 29) {
                query = "add("+ i +", " + (i - 6) + ", " + randomNum + ")";
                consult = new Query(query); consult.oneSolution(); consult.close();
            }
            else {
                query = "add("+ i +", " + (i + 6) + ", " + randomNum + ")";
                reverseQuery = "add("+ i +", " + (i - 6)  + ", " + randomNum + ")";
                consult = new Query(query); consult.oneSolution(); consult.close();
                reverseConsult = new Query(reverseQuery); reverseConsult.oneSolution(); reverseConsult.close();
            }
        }
    }
}
