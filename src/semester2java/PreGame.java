/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semester2java;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;

/**
 *
 * @author Christopher
 */
public class PreGame implements Runnable {

    private final JFrame frame;
    private final JLayeredPane layeredPane;

    public PreGame(JFrame frame, int resolutionX, int resolutionY) {
        this.frame = frame;
        layeredPane = new JLayeredPane();
        layeredPane.setOpaque(false);
        layeredPane.setPreferredSize(new Dimension(resolutionX, resolutionY));
    }

    @Override
    public void run() {
        //
    }

}
