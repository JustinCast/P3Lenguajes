package sample;

import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import org.jpl7.Query;
import org.jpl7.Term;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Controller {
    GridPane buttonsContainer;
    private boolean spider = false;
    private boolean bee = false;
    private ArrayList route = new ArrayList();

    public Controller() throws IOException {
        initializeProlog();
    }

    public void onBtnClick(Event e) {

    }
    public void onStartGameClick(Event e) {
        for(Object o : this.route)
            System.out.println(o);
    }

    private void initializeProlog() throws IOException {
        String PROLOG = "consult('ScriptProlog.pl')";
        Query query = new Query(PROLOG);
        if (query.hasSolution()) {
            addNodes();

            String shortest = "find_paths(0, 3, X)";
            Query q4 = new Query(shortest);
            String a = q4.oneSolution().get("X").toString()
                    .replace('[', ' ')
                    .replace(']', ' ')
                    .replace('|', ' ')
                    .replace('(', ' ')
                    .replace(')', ' ')
                    .replaceAll("'", "")
                    .replaceAll(" ", "")
                    .replaceAll(",", ",");
            System.out.println(a);
            //String[] routeSplitted = route.split(",");
            //this.route.addAll(Arrays.asList(routeSplitted));
        } else {
            JOptionPane.showMessageDialog(null, "!!!No se pudo conectar con Prolog!!!");
        }
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
