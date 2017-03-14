/*
* this class is the projectile object.  It is designed to be shot from a body
* 
 */
package semester2java.Bodies;

import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.DynamicBody;
import city.cs.engine.Shape;
import city.cs.engine.World;
import org.jbox2d.common.Vec2;
import semester2java.Bodies.AIBodies.Worm;



/**
 *
 * @author chris
 */
public class Projectile extends DynamicBody implements CollisionListener {

    private String nameOfShootingBody;  //the name of the body that shot this projectile

    public Projectile(World w, Shape shape, Vec2 force, Vec2 start, float angle) {
        super(w, shape);
        this.setPosition(start);
        this.setAngle(angle);   //angle to orient bullet in order to know how to orient the projectile
        this.applyForce(force); //speed/direction of projectile
        this.setName("projectileGeneric");
        nameOfShootingBody = "player";
        this.addCollisionListener(this);
    }

    public void setNameOfShootingBody(String name) {
        nameOfShootingBody = name;
    }

    public String getNameOfShootingBody() {
        return nameOfShootingBody;
    }

//collision listener for this body
    @Override
    public void collide(CollisionEvent e) {
        String thisName = this.getName();
        String otherName = e.getOtherBody().getName();
//        System.out.println(otherName);

// destroy this object if it hits an object other than its shooting body
        if (!(e.getOtherBody() instanceof Worm)&&!(e.getOtherBody() instanceof SawBlade)) {

            if (otherName == null) {
                this.destroy();
            } else if (otherName.equals("destructable")) {
                e.getOtherBody().destroy();
                this.destroy();
//            System.out.println("true");
            } else if (!(otherName.equals(nameOfShootingBody) || otherName.equals(thisName))) {
                this.destroy();
            }
        }
    }

}
