/*
 * The worm is an ai that is supposed to attract towards the player and 
 * inflict damage.  It can only be destroyed by a projectile and may grant 
 * health upon death.
 */
package semester2java.Bodies.AIBodies;

import city.cs.engine.Body;
import city.cs.engine.BodyImage;
import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.DynamicBody;
import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import city.cs.engine.SolidFixture;
import city.cs.engine.Walker;
import city.cs.engine.World;
import java.util.ArrayList;
import java.util.List;
import org.jbox2d.common.Vec2;
import semester2java.Bodies.Player;
import semester2java.Bodies.Projectile;

/**
 *
 * @author chris
 */
public class Worm extends Walker implements CollisionListener {

    private final World world;
    private Player player;
    private Vec2 playerPosition;
    private int health;
    //if random number is greater than threshold, the worm object will drop
    //health upon being shot.
    private double threshold;
    private double dropHealthChance;
    private static final BodyImage WORM_IMAGE = new BodyImage("data/worm.png", 2.5f);
    private static final BodyImage HEART_IMAGE = new BodyImage("data/appleImageFull.png", 2);

    public Worm(World world) {
        super(world);
        this.world = world;
        dropHealthChance = Math.random();
        threshold = 0.8f;
        health = 1;
        drawWorm();
    }

    private void drawWorm() {
        List<PolygonShape> shapes = new ArrayList<>();
        shapes.add(new PolygonShape(-0.737f, 0.072f, -0.744f, -0.241f, -0.706f, -0.597f, -0.337f, -0.941f, -0.187f, -0.409f, -0.206f, 0.184f));
        shapes.add(new PolygonShape(-0.337f, -0.941f, -0.187f, -0.409f, 0.241f, -0.659f, 0.334f, -1.209f));
        shapes.add(new PolygonShape(0.241f, -0.659f, 0.334f, -1.209f, 0.697f, -1.228f, 1.109f, -1.003f, 0.697f, -0.684f));
        shapes.add(new PolygonShape(1.109f, -1.003f, 0.697f, -0.684f, 0.691f, -0.134f, 0.903f, -0.003f, 1.109f, -0.147f, 1.241f, -0.516f));
        shapes.add(new PolygonShape(-0.097f, 0.772f, -0.034f, 0.403f, -0.216f, 0.184f, -0.734f, 0.072f, -1.122f, 0.309f, -1.191f, 0.609f, -0.966f, 0.891f, -0.397f, 0.953f));
        shapes.forEach(s -> new SolidFixture(this, s).setFriction(1));
        addImage(WORM_IMAGE);
        this.setName("standardEnemy");
        this.setPosition(new Vec2(0, 0));
        this.setAngle(-(float) Math.PI / 5);

        this.addCollisionListener(this);
    }

    public void addWormAI(Player player) {
        //used to add pathing to the worm to allow it to track the player
        this.player = player;
        this.playerPosition = player.getPosition();

    }

    public boolean healthDrop() {
//        System.out.println(dropHealthChance+"\t"+threshold);
        return dropHealthChance > threshold;
    }

    public void setHealthDrop(boolean dropHealth) {
        if (dropHealth) {
            Shape heartShape = new PolygonShape(0.741f, 0.477f, 0.935f, -0.04f, 0.548f, -0.844f, 0.33f, -0.981f, -0.38f, -0.988f, -0.723f, -0.695f, -0.953f, 0.016f, -0.66f, 0.508f);
            Body heart = new DynamicBody(world);
            SolidFixture heartFixture = new SolidFixture(heart, heartShape);
            heart.setName("1health");
            heart.addImage(HEART_IMAGE);
            heart.setPosition(this.getPosition());
            this.destroy();
        } else {
            this.destroy();
        }

    }

    public double getDropHealthChance() {
        return dropHealthChance;
    }

    public void setDropHealthChance(double dropHealthChance) {
        this.dropHealthChance = dropHealthChance;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public int getHealth() {
        return health;
    }

    public void decrementHealth() {
        health--;
    }

    @Override
    public void collide(CollisionEvent e) {
        if (e.getOtherBody() instanceof Projectile) {
            e.getOtherBody().destroy();
            if (health > 0) {
                decrementHealth();
            } else {
                //if using this, worm will always drop health
                setHealthDrop(true);

                //if using this, worm will drop health 80% of the time
//                setHealthDrop(healthDrop());
            }
        }
    }

}
