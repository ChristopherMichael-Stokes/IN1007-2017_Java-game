/*
 * this class is a subtype of Level, so it is also a subtype of world.  All the
 * bodies unique to the level are set here.
 *
 */
package semester2java.Levels.levels;

import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.Shape;
import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;
import city.cs.engine.World;
import org.jbox2d.common.Vec2;
import semester2java.Bodies.AIBodies.Worm;
import semester2java.Bodies.SawBlade;
import semester2java.Levels.Level;
import static semester2java.Levels.Level.getFrictionCoefficient;
import static semester2java.Levels.Level.getTextureLocation;

/**
 *
 * @author Christopher
 */
public final class Level2 extends Level implements StepListener, CollisionListener {

    private final Vec2 start;
    private static final BodyImage W3
            = new BodyImage(getTextureLocation(Level.Textures.WOOD_01), 15);
    private int numSteps;
    private float sawBladeXPos;

    public Level2() {
        super();
        start = new Vec2(0, -11.5f);
        numSteps = 0;
        initializeLevel();
    }

    @Override
    protected void initializeLevel() {
        float pi = (float) Math.PI;

        Shape pf0Shape = new BoxShape(2.5f, 5.5f);
        setBody(true, "0Shape", pf0Shape, start, 0);

        start.x += 4.5f;
        start.y -= 5f;
        Shape shape = new BoxShape(3, 0.5f);
        setBody(true, "start", shape, start, 0);

        start.x += 20f;
        start.y += 5f;
        Shape pf1 = new BoxShape(8f, 0.5f);
        setBody(true, "platform1", pf1, start, pi / 6);

        start.x += 13f;
        start.y += 7.5f;
        setBody(true, "platform2", pf1, start, pi / 6);
        sawBladeXPos = start.x + 5;

        start.x += 13.5f;
        start.y += 3.15f;
        setBody(true, "platform3", pf1, start, 0);
        new Worm((World)this).putOn(getBody("platform3"));

        start.x += 15f;
        setBody(true, "platform4", shape, start, 0);
        getBody("platform4").setName("end");
        setBackground(Level.Backgrounds.FOREST_BACKGROUND_01);

        changeFriction(getFrictionCoefficient(Level.FrictionCoefficient.WOOD));
        getBodies().forEach((k, v) -> {
            v.setClipped(true);
            v.addImage(W3);
        });

        addStepListener(this);
    }

    public void spawnSawBlades(boolean spawn) {
        if (spawn) {
            addStepListener(this);
        } else {
            removeStepListener(this);
        }
    }

    @Override
    public void preStep(StepEvent e) {
        //do nothing
    }

    @Override
    public void postStep(StepEvent e) {
        numSteps++;
        if (numSteps % 90 == 0) {
            numSteps = 0;
            new SawBlade((World) this).putOn(sawBladeXPos, getBody("platform2"));
        }

    }

    @Override
    public void collide(CollisionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
