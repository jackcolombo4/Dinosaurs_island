package it.polimi.dinosaursisland.dinosauri;

import java.util.ArrayList;
import it.polimi.dinosaursisland.mappa.*;
import it.polimi.dinosaursisland.partita.Player;
import java.io.Serializable;

public class Type implements Serializable{

    protected int numDinosaur;
    protected ArrayList<Dinosaur> dinos = new ArrayList<Dinosaur>(0);
    protected String name;
    protected ArrayList<String> names = new ArrayList(0);
    protected ArrayList<Box> fullVisual = new ArrayList(0);
    protected ArrayList<Box> exploredVisual = new ArrayList(0);
    private int actionCounter = 2;
    private int gameTime = 120;
    private boolean exist=true;

    public Dinosaur returnDinosaur(int i){
        return dinos.get(i);
    }
    public ArrayList<Dinosaur> returnDinosaurs(){
        return dinos;
    }
    public String returnName(){
        return name;
    }
    public void SetName(String name){
        this.name = name;
    }
    public Dinosaur findDinosaur(String name){
        for (int i = 0; i < dinos.size(); i++){
            if (dinos.get(i).ReturnName().equals(name)){
                return dinos.get(i);
            }
        }
        return null;
    }
    public void setFullVisual(){
        fullVisual.clear();
        for (int i = 0; i < dinos.size(); i++){
            fullVisual.addAll(dinos.get(i).returnVisual());
        }
        setExploredVisual();
    }
    public void setExploredVisual(){
        for (int i = 0; i < fullVisual.size(); i++){
            if (!exploredVisual.contains(fullVisual.get(i))){
                exploredVisual.add(fullVisual.get(i));
            }
        }
    }
    public boolean isInFullVisual(Box box){
        if (fullVisual.contains(box)){
            return true;
        }
        return false;
    }
    public boolean isInExploredVisual(Box box){
        if (exploredVisual.contains(box)){
            return true;
        }
        return false;
    }
    public ArrayList<Box> returnFullVisual(){
        return fullVisual;
    }
    public ArrayList<String> returnNames(){
        return names;
    }

    public Dinosaur newDinosaur(String name, Player player){
        if (dinos.size() < 5){
            if (this instanceof CarnivorousType){
                Carnivorous temp = new Carnivorous(name);
                temp.setPlayer(player);
                dinos.add(temp);
                numDinosaur++;
                names.add(name);
                actionCounter = actionCounter + 2;
                return temp;
            }
            if (this instanceof ErbivorousType){
                Erbivorous temp = new Erbivorous(name);
                temp.setPlayer(player);
                dinos.add(temp);
                numDinosaur++;
                names.add(name);
                actionCounter = actionCounter + 2;
                return temp;
            }
        }
        return null;
    }
    public boolean dinosaursDead(Dinosaur dino){
        //Remove the dinosaur from species and return false if the specie does not have dinosaurs anymore
        dinos.remove(dino);
        numDinosaur--;
        names.remove(dino.ReturnName());
        actionCounter = actionCounter - 2;
        setFullVisual();
        if (!dinos.isEmpty()){
            return true;
        }
        return false;

    }

    public void doneAction(){
        actionCounter--;
    }
    public int returnAvailableAction(){
        return actionCounter;
    }
    public void decrementTime(){
        gameTime--;
    }
    public int leftTime(){
        return gameTime;
    }
    public void setAction(){
        actionCounter = 0;
        for (int i = 0; i < dinos.size(); i++){
            actionCounter = actionCounter + 2;
            dinos.get(i).setAction();
        }
    }
    public void extinguish(){
        exist=false;
    }
}