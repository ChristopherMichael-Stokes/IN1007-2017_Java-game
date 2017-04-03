/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semester2java;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Christopher
 */
public class PreGame implements Runnable {

    private final JFrame frame;
    private final JPanel panel;
    private final LayoutManager layout;
    private final GridBagConstraints gbc;
    private final JButton play, howToPlay, settings, back;
    private boolean end;

    public PreGame(JFrame frame, int resolutionX, int resolutionY) {
        this.frame = frame;
        layout = new GridBagLayout();
        gbc = new GridBagConstraints();
        play = new JButton("Play");
        howToPlay = new JButton("How To Play");
        settings = new JButton("Settings");
        back = new JButton("Back");
        panel = new JPanel(layout);
    }
    
    private void homeScreen(){
        
        gbc.gridwidth = 3;
        gbc.gridheight = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipadx = 100;
        gbc.ipady = 50;
        panel.add(play, gbc);

        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.ipadx = 50;
        gbc.ipady = 25;
        gbc.gridy = 2;
        panel.add(howToPlay, gbc);

        gbc.gridx = 2;
        panel.add(settings, gbc);
    }

    @Override
    public void run() {
//        panel.add(settings);        

        frame.add(panel);
        frame.setVisible(true);

        play.addActionListener((ActionEvent ae) -> {            
            end = true;
            play.removeActionListener(play.getActionListeners()[0]);
        });
        
        howToPlay.addActionListener((ActionEvent ae) -> {
            
        });
        
        settings.addActionListener((ActionEvent ae) -> {
            
        });
        
        back.addActionListener((ActionEvent ae) -> {
            panel.removeAll();
            homeScreen();
        });


        
        while (!end) {
            Thread.currentThread().interrupt();
        }

        System.out.println("done");
        frame.remove(panel);

    }

}
