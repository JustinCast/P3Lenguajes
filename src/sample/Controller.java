package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.jpl7.Query;
import org.jpl7.Term;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Controller {
    @FXML GridPane buttonsContainer;
    private boolean spider = false;
    private boolean bee = false;
    private ArrayList<String> allRoutes = new ArrayList<>();
    private ArrayList route = new ArrayList();
    private ArrayList blocks = new ArrayList();

    public Controller() throws IOException {
        //initializeProlog();
    }

    public void onBtnClick(Event e) {
        String id = ((Button) e.getSource()).getText();
        System.out.println(id);
        if(!this.blocks.contains(id)) {
            this.blocks.add(Integer.parseInt(id));
            ((Button) e.getSource()).setDisable(true);
        }
    }
    public void onStartGameClick(Event e) throws IOException {
        initializeProlog();
        for (Object aRoute : this.route) {
            for (Node n : this.buttonsContainer.getChildren()) {
                //System.out.println(aRoute);
                if (((Button)n).getText().equals(aRoute))
                    n.setStyle("-fx-border-color:red; -fx-background-color: blue;");
            }
            //System.out.println("Indice: " + s);
            System.out.println("ROUTE: " + aRoute);
        }
    }

    private void initializeProlog() throws IOException {
        String PROLOG = "consult('ScriptProlog.pl')";
        Query query = new Query(PROLOG);
        if (query.hasSolution()) {
            addNodes();

            String shortest = "find_paths(0, 21, X)";
            Query q4 = new Query(shortest);
            int cont = 0;
            while(q4.hasNext()) {
                this.allRoutes.add(regexedString(q4.nextSolution().get("X").toString()));
                //System.out.println(this.allRoutes.get(cont));
                if(cont == 20000)
                    break;
                cont++;
            }

            getBestRoute(this.allRoutes);
        } else {
            JOptionPane.showMessageDialog(null, "!!!No se pudo conectar con Prolog!!!");
        }
    }

    private void getBestRoute(ArrayList<String> allRoutes) {
        String best = allRoutes.get(0);
        for(String s : allRoutes)
            if(s.length() < best.length())
                best = s;
        this.route.addAll(Arrays.asList(best.split(",")));
    }

    private String regexedString(String s) {
        return s.replace('[', ' ')
                .replace(']', ' ')
                .replace('|', ' ')
                .replace('(', ' ')
                .replace(')', ' ')
                .replaceAll("'", "")
                .replaceAll(" ", "")
                .replaceAll(",", ",");
    }

    private void addNodes() {
        // corners
        int upperLeftCorner = 0;
        int upperRightCorner = 5;
        int lowerLeftCorner = 30;
        int lowerRightCorner = 35;

        String leftQuery;
        String rightQuery;
        String upQuery;
        String downQuery;
        Query consult;
        for(int i = 0; i <= 35; i++) {

            rightQuery = "add(" + i + ", " + (i + 1) + ", " + 1 + ")";
            leftQuery = "add(" + i + ", " + (i - 1) + ", " + 1 + ")";
            downQuery = "add(" + i + ", " + (i + 6) + ", " + 1 + ")";
            upQuery = "add(" + i + ", " + (i - 6) + ", " + 1 + ")";

            if(!this.blocks.contains(i)){
                if(i == upperLeftCorner) {
                    if(!this.blocks.contains(i+1)) {
                        consult = new Query(rightQuery);
                        consult.oneSolution();
                        consult.close();
                    }
                    if(!this.blocks.contains(i + 6)) {
                        consult = new Query(downQuery);
                        consult.oneSolution();
                        consult.close();
                    }
                }
                else if(i == upperRightCorner) {
                    consult = new Query(leftQuery); consult.oneSolution(); consult.close();
                    consult = new Query(downQuery); consult.oneSolution(); consult.close();
                }
                else if(i == lowerLeftCorner){
                    consult = new Query(upQuery); consult.oneSolution(); consult.close();
                    consult = new Query(rightQuery); consult.oneSolution(); consult.close();
                }
                else if(i == lowerRightCorner) {
                    consult = new Query(leftQuery); consult.oneSolution(); consult.close();
                    consult = new Query(upQuery); consult.oneSolution(); consult.close();
                }
                else if(i < upperRightCorner){
                    consult = new Query(leftQuery); consult.oneSolution(); consult.close();
                    consult = new Query(downQuery); consult.oneSolution(); consult.close();
                    consult = new Query(rightQuery); consult.oneSolution(); consult.close();
                }
                else if(i == 6 || i == 12 || i == 18 || i == 24){
                    consult = new Query(upQuery); consult.oneSolution(); consult.close();
                    consult = new Query(downQuery); consult.oneSolution(); consult.close();
                    consult = new Query(rightQuery); consult.oneSolution(); consult.close();
                }
                else if(i == 11 || i == 17 || i == 23 || i == 29){
                    consult = new Query(leftQuery); consult.oneSolution(); consult.close();
                    consult = new Query(upQuery); consult.oneSolution(); consult.close();
                    consult = new Query(downQuery); consult.oneSolution(); consult.close();
                }
                else if(i == 31 || i == 32 || i == 33 || i == 34) {
                    consult = new Query(leftQuery); consult.oneSolution(); consult.close();
                    consult = new Query(upQuery); consult.oneSolution(); consult.close();
                    consult = new Query(rightQuery); consult.oneSolution(); consult.close();
                }
                else {
                    consult = new Query(leftQuery); consult.oneSolution(); consult.close();
                    consult = new Query(upQuery); consult.oneSolution(); consult.close();
                    consult = new Query(downQuery); consult.oneSolution(); consult.close();
                    consult = new Query(rightQuery); consult.oneSolution(); consult.close();
                }
            }
        }
    }
}
