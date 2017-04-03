/*
 * This is the main class, the levels, frame and panels are instantiated here.
 *
 */
package semester2java;

import city.cs.engine.SimulationSettings;
import city.cs.engine.UserView;
import city.cs.engine.World;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import semester2java.Controller.KeyboardHandler;
import semester2java.Levels.Levels;

/**
 * the game class itself.
 * Implements the runnable interface so multiple game objects can be run 
 * simultaneously
 * 
 * @author chris
 */
public class Semester2Java extends SimulationSettings implements Runnable {

    /**
     * the pane that all the panels in the game will be added to
     */
    private JLayeredPane layeredPane;
    /* the world that the game initializes to */
    private World world;
    /**
     * the panels relating to attributes from the player
     */
    private JPanel health, projectilePanel;
    /**
     * the keyboard handler object that will control input
     */
    private final KeyboardHandler kh;
    /**
     * the view of the game
     */
    private final UserView view;
    /**
     * the object that handles levels
     */
    private final Levels levels;
    /**
     * the window that the game will be displayed on
     */
    private JFrame frame;
    /**
     * the resolution of the game window
     */
    private final int resolutionX, resolutionY;

    /**
     * initializes the game, the gui and constructs the levels object.
     *
     * @param resolutionX the horizontal resolution of the game window
     * @param resolutionY the vertical resolution of the game window
     * @param fps the frame rate of the game
     */
    public Semester2Java(int resolutionX, int resolutionY, int fps) {
        super(fps);

        this.resolutionX = resolutionX;
        this.resolutionY = resolutionY;
        // display the view in a frame
        frame = new JFrame("semester2game");
        frame.setSize(resolutionX,resolutionY);
        layeredPane = new JLayeredPane();
        levels = new Levels(layeredPane, resolutionX, resolutionY, frame, fps);
        this.world = levels.getLevel();
        health = levels.getPlayer().getHealthPanel();
        view = levels.getView();
        kh = levels.getKeyboardHandler();
        
        frame.setFocusable(true);
        // quit the application when the game window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.setAutoRequestFocus(true);
        // don't let the game window be resized
        frame.setResizable(false);
    }

    /**
     * get the frame
     * @return the game window
     */
    public JFrame getFrame(){
        return frame;
    }
    
    public void setFrame(JFrame frame){
        this.frame = frame;
        this.frame.setSize(resolutionX,resolutionY);
        this.frame.setFocusable(true);
        // quit the application when the game window is closed
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLocationByPlatform(true);
        this.frame.setAutoRequestFocus(true);
        // don't let the game window be resized
        this.frame.setResizable(false);
        
    }
    
    /**
     * when this is called, the frame will be setup, and all the parts of the 
     * gui will be added to the frame.
     * 
     */
    @Override
    public void run() {
        

        layeredPane.setOpaque(false);
        layeredPane.setPreferredSize(new Dimension(resolutionX, resolutionY));
        
        view.setCentre(levels.getPlayer().getPosition());
        view.setBounds(0,0,resolutionX,resolutionY);
        levels.setView(view);
       
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
        // size the game window to fit the world view
        frame.pack();
        // make the window visible
        frame.setVisible(true);
    }
        
    /**
     * creates a new game object, and runs the game.
     * 
     * @param args command line arguments
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        int horizontal = 1024, vertical = 768;
        
        Semester2Java game = new Semester2Java(horizontal, vertical, 60);
        Thread t1 = new Thread(game);
        
        PreGame pg = new PreGame(game.getFrame(),horizontal,vertical);
        Thread t2 = new Thread(pg);
        t2.start();
        t2.join();
        t1.start();
        
        //System.out.println(game.getFpsAverageCount());
        //System.out.println(Thread.currentThread().getName());
    }

}
