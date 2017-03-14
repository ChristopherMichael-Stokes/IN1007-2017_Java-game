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
        
        start.x += 10f;
        start.y += 1f;
        Shape pf1 = new BoxShape(3, 0.5f);
        setBody(true, "platform1", pf1, start, 0);
        
        start.x += 20f;
        Shape pf2 = new BoxShape(7,0.5f);
        setBody(true, "platform2", pf2, start, 0);
                
        start.x += 11f;
        start.y += 7f;
        Shape pf3 = new BoxShape(7.25f,0.5f);
        setBody(true, "platform3", pf3, start, pi/2);
        
        start.x -= 11f;
        start.y -= 1f;
        Shape pf4 = new BoxShape(3f,0.5f);
        setBody(true, "platform4", pf4, start, 0);
        
        start.x += 9f;
        start.y += 6f;
        setBody(true, "platform5", pf4, start, pi/4);
        
        start.x += 11f;
        start.y += 3f;
        Shape pf6 = new BoxShape(4f,0.5f);
        setBody(true, "platform6", pf6, start, 0);
        
        start.y -= 17f;
        setBody(true, "platform7", pf6, start, 0);
        getBody("platform7").setName("end");
        
        setBackground(Backgrounds.FOREST_BACKGROUND_02);

        changeFriction(getFrictionCoefficient(FrictionCoefficient.METAL));
        getBodies().forEach((k, v) -> {
            v.setClipped(true);
            v.addImage(W3);
        });
    }

}
