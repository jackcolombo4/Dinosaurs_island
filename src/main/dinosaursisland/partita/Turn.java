package it.polimi.dinosaursisland.partita;

import java.io.Serializable;

public class Turn implements Serializable{
    private Player player;
    private GameController gameController;
    
    public Turn(Player player,GameController gameController){
        this.player = player;
        this.gameController = gameController;
    }
    public void startTurn(){
    }
}
