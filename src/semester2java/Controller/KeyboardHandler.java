/*
 *  this class is designed to handle key inputs given while focus is on the 
 *  jframe.  the class also stores bound keys in a tree map, and allows for 
 *  keys to be rebound.
 */
package semester2java.Controller;

import city.cs.engine.World;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import org.jbox2d.common.Vec2;
import semester2java.Bodies.Player;



/**
 *
 * @author chris
 */
public class KeyboardHandler implements KeyListener, ActionListener {

    private Runnable r1;
    private int key;
    private LayoutManager layout;
    private GridBagConstraints gBC;
    private JLabel confirmationLbl, pauseText;
    private JPanel pauseBackground, confirmation;
    private JButton rebindKey, play;
    private final World world;
    private final Player player;
    private final JLayeredPane layeredPane;
    //player bound keys
    private Map<String, Integer> keyBinds;

    public KeyboardHandler(World world, Player player, JLayeredPane layeredPane) {
        this.world = world;
        this.player = player;
        this.layeredPane = layeredPane;
        keyBinds = new TreeMap<>();

        //default key bindings
        keyBinds.put("left", KeyEvent.VK_A);
        keyBinds.put("right", KeyEvent.VK_D);
        keyBinds.put("jump1", KeyEvent.VK_W);
        keyBinds.put("jump2", KeyEvent.VK_SPACE);
        keyBinds.put("pause", KeyEvent.VK_ESCAPE);

        //adding the pause overlay
        layout = new GridBagLayout();
        gBC = new GridBagConstraints();
        pauseBackground = new JPanel(layout);
        buildPauseBackground();
        layeredPane.add(pauseBackground, -1);

        //set the game to pause after the first 500 milleseconds of runtime.
        //this is to allow for the game to fully initialize before it starts.
        r1 = () -> {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    pause();
                }
            }, 250);
        };
    }

    public void initialize() {
        new Thread(r1).start();
    }

    @Override
    public void keyPressed(KeyEvent e) {

        key = e.getKeyCode();
        //global code to change pause state
        if (key == keyBinds.get("pause")) {

            if (world.isRunning()) {
                pause();
            } else {
                unPause();
            }
        }

        //player related code
        if (player.canJump()) { //only move when player is on platform
            if (key == keyBinds.get("left")) {
                player.moveLeft();
            } else if (key == keyBinds.get("right")) {
                player.moveRight();
            }
        }

        if (key == keyBinds.get("jump1") || key == keyBinds.get("jump2")) { //only jump when player is on a platform
            if (player.canJump()) {
                player.jump();
                player.setCanJump(false);
            }
        }

        //if the player is in the air and are moving along the x-axis
        //they can press move in the opposite direction to move slightly
        //in the other direction
        if (!player.canJump()) {
            if (player.getLinearVelocity().x >= 0 && key == keyBinds.get("left")) {
                player.setLinearVelocity(new Vec2(player.getMoveSpeed() / 3f, player.getLinearVelocity().y));
            } else if (player.getLinearVelocity().x <= 0 && key == keyBinds.get("right")) {
                player.setLinearVelocity(new Vec2(-player.getMoveSpeed() / 3f, player.getLinearVelocity().y));
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        key = e.getKeyCode();

        if (key == keyBinds.get("left") || key == keyBinds.get("right")) { //stop moving when player releases move key
            player.stopWalking();

            float velocityX = player.getLinearVelocity().x;
            float velocityY = player.getLinearVelocity().y;
            //if the player is on a platform and stops moving, make the player move
            //slowly in the same direction to simulate deceleration
            if (player.canJump()) {
                player.setLinearVelocity(new Vec2(velocityX / 3, velocityY));
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public int getKey() {
        return key;
    }

    //allow player to be able to bind keys
    public void setKeyBind(String action, int keyNumber) {

        if (keyBinds.containsKey(action)) {

            Optional<String> oldBinding = keyBinds.entrySet().stream()
                    .filter(e -> e.getValue() == keyNumber)
                    .map(Map.Entry::getKey)
                    .findFirst();

            if (oldBinding.isPresent()) {
                //put code here to ask player if they are sure they want
                //to unbind the action bound to key
                String keyName = Character.getName(keyNumber);
                confirmationLbl = new JLabel(keyName
                        + " is already bound to " + oldBinding
                        + ", are you sure you would like to unbind " + keyName);
//                confirmationLbl
                pauseBackground.add(confirmationLbl);
            }

//                String oldBinding = keyBinds.entrySet()
//                                            .stream()
//                                            .filter((Map.Entry<String, Integer> entry) -> {
//                                                entry.getValue().equals(keyNumber);
//                                                return entry.getKey();
//                                            });
        } else {
            System.out.println("action not present");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(play)) {
            unPause();
        } else if (e.getSource().equals(rebindKey)) {
            rk();
        }

    }

    private void rk() {
        layeredPane.moveToBack(pauseBackground);
        pauseBackground.remove(rebindKey);
        gBC.gridy = 2;
        gBC.gridwidth = 2;
        pauseBackground.add(new JLabel(" "), gBC);
        gBC.gridwidth = 1;
        int i = 2;
        for (Map.Entry<String, Integer> entry : keyBinds.entrySet()) {
            gBC.fill = GridBagConstraints.BOTH;
            gBC.gridx = 0;
            gBC.gridy = i + 1;
            pauseBackground.add(new JButton(entry.getKey()), gBC);
            gBC.gridx = 1;
            JFormattedTextField textField = new JFormattedTextField();
            String keyValue;
            int value = entry.getValue();
            if (Character.isAlphabetic(entry.getValue())) {
                keyValue = Character.toString((char) value);
            } else {
                keyValue = Character.getName(entry.getValue());
            }

            textField.setValue(keyValue);
            textField.setColumns(5);
            gBC.fill = GridBagConstraints.VERTICAL;
            pauseBackground.add(textField, gBC);
            i++;
        }
        pauseBackground.revalidate();
        pauseBackground.repaint();
        layeredPane.moveToFront(pauseBackground);
    }

    private void buildPauseBackground() {
        pauseBackground.removeAll();
        pauseBackground.setOpaque(true);
        pauseBackground.setBackground(new Color(135, 135, 135, 225));
        pauseBackground.setBounds(0, 0, 1024, 768);

        pauseText = new JLabel("The game is currently paused");
        pauseText.setFont(new Font(pauseText.getFont().getName(), Font.PLAIN, 20));
        gBC.gridx = 0;
        gBC.gridy = 0;
        gBC.gridwidth = 2;
        pauseBackground.add(pauseText, gBC);

        play = new JButton("Click to resume game");
        play.addActionListener(this);
        gBC.gridx = 0;
        gBC.gridy = 1;
        pauseBackground.add(play, gBC);

        rebindKey = new JButton("Reconfigure Controls");
        rebindKey.addActionListener(this);
        gBC.gridx = 1;
        gBC.gridy = 2;
        gBC.gridwidth = 1;
        pauseBackground.add(rebindKey, gBC);
    }

    private void pause() {
        layeredPane.moveToFront(pauseBackground);
        world.stop();
    }

    private void unPause() {
        world.start();
        //used to build set the background back to its initial state
        buildPauseBackground();
        layeredPane.moveToBack(pauseBackground);
    }

}
