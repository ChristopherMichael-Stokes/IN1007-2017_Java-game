/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semester2java;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author chris
 */
public class HighScore {

    private int score;
    private Map<String, Integer> scores, sortedScores;
    private String name;
    private final JLayeredPane layeredPane;

    public HighScore(JLayeredPane layeredPane) {
        this.layeredPane=layeredPane;

        readScoresFromFile();

    }

    private String askForName() {
        //needs swing code to ask user to input name
        String tempName = "";
        do {
            tempName = JOptionPane.showInternalInputDialog(layeredPane, "Type in your name: ", "title", JOptionPane.QUESTION_MESSAGE);
        } while (!scores.containsKey(tempName) || tempName == null || tempName.matches("/^$|\\s+/"));
        
        return tempName;        
    }

    public void finish() {
        name=askForName();
        scores.put(name, score);
        sortScores();
        outputScoresToFile();

    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Map getSortedScores() {
        return sortedScores;
    }

    private void sortScores() {
        List<Map.Entry<String, Integer>> list
                = new LinkedList<>(scores.entrySet());
        Collections.sort(list,
                (Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)
                -> (o2.getValue()).compareTo(o1.getValue()));
        sortedScores = new LinkedHashMap<>();

        list.forEach((entry) -> {
            sortedScores.put(entry.getKey(), entry.getValue());
        });

    }

    private void readScoresFromFile() {

        try (FileInputStream fis = new FileInputStream("data/scores.txt");
                ObjectInputStream ois = new ObjectInputStream(fis);) {

            scores = (HashMap) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            scores = new HashMap<>();
        } finally {
            if (scores != null) {
                sortScores();
            }
        }

    }

    private void outputScoresToFile() {

        try (FileOutputStream fos = new FileOutputStream("data/scores.txt");
                ObjectOutputStream oos = new ObjectOutputStream(fos);) {

            oos.writeObject(scores);
        } catch (IOException e) {
            System.out.println("Scores will not be saved");
        }

    }

}
