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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * class used to manage high scores, and read and write from the high score file
 *
 * @author chris
 */
@SuppressWarnings("unchecked")
public class HighScore {

    /**
     * the current score for the game
     */
    private int score;
    /**
     * map of previous scores
     */
    private Map<String, Integer> scores, sortedScores;
    /**
     * the name of the player
     */
    private String name;
    /**
     * the amount of frames that has been run
     */
    private short frames;

    /**
     * constructs high scores, and attempts to read the previous scores from a 
     * file
     */
    public HighScore() {
        frames = 0;
        score = 0;
        sortedScores = new LinkedHashMap<>();
        readScoresFromFile();
    }

    public void resetFrames(){
        this.frames = 0;
    }
    
    /**
     * works out player score after they loose a life
     */
    public void deductLife() {
        score -= 200;
    }
    /**
     * works out player score after they loose a health point
     */
    public void deductHealth(){
        score -= 50;
    }
    
    /**
     * deducts 
     * @param frameRate the simulation rate the game is running at
     */
    public void timePenalty(int frameRate){
        frames++;
        if (frames % frameRate == 0){
            frames = 0;
            score--;
        }       
    }
    
    /**
     * works out the player score after they shoot a small enemy
     */
    public void shotSmallEnemy() {
        score += 50;
    }

    /**
     * works out the player score after they shoot a large enemy
     */
    public void shotLargeEnemy() {
        score += 100;
    }
    
    public void completedLevel(){
        score += 200;
    }

    /**
     * asks the user to input a name, then checks that the name does not already
     * exist in the scoring system, and that the name is not blank.
     *
     * @param panel the panel to display the request
     * @return a validated name inputted by the user
     */
    private String askForName(JPanel panel) {
        //needs swing code to ask user to input name
        String tempName = "";
        String defaultMessage = "You must enter a name: ";
        String message = defaultMessage;
        String title = "Input Name";
        boolean cancelled = false;

        do {
            //ask for input with internal dialog
            tempName = JOptionPane.showInternalInputDialog(panel, message, title, JOptionPane.QUESTION_MESSAGE);

            if (tempName == null) {
                cancelled = true;
                break;
            } else if (tempName.matches("[ ]+")) {
                System.out.println("wrong name");
                message = "Enter a name that is not only spaces";
            } else if (scores.containsKey(tempName)) {
                message = "The name \"" + tempName + "\" exists.  Choose another name: ";
            } else {
                message = defaultMessage;
            }

            //check if name exists or matches regex
        } while ((scores.containsKey(tempName) || tempName.matches("[ ]+")));

        if (cancelled) {
            return null;
        }

        return tempName;
    }

    /**
     * Should be called when the game is over.
     * Will call askForName(), and will display the consolidated list of scores
     * 
     * @param panel the panel in which to display the scores
     */
    public void finish(JPanel panel) {
        name = askForName(panel);
        scores.put(name, score);
        sortScores();
        outputScoresToFile();

        StringBuilder sb = new StringBuilder();
        sb.append("Name:\t\tScore:\n");
        System.out.println("");
        int i = 0;
        for (Map.Entry<String, Integer> entry : sortedScores.entrySet()) {
            sb.append(entry.getKey()).append("\t\t")
                    .append(entry.getValue()).append("\n");
            if (i++ > 20) {
                break;
            }
        }
        System.out.println("");

        JOptionPane.showInternalMessageDialog(panel, new JTextArea(sb.toString()), "HighScores", JOptionPane.YES_NO_OPTION);
    }

    /**
     * gets the current score
     * 
     * @return the current score
     */
    public int getScore() {
        return score;
    }

    /**
     * gets the sorted map of scores
     * 
     * @return the map that has scores sorted in descending order
     */
    public Map getSortedScores() {
        return sortedScores;
    }

    /**
     * takes the score field, and sorts it in descending order, then stores it
     * in the sortedScores field
     */
    private void sortScores() {
        List<Map.Entry<String, Integer>> list
                = new LinkedList<>(scores.entrySet());
        Collections.sort(list,
                (Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)
                -> (o2.getValue()).compareTo(o1.getValue()));

        list.forEach((entry) -> {
            sortedScores.put(entry.getKey(), entry.getValue());
        });

    }

    /**
     * reads previous high scores from a file called "scores.txt". If no file
     * exists, or if the file is not read correctly, the scores will be reset
     */
    private void readScoresFromFile() {

        //use try with resources to close resources at the end
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

    /**
     * writes high scores to file
     */
    private void outputScoresToFile() {

        for (int i = 0; i < 5; i++) {
            try (FileOutputStream fos = new FileOutputStream("data/scores.txt");
                    ObjectOutputStream oos = new ObjectOutputStream(fos);) {

                oos.writeObject(scores);
                break;
            } catch (IOException e) {
                System.out.print("Scores can not be saved");
                if (i< 4) System.out.print(", retrying");
                System.out.println("");
            }
        }

    }

}
