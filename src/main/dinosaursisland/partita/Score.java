package it.polimi.dinosaursisland.partita;

import it.polimi.dinosaursisland.dinosauri.*;
import java.io.Serializable;

public class Score implements Serializable{
    private int score;
    private Player player;
    
    public Score(Player player){
        this.player = player;
        AssignScore();
    }
    public int returnScore(){
        return score;
    }
    public void setScore(int score){
        this.score = score;
    }
    public String returnPlayer(){
        return player.returnId();
    }
    public Player returnPlayer2(){
        return player;
    }
    public int AssignScore(){
            score=0;
            for(int i=0;i<player.returnType().returnDinosaurs().size();i++){
                score = score +(1 + player.returnType().returnDinosaurs().get(i).ReturnSize());
            }
            return score;
    }
}
