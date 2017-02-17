/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semester2java.Levels.Event;

import java.util.EventObject;

/**
 *
 * @author chris
 */
public class EndGameEvent extends EventObject{
    
    public EndGameEvent(Object source) {
        super(source);
    }
    
}
