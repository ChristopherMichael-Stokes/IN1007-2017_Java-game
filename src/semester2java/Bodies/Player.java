/*
 * this class represents the player object, which extends the walker class
 * and has its own image.
 */
package semester2java.Bodies;

import semester2java.Bodies.AIBodies.Worm;
import city.cs.engine.BodyImage;
import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import city.cs.engine.SolidFixture;
import city.cs.engine.Walker;
import city.cs.engine.World;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jbox2d.common.Vec2;
import semester2java.Levels.Event.EndGame;

/**
 *
 * @author Christopher
 */
public class Player extends Walker implements CollisionListener {

    private static final BodyImage image = new BodyImage("data/owl.png", 2);
    private static final BodyImage jump1 = new BodyImage("data/owlJump1.gif", 2);
    private static final BodyImage jump2 = new BodyImage("data/owlJump2.gif", 2);

    private static final ImageIcon fH = new ImageIcon(new ImageIcon("data/appleImageFull.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
    private static final ImageIcon hH = new ImageIcon(new ImageIcon("data/appleImageHalf.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
    private static final ImageIcon eH = new ImageIcon(new ImageIcon("data/appleImageEmpty.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));

    private static final ImageIcon projectileIcon = new ImageIcon(new ImageIcon("data/pumpkinseed.png").getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
    public SolidFixture leftFootFixture, rightFootFixture;
    private int hearts, shots, shotSpeed;
    private String defaultHealth, health;
    private float moveSpeed;
    private Vec2 jump;
    private boolean canJump; //should only evaluate to true when a player is on a surface
    private Projectile playerProjectile;
    private final EndGame endGame = new EndGame();
    private JPanel healthPanel, projectilePanel;

    public Player(World world) {
        super(world);
        drawPlayer();
        hearts = 3;
        defaultHealth = "";
        for (int i = 0; i < hearts; i++) {
            //health is stored as a string where each heart has three states
            //empty(0), half(1), full(2)
            defaultHealth += "2";
        }
//        System.out.println(defaultHealth);
        health = defaultHealth;
        shots = 10;
        shotSpeed = 750;
        moveSpeed = 10;
        jump = new Vec2(0, 15000);
        canJump = false;
        projectilePanel = new JPanel();
        projectilePanel.setLayout(new BoxLayout(projectilePanel, BoxLayout.LINE_AXIS));
        projectilePanel.setBackground(new Color(0, 0, 0, 0));
        healthPanel = new JPanel();
        healthPanel.setLayout(new BoxLayout(healthPanel, BoxLayout.LINE_AXIS));        
        healthPanel.setBackground(new Color(0, 0, 0, 0));
    }

    private void drawPlayer() {
        //create fixtures to model player and set the correct physical properties
        Shape playerShape = new PolygonShape(-0.065f, 1.007f, -0.653f, 0.744f, -0.889f, 0.303f, -0.489f, -0.689f, -0.014f, -0.826f, 0.544f, -0.651f, 0.885f, 0.316f, 0.632f, 0.774f);
        SolidFixture bodyFixture = new SolidFixture(this, playerShape);
        bodyFixture.setDensity(10);
        changeFriction(bodyFixture);

        Shape rightWing = new PolygonShape(0.885f, 0.303f, 0.985f, 0.221f, 1.015f, 0.125f, 1.012f, -0.121f, 0.875f, -0.426f, 0.708f, -0.569f, 0.537f, -0.621f);
        SolidFixture rightWingFixture = new SolidFixture(this, rightWing);
        changeFriction(rightWingFixture);
        rightWingFixture.setRestitution(0.4f); //give slight restitution to wings to allow player to bounce off wall and not slide up it

        Shape leftWing = new PolygonShape(-0.879f, 0.309f, -0.961f, 0.251f, -1.022f, 0.142f, -1.029f, 0.005f, -0.968f, -0.248f, -0.875f, -0.415f, -0.718f, -0.566f, -0.523f, -0.621f);
        SolidFixture leftWingFixture = new SolidFixture(this, leftWing);
        changeFriction(leftWingFixture);
        leftWingFixture.setRestitution(0.4f);

        Shape leftFoot = new PolygonShape(-0.356f, -0.764f, -0.376f, -0.874f, -0.342f, -0.976f, -0.294f, -0.997f, -0.106f, -0.997f, -0.038f, -0.949f, -0.031f, -0.815f);
        leftFootFixture = new SolidFixture(this, leftFoot);
        changeFriction(leftFootFixture);
        leftFootFixture.setRestitution(0.05f);

        Shape rightFoot = new PolygonShape(0.352f, -0.75f, 0.397f, -0.832f, 0.386f, -0.935f, 0.325f, -0.997f, 0.116f, -0.997f, 0.051f, -0.935f, 0.048f, -0.819f);
        rightFootFixture = new SolidFixture(this, rightFoot);
        changeFriction(rightFootFixture);
        rightFootFixture.setRestitution(0.05f);

        addImage(image);
        this.setName("player");
        this.addCollisionListener(this);
    }

    // assigners/mutators
    private void changeFriction(SolidFixture sf) {
        sf.setFriction(0.9f);
    }

    public void decrementHalfHeart() {
        for (int i = 0; i < health.length(); i++) {
            //convert each character of health string to an integer
            int temp = Integer.parseInt(Character.toString(health.charAt(i)));
            if (temp > 0) {
                temp--;
                if (i == health.length() - 1) {
                    health = health.substring(0, i) + temp;
                    if (temp == 0) {
                        //fire endgame event as player is on 0 health
                        endGame.endGame();
//                        System.out.println("end");

                    }
                } else {
                    health = health.substring(0, i) + temp + health.substring(i + 1);
                }
                break;
            }
        }
    }

    public void incrementHalfHeart() {
        for (int i = health.length() - 1; i >= 0; i--) {
            int temp = Integer.parseInt(Character.toString(health.charAt(i)));
            if (temp < 2) {
                temp++;
                System.out.println("health" + temp);
                if (i == 0) {
                    health = temp + health.substring(i + 1);
                } else {
                    health = health.substring(0, i) + temp + health.substring(i + 1);
                }
                System.out.println(health);
                break;
            }

        }

    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public int getHearts() {
        return hearts;
    }

    public String getDefaultHealth() {
        return defaultHealth;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public void setHealthPanel(JPanel healthPanel) {
        this.healthPanel = healthPanel;
        
    }
    public JPanel getHealthPanel(){
        return healthPanel;
    }

    public void drawPlayerHealth() {
        healthPanel.removeAll();
        JLabel fullHeart = new JLabel(fH);
        JLabel halfHeart = new JLabel(hH);
        JLabel emptyHeart = new JLabel(eH);

        JLabel[] heartTypes = new JLabel[]{emptyHeart, halfHeart, fullHeart};
        JLabel[] drawHearts = new JLabel[this.getHearts()];
        for (int i = 0; i < this.getHearts(); i++) {
            //place the correct label at each position of the array
            drawHearts[(this.getHearts() - 1) - i] = heartTypes[Integer.parseInt(this.health.substring(i, i + 1))];
        }

        for (JLabel drawHeart : drawHearts) {
            healthPanel.add(new JLabel(drawHeart.getIcon()));
            healthPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        }
//        for (JLabel label : drawHearts) {
//            System.out.println(label);
//        }
//        System.out.println("");

//        System.out.println(healthDisplay.getComponentCount());
    }

    public int getShots() {
        return shots;
    }

    public void decrementShots() {
        shots--;
    }

    public int getShotSpeed() {
        return shotSpeed;
    }

    public void setShotSpeed(int shotSpeed) {
        this.shotSpeed = shotSpeed;
    }

    public JPanel getProjectilePanel() {
        return projectilePanel;
    }

    public void drawPlayerShots() {
        projectilePanel.removeAll();

        for (int i = 0; i < shots; i++) {
            projectilePanel.add(new JLabel(projectileIcon));
        }
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void moveLeft() {
        this.startWalking(-moveSpeed);
    }

    public void moveRight() {
        this.startWalking(moveSpeed);
    }

    public void setStopWalking() {
        this.stopWalking();
    }

    public void setJumpHeight(float jumpHeight) {
        jump = new Vec2(jump.x, jumpHeight);
    }

    public float getJumpHeight() {
        return jump.y;
    }

    public void jump() {
        this.applyForce(jump);
        this.removeAllImages();
        this.addImage(jump1);
    }

    public void jumpDown() {
        this.removeAllImages();
        this.addImage(jump2);
    }

    public boolean canJump() {
        return canJump;
    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    public void setDefaultImage() {
        this.removeAllImages();
        this.addImage(image);
    }

    public void setPlayerProjectile(Projectile playerProjectile) {
        this.playerProjectile = playerProjectile;
    }

    public Projectile getPlayerProjectile() {
        return playerProjectile;
    }

    SolidFixture prevCollision;

    //players collision listener
    @Override
    public void collide(CollisionEvent e) {
        //player should only be able to be able to jump when its feet is on a
        //platform.  Or when the player is touching a platform with its wings and
        //the last collision was with its feet.  This effectively allows the 
        //player to wall jump.

        if ((e.getReportingFixture().equals(leftFootFixture)
                || e.getReportingFixture().equals(rightFootFixture))
                || (prevCollision.equals(leftFootFixture)
                || prevCollision.equals(rightFootFixture))) {
//            System.out.println(e.getReportingFixture());
            if (!(e.getOtherBody() instanceof Projectile
                    || e.getOtherBody() instanceof SpikedBarrel
                    || e.getOtherBody() instanceof Worm)) {
                canJump = true;
            }

        }
        if (e.getOtherBody() instanceof Worm) {
            Vec2 temp1 = this.getPosition();
            Vec2 temp2 = e.getOtherBody().getPosition();
            Vec2 difference = temp2.sub(temp1);
            difference.normalize();
            this.applyImpulse(difference.mul(-250f));
            decrementHalfHeart();

        } else if (e.getOtherBody().getName() != null) {
            if (e.getOtherBody().getName().equals("standardEnemy")) {
                decrementHalfHeart();
                e.getOtherBody().destroy();
            } else if (e.getOtherBody().getName().equals("1health")) {
                e.getOtherBody().destroy();
                for (int i = 0; i < 2; i++) {
                    this.incrementHalfHeart();
                }
            }

        }

        prevCollision = e.getReportingFixture();

    }
}
