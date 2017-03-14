/*
 * this class is a subtype of Level, so it is also a subtype of world.  All the
 * bodies unique to the level are set here.
 *
 */
package semester2java.Levels.levels;

import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.Shape;
import java.io.Serializable;
import org.jbox2d.common.Vec2;
import semester2java.Levels.Level;

/**
 *
 * @author Christopher
 */
public final class Level2 extends Level implements Serializable {

    private final Vec2 start;
    private static final BodyImage W3
            = new BodyImage(getTextureLocation(Textures.METAL_03), 15);

    public Level2() {
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
        
        setBackground(Backgrounds.FOREST_BACKGROUND_02);

        changeFriction(getFrictionCoefficient(FrictionCoefficient.METAL));
        getBodies().forEach((k, v) -> {
            v.setClipped(true);
            v.addImage(W3);
        });
    }

}
