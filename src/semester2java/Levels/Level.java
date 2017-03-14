/*
 * the level object is a subtype of world, and stores a collection of bodies
 * within the level.  The class can also be used to alter the physical properties 
 * of the bodies
 */
package semester2java.Levels;

import city.cs.engine.Body;
import city.cs.engine.DynamicBody;
import city.cs.engine.Shape;
import city.cs.engine.SolidFixture;
import city.cs.engine.StaticBody;
import city.cs.engine.World;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jbox2d.common.Vec2;
import semester2java.Levels.Event.ChangeLevelEvent;
import semester2java.Levels.Event.ChangeLevelListener;
import semester2java.Levels.Event.EndGameEvent;
import semester2java.Levels.Event.EndGameListener;

/**
 *
 * @author Christopher
 */
public abstract class Level extends World {

    private final Map<String, Body> bodies;
    private final Map<String, Shape> shapes;
    private static List<ChangeLevelListener> _listeners;
    private static List<EndGameListener> _listenersEndGame;
    private float mu;
    private Backgrounds background;

    //all texture paths   
    public enum Textures {
        CONCRETE_01, CONCRETE_02, CONCRETE_03, METAL_01,
        METAL_02, METAL_03, WOOD_01, WOOD_02, WOOD_03
    }

    public enum FrictionCoefficient {
        WOOD, CONCRETE, METAL
    }

    public enum Backgrounds {
        FOREST_BACKGROUND_01, FOREST_BACKGROUND_02, FOREST_BACKGROUND_03;
    }

    public static float getFrictionCoefficient(FrictionCoefficient frictionCoefficient) {
        switch (frictionCoefficient) {
            case WOOD:
                return 0.5f;
            case CONCRETE:
                return 0.8f;
            case METAL:
                return 0.7f;
            default:
                return 0.0f;
        }
    }

    public static String getTextureLocation(Textures textures) {

        switch (textures) {
            case CONCRETE_01:
                return "data/concreteTexture01.jpg";
            case CONCRETE_02:
                return "data/concreteTexture02.jpg";
            case CONCRETE_03:
                return "data/concreteTexture03.jpg";
            case METAL_01:
                return "data/metalTexture01.jpg";
            case METAL_02:
                return "data/metalTexture02.jpg";
            case METAL_03:
                return "data/metalTexture03.jpg";
            case WOOD_01:
                return "data/woodTexture01.jpg";
            case WOOD_02:
                return "data/woodTexture02.jpeg";
            case WOOD_03:
                return "data/woodTexture03.jpeg";
            default:
                return null;
        }
    }

    public File chooseBackground(Backgrounds background) {
        switch (background) {
            case FOREST_BACKGROUND_01:
                return new File("data/forestBackground01.jpg");
            case FOREST_BACKGROUND_02:
                return new File("data/forestBackground03.jpeg");
            case FOREST_BACKGROUND_03:
                return new File("data/forestBackground02.png");
            default:
                return null;
        }

    }

    public Level() {
        shapes = new HashMap<>();
        bodies = new HashMap<>();
        _listeners = new ArrayList<>();
        _listenersEndGame = new ArrayList<>();
        this.stop();
    }

    protected abstract void initializeLevel();

    public World getWorld() {
        return this;
    }

    public Map<String, Body> getBodies() {
        return bodies;
    }

    public void setBody(boolean isStatic, String bodyName, Shape shape, Vec2 position, float angle) {
        //initialize bodies in a level
        if (isStatic) {
            setStaticBodies(bodyName, shape);
        } else {
            setDynamicBodies(bodyName, shape);
        }
        getBody(bodyName).setPosition(position);
        getBody(bodyName).setAngle(angle);
    }

    public Body getBody(String key) {
        return bodies.get(key);
    }

    public Shape getShape(String key) {
        return shapes.get(key);
    }

    public void setStaticBodies(String key, Shape shape) {
        shapes.put(key, shape);
        Body sb = new StaticBody(this, shape);
        bodies.put(key, sb);
    }

    public void setDynamicBodies(String key, Shape shape) {
        shapes.put(key, shape);
        Body sb = new DynamicBody(this, shape);
        bodies.put(key, sb);
    }

    public void setBackground(Backgrounds background) {
        this.background = background;
    }

    public File getBackground() {
        return chooseBackground(background);
    }

    public void changeFriction(float mu) {
        //change friction for all bodies
        bodies.forEach((k, v) -> {
            this.mu = mu;
            if (v.getAngle() != 0) {
                this.mu = 1;
            }
            new SolidFixture(v, getShape(k)).setFriction(this.mu);
        });
    }

    public synchronized void addChangeLevelListener(ChangeLevelListener listener) {
        _listeners.add(listener);
    }

    public synchronized void removeChangeLevelListener(ChangeLevelListener listener) {
        _listeners.remove(listener);
    }

    public void fireChangeLevelEvent() {
        //create new event to allow the main class to create a new step listener
        ChangeLevelEvent event = new ChangeLevelEvent(this);
        Iterator listeners = _listeners.iterator();
        while (listeners.hasNext()) {
            ((ChangeLevelListener) listeners.next()).changeLevel(event);
        }
    }

    public synchronized void addEndGameListener(EndGameListener listener) {
        _listenersEndGame.add(listener);
    }

    public synchronized void removeEndGameListener(EndGameListener listener) {
        _listenersEndGame.remove(listener);
    }

    public synchronized void endGame() {
        EndGameEvent event = new EndGameEvent(this);
        Iterator listeners = _listenersEndGame.iterator();
        while (listeners.hasNext()) {
            ((EndGameListener) listeners.next()).endGame(event);
        }
    }

}
