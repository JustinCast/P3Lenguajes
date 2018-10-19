package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import org.jpl7.Query;

import javax.swing.*;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

public class Controller {
    @FXML GridPane buttonsContainer;
    @FXML
    ToggleButton selectBlocks;
    @FXML
    ToggleButton selectBeginEnd;
    @FXML
    Button playButton;
    private ArrayList<String> allRoutes = new ArrayList<>();
    private ArrayList route = new ArrayList();
    private ArrayList blocks = new ArrayList();
    @FXML
    private Button begin;
    @FXML
    private Button end;
    @FXML
    private Button prev;
    @FXML
    private ProgressBar progressBar;
    private final ToggleGroup selectGroup = new ToggleGroup();
    private Timer timer;
    public Controller() throws IOException {
    }

    private void config() {
        this.selectBlocks.setToggleGroup(this.selectGroup);
        this.selectBeginEnd.setToggleGroup(this.selectGroup);
    }

    public void onBtnClick(Event e) {
        config();
        String id = ((Button) e.getSource()).getText();
        if(this.selectBlocks.isSelected()){
            if(!this.blocks.contains(Integer.parseInt(id))) {
                this.blocks.add(Integer.parseInt(id));
                setBlockStyle((Button) e.getSource());
            }else {
                int index = this.blocks.indexOf(Integer.parseInt(id));
                this.blocks.remove(index);
                ((Button) e.getSource()).setStyle(null);
            }
        }
        else if(this.selectBeginEnd.isSelected()){
            if(this.begin == null)
            {
                this.begin = ((Button) e.getSource());
                setSpiderStyle(begin);
                return;
            }
            this.end = ((Button) e.getSource());
            setBeeStyle(end);
            this.playButton.setDisable(false);
        }
        else JOptionPane.showMessageDialog(null, "Select 'Select Blocks' Button first");
    }

    private void setSpiderStyle(Button b) {
        b.setStyle("" +
                "-fx-border-color:red; -fx-background-repeat:no-repeat; " +
                "-fx-color: transparent;" +
                "-fx-background-position: center;" +
                "-fx-background-image: url('./assets/spider.png'); " +
                "-fx-background-size: 30%");
    }

    private void setBeeStyle(Button b) {
        b.setStyle("" +
                "-fx-border-color:red; -fx-background-repeat:no-repeat; " +
                "-fx-color: transparent;" +
                "-fx-background-position: center;" +
                "-fx-background-image: url('./assets/bee.png'); " +
                "-fx-background-size: 30%");
    }

    private void setTrophyStyle(Button b) {
        b.setStyle("" +
                "-fx-border-color:red; -fx-background-repeat:no-repeat; " +
                "-fx-color: transparent;" +
                "-fx-background-position: center;" +
                "-fx-background-image: url('./assets/trophy.png'); " +
                "-fx-background-size: 30%");
    }

    private void setBlockStyle(Button b) {
        b.setStyle("-fx-border-color:red; -fx-background-repeat:no-repeat; " +
                "-fx-color: transparent;" +
                "-fx-background-position: center;" +
                "-fx-background-image: url('./assets/block.png'); " +
                "-fx-background-size: 100%");
    }

