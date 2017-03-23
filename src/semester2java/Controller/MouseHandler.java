/*
 * this class is designed to handle user mouse input while focus is on the
 * frame.
 *
 */
package semester2java.Controller;

import city.cs.engine.BodyImage;
import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import city.cs.engine.World;
import city.cs.engine.WorldView;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.jbox2d.common.Vec2;
import semester2java.Bodies.Player;
import semester2java.Bodies.Projectile;



/**
 *
 * @author chris
 */
public class MouseHandler extends MouseAdapter {

    private static final Shape projectile
            = new PolygonShape(-0.059f, 0.659f, 0.114f, 0.281f, 0.207f, -0.405f, 0.053f, -0.64f, -0.368f, -0.64f, -0.535f, -0.386f, -0.362f, 0.281f, -0.189f, 0.652f);

    private static final BodyImage projectileImage
            = new BodyImage("data/pumpkinseed.png", 2);

    private Player player;
    private WorldView view;
    private World world;

    public MouseHandler(WorldView view, World world, Player player) {
        this.view = view;
        this.world = world;
        this.player = player;
    }

    /**
     * @param e event object containing the mouse position
     */
    @Override
    public void mousePressed(MouseEvent e) {
        playerProjectile(e);
    }

    private void playerProjectile(MouseEvent e) {

        if (world.isRunning()) {
            if (player.getShots() > 0) {
                Vec2 playerLocation = player.getPosition();

                Vec2 mouseLocation = view.viewToWorld(e.getPoint()); //transpose position of mouse relative world
                Vec2 force = mouseLocation.sub(player.getPosition());   //force to apply to bullet
                float magnitude = (float) Math.sqrt(force.x * force.x + force.y * force.y);

                //work out where the bullet starts
                //starts at a position between the player and the mouse
                Vec2 bulletStart = playerLocation.add(force.mul(2 / magnitude));

                Vec2 horizontalVector = new Vec2(mouseLocation.x, playerLocation.y);
                horizontalVector.sub(playerLocation);

                //needed to work out the angle between the mouse and the horizontal
                //to work out how much to rotate the image
                float theta = (float) Math.acos(Math.abs(mouseLocation.x - playerLocation.x) / force.length());
                float pi = (float) Math.PI;

                //shift theta along cosine graph to get correct rotation
                if (mouseLocation.y < playerLocation.y) {
                    if (mouseLocation.x > playerLocation.x) {
                        theta = pi + theta;
                    } else {
                        theta = 0 - theta;
                    }
                } else if (mouseLocation.x > playerLocation.x) {
                    theta = pi - theta;
                }
                Projectile playerProjectile = new Projectile(view.getWorld(), projectile, force.mul(player.getShotSpeed() / magnitude), bulletStart, pi / 2 - theta);
                playerProjectile.setName("playerBullet");
                playerProjectile.addImage(projectileImage);
                player.decrementShots();
                player.setPlayerProjectile(playerProjectile);
            }
        }

    }

}
