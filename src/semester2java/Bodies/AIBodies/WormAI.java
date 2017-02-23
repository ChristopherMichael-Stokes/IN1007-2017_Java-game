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
    
    
    public WormAI(Player player, Level level, Worm worm){
        this.player=player;        
        this.level=level;
        this.worm=worm;
        
                
        
        
        
    }
    
    
            
    

    @Override
    public void preStep(StepEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void postStep(StepEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