    public void onStartGameClick(Event e) throws IOException {
        initializeProlog();
        timer = new Timer();
        progressBar.setProgress(0);
        timer.schedule(new TimerTask() {
            int period = 0;

            @Override
            public void run() {
                progressBar.setProgress((double) period/4);
                if(period == 4) {
                    try {
                        cleanPrologBD();
                        route.clear();
                        changePosition();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    period = 0;
                }
                else {
                    playGame();
                }

                period++;
            }
        }, 500, 500);
    }

    private void resetGame() {
        this.buttonsContainer.getChildren().forEach((node -> {
            node.setStyle(null);
        }));
        this.begin = null; this.end = null;
        this.timer.cancel();
        this.playButton.setDisable(true);
        this.route.clear();
        this.allRoutes.clear();
        this.progressBar.setProgress(0);
    }

    private void changePosition() throws IOException {
        Random rand = new Random();
        int pickedNumber = rand.nextInt(35) + 1;
        int a = Integer.parseInt(prev.getText());
        int b = Integer.parseInt(end.getText());
        while(blocks.contains(pickedNumber) || pickedNumber == a || pickedNumber == b)
            pickedNumber = rand.nextInt(35) + 1;
        end.setStyle(null);
        end = (Button) buttonsContainer.getChildren().get(pickedNumber);
        setBeeStyle(end);
        begin = (Button) buttonsContainer.getChildren().get(buttonsContainer.getChildren().indexOf(prev));
        initializeProlog();
    }

    private void playGame() {
        for (Object aRoute : this.route) {
            for (Node n : this.buttonsContainer.getChildren()) {
                if (((Button)n).getText().equals(aRoute)) {
                    if(this.prev != null)
                        this.prev.setStyle(null);
                    if(((Button)n).getText().equals(this.end.getText())){
                        setTrophyStyle((Button) n);
                        JOptionPane.showMessageDialog(null, "You win");
                        resetGame();
                        return;
                    }
                    setSpiderStyle((Button) n);
                    this.route.remove(aRoute);
                    this.prev = (Button) n;
                    return;
                }
            }
        }
    }

    private void cleanPrologBD() {
        String PROLOG = "consult('ScriptProlog.pl')";
        Query query = new Query(PROLOG);
        if (query.hasSolution()) {
            String clean = "cleanBD(X, Y, W)";
            Query cleanQuery = new Query(clean);
            cleanQuery.oneSolution();
            cleanQuery.close();
        }
    }

    private void initializeProlog() throws IOException {
        String PROLOG = "consult('ScriptProlog.pl')";
        Query query = new Query(PROLOG);
        if (query.hasSolution()) {
            addNodes();
            System.out.println("INICIO: " + this.begin.getText());
            String shortest = "go("+ Integer.parseInt(this.begin.getText()) +"," + Integer.parseInt(this.end.getText()) + ","+ "X)";
            Query q4 = new Query(shortest);
            while(q4.hasNext()) {
                this.allRoutes.add(regexedString(q4.nextSolution().get("X").toString()));
                //System.out.println(this.allRoutes.get(cont));
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
        this.allRoutes.clear();
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
                else if(i == 1 || i == 2 || i == 3 || i == 4){
                    if(!this.blocks.contains(i - 1)) {
                        System.out.println(leftQuery);
                        consult = new Query(leftQuery);
                        consult.oneSolution();
                        consult.close();
                    }
                    if(!this.blocks.contains(i + 6)) {
                        System.out.println(downQuery);
                        consult = new Query(downQuery);
                        consult.oneSolution();
                        consult.close();
                    }
                    if(!this.blocks.contains(i + 1)) {
                        System.out.println(rightQuery);
                        consult = new Query(rightQuery);
                        consult.oneSolution();
                        consult.close();
                    }
                }
                else if(i == upperRightCorner) {
                    if(!this.blocks.contains(i - 1)) {
                        consult = new Query(leftQuery);
                        consult.oneSolution();
                        consult.close();
                    }
                    if(!this.blocks.contains(i + 6)) {
                        consult = new Query(downQuery);
                        consult.oneSolution();
                        consult.close();
                    }
                }
                else if(i == lowerLeftCorner){
                    if(!this.blocks.contains(i - 6)){
                        consult = new Query(upQuery);
                        consult.oneSolution();
                        consult.close();
                    }
                    if(!this.blocks.contains(i + 1)) {
                        consult = new Query(rightQuery);
                        consult.oneSolution();
                        consult.close();
                    }
                }
                else if(i == lowerRightCorner) {
                    if(!this.blocks.contains(i - 1)) {
                        consult = new Query(leftQuery);
                        consult.oneSolution();
                        consult.close();
                    }
                    if(!this.blocks.contains(i - 6)){
                        consult = new Query(upQuery);
                        consult.oneSolution();
                        consult.close();
                    }
                }
                else if(i == 6 || i == 12 || i == 18 || i == 24){
                    if(!this.blocks.contains(i - 6)){
                        consult = new Query(upQuery);
                        consult.oneSolution();
                        consult.close();
                    }
                    if(!this.blocks.contains(i + 6)) {
                        consult = new Query(downQuery);
                        consult.oneSolution();
                        consult.close();
                    }
                    if(!this.blocks.contains(i+1)) {
                        consult = new Query(rightQuery);
                        consult.oneSolution();
                        consult.close();
                    }
                }
                else if(i == 11 || i == 17 || i == 23 || i == 29){
                    if(!this.blocks.contains(i - 1)) {
                        consult = new Query(leftQuery);
                        consult.oneSolution();
                        consult.close();
                    }
                    if(!this.blocks.contains(i - 6)){
                        consult = new Query(upQuery);
                        consult.oneSolution();
                        consult.close();
                    }
                    if(!this.blocks.contains(i + 6)) {
                        consult = new Query(downQuery);
                        consult.oneSolution();
                        consult.close();
                    }
                }
                else if(i == 31 || i == 32 || i == 33 || i == 34) {
                    if(!this.blocks.contains(i - 1)) {
                        consult = new Query(leftQuery);
                        consult.oneSolution();
                        consult.close();
                    }
                    if(!this.blocks.contains(i - 6)){
                        consult = new Query(upQuery);
                        consult.oneSolution();
                        consult.close();
                    }
                    if(!this.blocks.contains(i+1)) {
                        consult = new Query(rightQuery);
                        consult.oneSolution();
                        consult.close();
                    }
                }
                else {
                    if(!this.blocks.contains(i - 1)) {
                        consult = new Query(leftQuery);
                        consult.oneSolution();
                        consult.close();
                    }
                    if(!this.blocks.contains(i - 6)){
                        consult = new Query(upQuery);
                        consult.oneSolution();
                        consult.close();
                    }
                    if(!this.blocks.contains(i + 6)) {
                        consult = new Query(downQuery);
                        consult.oneSolution();
                        consult.close();
                    }
                    if(!this.blocks.contains(i+1)) {
                        consult = new Query(rightQuery);
                        consult.oneSolution();
                        consult.close();
                    }
                }
            }
        }
    }
}
