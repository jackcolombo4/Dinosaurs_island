
package it.polimi.dinosaursisland.exceptions;

import it.polimi.dinosaursisland.dinosauri.Dinosaur;


public class NotMovedException extends Exception{
    private Dinosaur dino;
    
    public NotMovedException(){}
    
    public NotMovedException(Dinosaur dino){
        this.dino=dino;
    }
    
    public Dinosaur dinosaurNotMoved(){
        return dino;
    }
    
}
