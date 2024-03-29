package it.polimi.dinosaursisland.partita;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Ranking implements Serializable{
    private ArrayList<Score> scoreRanking = new ArrayList<Score>(0);
    
    public Ranking(ArrayList<Player> players){
        updateRanking(players);
    }

    public void updateRanking(ArrayList<Player> players){
        scoreRanking.removeAll(scoreRanking);
        for(int i =0;i<players.size();i++){
            scoreRanking.add(players.get(i).returnScore());
        }
        for(int i=0;i<scoreRanking.size()-1;i++)
            for(int j=i+1;j<scoreRanking.size();j++){
                if(scoreRanking.get(i).AssignScore()<scoreRanking.get(j).AssignScore()){
                    Collections.swap(scoreRanking, i, j);
                }
            }
    }

    public ArrayList<Score> returnRanking(){
        return scoreRanking;
    }
    
    public void printRanking(){
         for (int i=0;i<scoreRanking.size();i++){
                System.out.print(scoreRanking.get(i).returnPlayer() + " - " );
                System.out.println(scoreRanking.get(i).returnScore());
        }
    }
    
    public String returnStringRanking(){
        String string= "@classifica";
        for(int i=0;i<scoreRanking.size();i++){
            if(scoreRanking.get(i).returnPlayer2().returnType()==null){
                string = string.concat(",{").concat(scoreRanking.get(i).returnPlayer()).concat(",").concat(",n").concat("}");
            }else{
            string = string.concat(",{").concat(scoreRanking.get(i).returnPlayer()).concat(",").concat(scoreRanking.get(i).returnPlayer2().returnType().returnName()).concat(",").concat(String.valueOf(scoreRanking.get(i).returnScore())).concat(",s").concat("}");
            }
        }
        System.out.println(string);
        return string;
    }
}
