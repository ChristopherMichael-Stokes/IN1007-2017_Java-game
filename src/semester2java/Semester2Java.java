/*
 * This is the main class, the levels, frame and panels are instantiated here.
 *
 */
package semester2java;

import city.cs.engine.SimulationSettings;
import city.cs.engine.UserView;
import city.cs.engine.World;
import java.awt.Dimension;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import semester2java.Controller.KeyboardHandler;
import semester2java.Levels.Levels;

/**
 *
 * @author chris
 */
public class Semester2Java extends SimulationSettings implements Runnable{

    public JLayeredPane layeredPane;
    private World world;
    private JPanel health, projectilePanel;
    private final KeyboardHandler kh;
    private final UserView view;
    private final Levels levels;
    private final JFrame frame;
    private static final File FOREST_01 = new File("data/forestBackground01.jpg");
    private static final File FOREST_02 = new File("data/forestBackground02.jpg");
    private static final File FOREST_03 = new File("data/forestBackground03.jpg");
    private final int resolutionX, resolutionY;

    public Semester2Java(int resolutionX, int resolutionY, int fps) {
        super(fps);

        this.resolutionX=resolutionX;
        this.resolutionY=resolutionY;
        // display the view in a frame
        frame = new JFrame("semester2game");
        layeredPane = new JLayeredPane();
        levels = new Levels(layeredPane, resolutionX, resolutionY, this);
        this.world = levels.getLevel();
        health = levels.getPlayer().getHealthPanel();
        view = levels.getView();
        kh = levels.getKeyboardHandler();
        
        

    }

    public JFrame getFrame() {
        return frame;
    }
    
    @Override
    public void run() {
        frame.setFocusable(true);

        // quit the application when the game window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.setAutoRequestFocus(true);

        
        layeredPane.setOpaque(false);
        layeredPane.setPreferredSize(new Dimension(resolutionX, resolutionY));

        

        
        levels.setView(view);
        view.setCentre(levels.getPlayer().getPosition());
        // uncomment this to draw a 1-metre grid over the view
//        view.setGridResolution(1);

        
        levels.getPlayer().drawPlayerHealth();

        layeredPane.add(health, 0);
        health.setBounds(20, 5, resolutionX - 20, 50);

        projectilePanel = levels.getPlayer().getProjectilePanel();
        levels.getPlayer().drawPlayerShots();

        layeredPane.add(projectilePanel, 0);
        projectilePanel.setBounds(20, health.getHeight() + 5, resolutionX - 20, 50);

        layeredPane.add(view, 1);

        
        frame.addKeyListener(kh);
        frame.add(layeredPane);
        // don't let the game window be resized
        frame.setResizable(false);
        // size the game window to fit the world view
        frame.pack();
        // make the window visible
        frame.setVisible(true);
        
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread((new Semester2Java(1024, 768, 60)));
        t1.start();
        //System.out.println(game.getFpsAverageCount());
        //System.out.println(Thread.currentThread().getName());
    }

    

}
