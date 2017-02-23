/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semester2java.Bodies.AIBodies;

import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;
import semester2java.Bodies.Player;
import semester2java.Levels.Level;



/**
 *
 * @author chris
 */
public class WormAI implements StepListener{
    
    private Player player;
    private Level level;
    private Worm worm;
    private float moveSpeed;
    private float jumpHeight;
    
    public WormAI(Player player, Level level, Worm worm){
        this.player=player;        
        this.level=level;
        this.worm=worm;
        moveSpeed=5;
        jumpHeight=4;
                   
    }            
    
    public void setMoveSpeed(float moveSpeed){
        this.moveSpeed=moveSpeed;
    }
    public float getMoveSpeed(){
        return moveSpeed;
    }
    public void setJumpHeight(float jumpHeight){
        this.jumpHeight=jumpHeight;
    }
    public float getJumpHeight(){
        return jumpHeight;
    }
    
    
    //implementation of abstract methods
    @Override
    public void preStep(StepEvent e) {
        
    }

    @Override
    public void postStep(StepEvent e) {
        
    }
    
}
