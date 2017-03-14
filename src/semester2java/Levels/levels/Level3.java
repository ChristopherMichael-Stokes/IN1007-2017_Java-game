/*
 * this class is a subtype of Level, so it is also a subtype of world.  All the
 * bodies unique to the level are set here.
 *
 */
package semester2java.Levels.levels;

import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.Shape;
import city.cs.engine.World;
import java.io.Serializable;
import org.jbox2d.common.Vec2;
import semester2java.Bodies.SawBlade;
import semester2java.Levels.Level;
import static semester2java.Levels.Level.getFrictionCoefficient;
import static semester2java.Levels.Level.getTextureLocation;

/**
 *
 * @author Christopher
 */
public final class Level3 extends Level implements Serializable {

    private final Vec2 start;
    private static final BodyImage W3
            = new BodyImage(getTextureLocation(Level.Textures.WOOD_01), 15);

    public Level3() {
        super();
        start = new Vec2(0, -11.5f);
        initializeLevel();
    }

    @Override
    protected void initializeLevel() {
        //the purpose of this method is to avoid putting overridable
        //method calls in the constructor.
        float pi = (float) Math.PI;

        Shape pf0Shape = new BoxShape(2.5f, 5.5f);
        setBody(true, "0Shape", pf0Shape, start, 0);

        start.x += 4.5f;
        start.y -= 5f;
        Shape shape = new BoxShape(3, 0.5f);
        setBody(true, "start", shape, start, 0);
        
        start.x += 20f;
        start.y += 5f;
        Shape pf1 = new BoxShape(8f,0.5f);
        setBody(true, "platform1", pf1, start, pi/6);
        
        start.x += 13f;
        start.y += 7.5f;
        setBody(true, "platform2", pf1, start, pi/6);
        new SawBlade((World)this).putOn(getBody("platform2"));
        
        start.x += 13f;
        start.y += 5f;
        
        
        
        setBackground(Level.Backgrounds.FOREST_BACKGROUND_01);

        changeFriction(getFrictionCoefficient(Level.FrictionCoefficient.WOOD));
        getBodies().forEach((k, v) -> {
            v.setClipped(true);
            v.addImage(W3);
        });
    }

}
