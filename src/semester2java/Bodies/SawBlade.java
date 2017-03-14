/*
 * This is a subtype of body, and is designed to rotate in a given direction,
 * it will take two shots to destroy this.
 */
package semester2java.Bodies;

import city.cs.engine.BodyImage;
import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.DynamicBody;
import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import city.cs.engine.SolidFixture;
import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;
import city.cs.engine.World;

/**
 *
 * @author Christopher
 */
public class SawBlade extends DynamicBody implements StepListener, CollisionListener {

    private static final BodyImage image = new BodyImage("data/sawBlade.png", 4);
    private final World world;
    private float angVelocity;
    private final double pi;
    private byte health;

    public SawBlade(World world) {
        super(world);
        this.world = world;
        pi = Math.PI;
        angVelocity = (float) (2 * pi);
        health = 2;
        
        drawSawBlade();
    }

    private void drawSawBlade() {
        Shape bodyShape = new PolygonShape(-1.0f, 1.08f, 0.02f, 1.47f, 1.06f, 1.06f, 1.47f, 0.17f, 1.16f, -0.93f, 0.06f, -1.47f, -1.12f, -1.0f, -1.47f, 0.11f);
        SolidFixture bodyFixture = new SolidFixture(this, bodyShape);
        bodyFixture.setFriction(0.8f);
        bodyFixture.setRestitution(0.7f);
        bodyFixture.setDensity(40);
        addImage(image);
        setName("standardEnemy");

        addCollisionListener(this);
        world.addStepListener(this);
    }

    public float getAngVelocity() {
        return angVelocity;
    }

    public void setAngVelocity(float angVelocity) {
        this.angVelocity = angVelocity;
    }

    @Override
    public void preStep(StepEvent e) {
        //do nothing for now
    }

    @Override
    public void postStep(StepEvent e) {
        setAngularVelocity(angVelocity);
        if (this.getPosition().y < -30f){
            destroy();
            world.removeStepListener(this);
            removeCollisionListener(this);
        }
    }

    @Override
    public void collide(CollisionEvent e) {
        if (e.getOtherBody() instanceof Projectile) {
            e.getOtherBody().destroy();
            if (health > 1) {
                health--;
            } else if (health <= 1) {
                destroy();
                world.removeStepListener(this);
                removeCollisionListener(this);
            }
        }
    }

}
