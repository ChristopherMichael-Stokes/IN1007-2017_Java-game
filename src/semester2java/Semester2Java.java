/*
 * This is the main class, the levels, frame and panels are instantiated here.
 * The class also handles simulation steps under a seperate thread
 *
 */
package semester2java;

import city.cs.engine.SimulationSettings;
import city.cs.engine.UserView;
import city.cs.engine.World;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import semester2java.Controller.KeyboardHandler;
import semester2java.Levels.Levels;

/**
 *
 * @author chris
 */
public class Semester2Java extends SimulationSettings {

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

    public Semester2Java(int resolutionX, int resolutionY, int fps) {
        super(fps);

        // display the view in a frame
        frame = new JFrame("semester2game");
        frame.setFocusable(true);

        // quit the application when the game window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);

        layeredPane = new JLayeredPane();
        layeredPane.setOpaque(false);
        layeredPane.setPreferredSize(new Dimension(resolutionX, resolutionY));

        levels = new Levels(layeredPane, resolutionX, resolutionY, this);
        this.world = levels.getLevel();

        view = levels.getView();
        levels.setView(view);
        view.setCentre(levels.getPlayer().getPosition());
        // uncomment this to draw a 1-metre grid over the view
//        view.setGridResolution(1);

        health = levels.getPlayer().getHealthPanel();
        levels.getPlayer().drawPlayerHealth();

        layeredPane.add(health, 0);
        health.setBounds(20, 5, resolutionX - 20, 50);

        projectilePanel = levels.getPlayer().getProjectilePanel();
        levels.getPlayer().drawPlayerShots();

        layeredPane.add(projectilePanel, 0);
        projectilePanel.setBounds(20, health.getHeight() + 5, resolutionX - 20, 50);

        layeredPane.add(view, 1);

//        JLabel background = new JLabel();
//        try {
//            BufferedImage myPicture = ImageIO.read(FOREST_01);
//            background = new JLabel(new ImageIcon(myPicture));
//        } catch (IOException e) {
//            //if the image is missing paint the background blue
//            System.out.println("Background image missing\n" + e);
//            background.setOpaque(true);
//            background.setBackground(Color.CYAN);
//        } finally {
//            layeredPane.add(background, -1);
//            background.setBounds(0, 0, resolutionX, resolutionY);
//        }

        kh = levels.getKeyboardHandler();
        frame.addKeyListener(kh);
        frame.add(layeredPane);
        // don't let the game window be resized
        frame.setResizable(false);
        // size the game window to fit the world view
        frame.pack();
        // make the window visible
        frame.setVisible(true);

//        kh.initialize();
        // uncomment this to make a debugging view
//        JFrame debugView = new DebugViewer(world, resolutionX, resolutionY);
        // start!
    }

    public JFrame getFrame() {
        return frame;
    }

    public static void main(String[] args) throws InterruptedException {
        Semester2Java game = new Semester2Java(1024, 768, 60);
        //System.out.println(game.getFpsAverageCount());
        //System.out.println(Thread.currentThread().getName());
    }

}
