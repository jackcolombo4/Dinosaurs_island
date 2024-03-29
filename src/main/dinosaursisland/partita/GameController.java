package it.polimi.dinosaursisland.partita;

import it.polimi.dinosaursisland.mappa.*;
import it.polimi.dinosaursisland.dinosauri.*;
import it.polimi.dinosaursisland.dinosauri.Dinosaur;
import it.polimi.dinosaursisland.exceptions.*;
import java.io.Serializable;

public class GameController implements Serializable {

    private Game game;
    private Dinosaur egg = null;
    private boolean booleanEgg = false;
    //attributi per i metodi
    private Dinosaur tempDino;
    private Object movement;
    private Carrion tempCarr;
    private Vegetation tempVege;
    private int tempEnergy, i;
    private Player player;
    private Box box;
    private String local;

    public GameController(Game game){

        this.game = game;

    }

    //se il dinosauro ha gia delle coordinate lo posiziona sulla mappa e aggiorna la sua visuale, se non le ha gliele assegna casualmente.
    public void setDinosaur(Dinosaur dino){
        if (dino.getX() == 0 && dino.getY() == 0){
            box = game.returnMap().returnRandomAvailableBox();
            game.returnMap().setDinosaur(dino, box.GetX(), box.GetY());
            setTypeVisual(findType(dino));
            return;//?
        }
        game.returnMap().setDinosaur(dino, dino.getX(), dino.getY());
        setTypeVisual(findType(dino));
        return;//?
    }

    
    /*Metodi per la ricerca*/
    
    //trova la specie del dinosauro passato come argomento
    public Type findType(Dinosaur dino) throws NullPointerException{
        if(game.returnPlayerList().isEmpty()){
           throw new NullPointerException(); 
        }
        for (i = 0; i < game.returnPlayerList().size(); i++){
            if (game.returnPlayerList().get(i).returnType().returnDinosaurs().contains(dino)){
                return game.returnPlayerList().get(i).returnType();
            }
        }
        return null;
    }

    public Type findTypeByName(String name) throws NullPointerException{
        if(game.returnPlayerList().isEmpty()){
           throw new NullPointerException(); 
        }
        for (i = 0; i < game.returnPlayerList().size(); i++){
            for (int j = 0; j < game.returnPlayerList().get(i).returnType().returnDinosaurs().size(); j++){
                if (game.returnPlayerList().get(i).returnType().returnDinosaur(j).ReturnName().equals(name)){
                    return game.returnPlayerList().get(i).returnType();
                }
            }
        }
        return null;
    }

    //ritorna null se il dinosauro non esiste
    public Dinosaur findDinoByName(String name) throws NullPointerException{
        if(game.returnPlayerList().isEmpty()){
           throw new NullPointerException(); 
        }
        for (i = 0; i < game.returnPlayerList().size(); i++){
            for (int j = 0; j < game.returnPlayerList().get(i).returnType().returnDinosaurs().size(); j++){
                if (game.returnPlayerList().get(i).returnType().returnDinosaur(j).ReturnName().equals(name)){
                    return game.returnPlayerList().get(i).returnType().returnDinosaur(j);
                }
            }
        }
        return null;
    }

    //trova il giocatore della specie passata come argomento
    public Player findPlayer(Type type) throws NullPointerException{
         if(game.returnPlayerList().isEmpty()){
           throw new NullPointerException(); 
         }
         for (i = 0; i < game.returnPlayerList().size(); i++){
            if (game.returnPlayerList().get(i).returnType() == type){
                return game.returnPlayerList().get(i);
            }
        }
        return null;
    }
    
    public Player findPlayerByName(String name)throws NullPointerException{
         if(game.returnPlayerList().isEmpty()){
           throw new NullPointerException(); 
        }
         for(i=0;i<game.returnPlayerList().size();i++){
            if(game.returnPlayerList().get(i).returnId().equals(name)){
                return game.returnPlayerList().get(i);
            }
        }
        return null;
    }
    
    /*Metodi per la visuale*/
    
    //setta la visuale per la specie passata come argomento
    public void setTypeVisual(Type type){
        //System.out.println(type.returnDinosaurs());
        for (i = 0; i < type.returnDinosaurs().size(); i++){
            type.returnDinosaurs().get(i).SetVisual(game.returnMap());
        }
        type.setFullVisual();

    }

    public String localVisual(Dinosaur dino){
        local= new String();
        
        int j = -1;

        local = local.concat("@vistaLocale,{");
        local += String.valueOf(dino.returnVisual().get(0).GetX());
        local = local.concat(",");
        local += String.valueOf(dino.returnVisual().get(0).GetY());
        local = local.concat("},{");
        local += String.valueOf(dino.returnNumRow());
        local = local.concat(",");
        local += String.valueOf(dino.returnNumColumn());
        local = local.concat("},");
        
        
       for (i = 0; i < dino.returnNumRow() ; i++){
            for (j = 0; j <dino.returnNumColumn(); j++){
                Box temp = dino.returnVisual().get(i*dino.returnNumColumn()+j);
                if (temp instanceof WaterBox){
                    local = local.concat("[").concat("a").concat("]");

                } else {
                       
                        if (((GroundBox) temp).isCarrionPres() != null && ((GroundBox) temp).isDinosaurPres() != null){
                            local = local.concat("[").concat("c,").concat(String.valueOf(((GroundBox) temp).isCarrionPres().ReturnEnergy())).concat(",d,").concat(String.valueOf(((GroundBox) temp).isDinosaurPres().ReturnEnergy())).concat("]");
                        }   
                        if (((GroundBox) temp).isVegetationPres() != null && ((GroundBox) temp).isDinosaurPres() != null){
                                local = local.concat("[").concat("v,").concat(String.valueOf(((GroundBox) temp).isVegetationPres().ReturnEnergy())).concat(",d,").concat(String.valueOf(((GroundBox) temp).isDinosaurPres().ReturnEnergy())).concat("]");

                        }
                        if (((GroundBox) temp).isCarrionPres() != null && ((GroundBox) temp).isDinosaurPres() == null){
                                   local = local.concat("[").concat("c,").concat(String.valueOf(((GroundBox) temp).isCarrionPres().ReturnEnergy())).concat("]"); 

                        }
                        if (((GroundBox) temp).isVegetationPres() != null && ((GroundBox) temp).isDinosaurPres() == null){
                               local = local.concat("[").concat("v,").concat(String.valueOf(((GroundBox) temp).isVegetationPres().ReturnEnergy())).concat("]"); 

                        }
                        if (((GroundBox) temp).isDinosaurPres() != null && ((GroundBox) temp).isVegetationPres() == null && ((GroundBox) temp).isVegetationPres() == null){
                                local = local.concat("[").concat("d,").concat(String.valueOf(((GroundBox) temp).isDinosaurPres().ReturnEnergy())).concat("]");

                        }
                    }
                    if(temp instanceof GroundBox && ((GroundBox) temp).isCarrionPres() == null && ((GroundBox) temp).isDinosaurPres() == null && ((GroundBox) temp).isVegetationPres() == null){
                        local = local.concat("[").concat("t").concat("]");
                    }
                }
                local= local.concat(";");
            }  
            
             System.out.println();
             System.out.println(local);
             System.out.println();
        return local;
    }
    
    public void setAllVisual(){
        
        for (int i = 0; i < game.returnPlayerList().size(); i++){
            setTypeVisual(game.returnPlayerList().get(i).returnType());
        }

    }
    
    /*Metodi per i comandi in partita*/

    public void MoveDinosaur(Dinosaur dino, Box destinationBox) throws NotMovedException, YetMovedException, DinoDeathException, TypeDeathException, EatException, MovedException, MoveDeathException,FightException {

        destinationBox = game.returnMap().ReturnBox(destinationBox.GetX(), destinationBox.GetY());

        if (dino.returnMovementAction() == false){ 
            throw new YetMovedException();
        }
        
        if (destinationBox instanceof WaterBox){
            throw new NotMovedException();
        }

        tempDino = ((GroundBox) destinationBox).isDinosaurPres();
        movement = game.returnMap().moveDinosaur(dino, destinationBox);
        if (movement instanceof Box){
            tempCarr = ((GroundBox) destinationBox).isCarrionPres();
            tempVege = ((GroundBox) destinationBox).isVegetationPres();

            if (tempDino != null){
                //System.out.println(dino.ReturnName());
                //System.out.println(tempDino.ReturnName());
                Dinosaur deadDino = dino.Fight(tempDino);
                //System.out.println(deadDino.ReturnName());
                if (deadDino == tempDino){   //perde l'altro dinosauro
                    dino.IncrementEnergy(deadDino.ReturnEnergy());
                    setDinosaur(dino);
                    try {
                        findType(dino).doneAction();
                        dino.doneMovementAction();
                        killDinosaur(tempDino);
                        checkTurn();
                        throw new FightException(tempDino);
                    } catch (TypeDeathException tde){
                        throw new TypeDeathException(findType(dino));
                    }

                }

                if (deadDino == dino){  //perde il dinosauro che si è mosso
                    tempDino.IncrementEnergy(dino.ReturnEnergy());
                    setDinosaur(tempDino);
                    try {
                        killDinosaur(dino);
                        throw new FightException(dino);
                    } catch (TypeDeathException tde){//la specie muore
                        throw new TypeDeathException(findType(dino));
                    }
                }
            }
            if (dino instanceof Carnivorous && tempCarr != null){
                tempEnergy = dino.IncrementEnergy(tempCarr.ReturnEnergy());
                if (tempEnergy != 0){
                    tempCarr.setEnergy(tempEnergy);
                }
                if (tempEnergy == 0){
                    game.returnMap().newCarrion(tempCarr);
                }
                setTypeVisual(game.returnCurrentPlayer().returnType());
                findType(dino).doneAction();
                dino.doneMovementAction();
                checkTurn();
                throw new EatException(dino.ReturnEnergy());

            }
            if (dino instanceof Erbivorous && tempVege != null){
                tempVege.setEnergy(dino.IncrementEnergy(tempVege.ReturnEnergy()));
                setTypeVisual(game.returnCurrentPlayer().returnType());
                findType(dino).doneAction();
                dino.doneMovementAction();
                checkTurn();
                throw new EatException(dino.ReturnEnergy());

            }
            setTypeVisual(findType(dino));
            findType(dino).doneAction();
            dino.doneMovementAction();
            checkTurn();
            throw new MovedException(dino.ReturnPos());
        }

        if (movement instanceof Carnivorous || movement instanceof Erbivorous){
            oldKillDinosaur((Dinosaur) movement);
            throw new MoveDeathException();
        }
    }

    public void layEgg(Dinosaur dino, String name) throws TypeDeathException, DinoDeathException, ActionNotPermittedException, EggDeposedException {
        if (!dino.returnOtherAction()){
            throw new ActionNotPermittedException();
        }
        if (dino.LayEgg()){
            Dinosaur temp = new Dinosaur(name);
            if (temp != null){
                temp.setPosition(dino.returnAdjacentPosition(game.returnMap()));
                game.returnCurrentPlayer().setEgg(temp);
                findType(dino).doneAction();
                checkTurn();
                throw new EggDeposedException(temp);
            } else {
                throw new ActionNotPermittedException();
            }
        } else {
            throw new DinoDeathException(dino);
        }

    }

    public void GrowDinosaur(Dinosaur dino) throws DinoDeathException, TypeDeathException, ActionNotPermittedException, GrowDinosaurException {
        if (!dino.returnOtherAction()){
            throw new ActionNotPermittedException();
        }
        int newD = dino.Growth();
        if (newD != 0){
            setTypeVisual(findType(dino));
            findType(dino).doneAction();
            dino.doneOtherAction();
            checkTurn();
            throw new GrowDinosaurException(newD);
        }
        killDinosaur(dino);
        throw new DinoDeathException(dino);

    }

    public void killDinosaur(Dinosaur dino) throws TypeDeathException {
        if (((GroundBox) game.returnMap().ReturnBox(dino.getX(), dino.getY())).isDinosaurPres() == dino){
            game.returnMap().removeDinosaur(dino);
        }
        Type type = findType(dino);
        if (findType(dino).dinosaursDead(dino)){
            setTypeVisual(player.returnType());
            return;
        }
        type.extinguish();
        throw new TypeDeathException(type);
    }

    public void decrementDinosaursLife(Type type){
        for (i = 0; i < type.returnDinosaurs().size(); i++){
            type.returnDinosaurs().get(i).decrementLife();
            if (type.returnDinosaurs().get(i).ReturnLifeSpan() == 0){
                try {
                    killDinosaur(type.returnDinosaurs().get(i));
                } catch (TypeDeathException tde){
                    tde.returnDeadType();
                }
            }
        }
    }
    
    public void extinguishType(Type type){
        type.extinguish();
        for(int i =0;i<type.returnDinosaurs().size();i++){
            try{
                killDinosaur(type.returnDinosaurs().get(i));
            }catch(TypeDeathException tde){
                
            }
        }
    }
    
    //ritorna true se si è schiuso un uovo
    public boolean hatchEgg(){
        egg = game.returnCurrentPlayer().returnEgg();
        if (egg != null && booleanEgg == true){
            Dinosaur tempEgg = game.returnCurrentPlayer().returnType().newDinosaur(egg.ReturnName(), findPlayer(game.returnCurrentPlayer().returnType()));
            tempEgg.setPosition(new Box(egg.getX(), egg.getY()));
            setDinosaur(tempEgg);
            egg = null;
            booleanEgg = false;
            game.returnCurrentPlayer().setEgg(null);
            return true;
        }
        return false;
    }

    
    /*Metodi per la gestione della partita*/
    
    //setta il nuovo giocatore corrent, fa crescere le piante e degradare le carogne
    public void nextTurn() throws TypeDeathException {
        Player tempPlayer = game.returnCurrentPlayer();
        
        tempPlayer.returnType().decrementTime();        
        tempPlayer.returnType().setAction();
        
        decrementDinosaursLife(tempPlayer.returnType());
        
        game.setNextPlayer();
        
        if (game.returnCurrentPlayer().returnEgg() != null){
            booleanEgg = true;
        }
        game.returnMap().carrionDegradation();
        game.returnMap().vegetationGrowth();
        game.updateRanking();
        
        
        if (tempPlayer.returnType().leftTime() == 0){
            extinguishType(tempPlayer.returnType());
            throw new TypeDeathException(tempPlayer.returnType());
        }
        
        setAllVisual();
        
    }

    public void emptyCount(){
    }

    public void checkTurn(){
        if (game.returnCurrentPlayer().returnType().returnAvailableAction() == 0){
            try {
                nextTurn();
            } catch (TypeDeathException t){
                t.returnDeadType();
            }
        }
    }

    public Ranking returnRanking(){
        return game.updateRanking();

    }
    
    /*Metodi deprecati*/
    
    //esegue tutte le operazioni di spostamento del dinosauro, lo fa combattere se è presente un dinosauro sulla casella di destinazione e mangiare se si sposta in una
    //casella con presente del cibo per la sua specie. Ritorna null se lo spostamento non è possibile,un box se lo spostamento è avvenuto senza altri avvenimenti,
    //il dinosauro che muore se si verifica un combattimento, un giocatore se il dinosauro morto era l ultimo della specie del giocatore, un integer con la nuova energia del
    //dinosauro se mangia
    public Object oldMoveDinosaur(Dinosaur dino, Box destinationBox){
        destinationBox = game.returnMap().ReturnBox(destinationBox.GetX(), destinationBox.GetY());

        if (!dino.returnMovementAction()){
            return new Boolean(false);
        }

        if (destinationBox instanceof WaterBox){
            return null;
        }
        tempDino = ((GroundBox) destinationBox).isDinosaurPres();
        movement = game.returnMap().moveDinosaur(dino, destinationBox);
        if (movement instanceof Box){
            tempCarr = ((GroundBox) destinationBox).isCarrionPres();
            tempVege = ((GroundBox) destinationBox).isVegetationPres();

            if (tempDino != null){
                //System.out.println(dino.ReturnName());
                //System.out.println(tempDino.ReturnName());
                Dinosaur deadDino = dino.Fight(tempDino);
                //System.out.println(deadDino.ReturnName());
                if (deadDino == tempDino){   //perde l'altro dinosauro
                    dino.IncrementEnergy(deadDino.ReturnEnergy());
                    Object temp = oldKillDinosaur(tempDino);
                    setDinosaur(dino);
                    if (temp == null){
                        findType(dino).doneAction();
                        dino.doneMovementAction();
                        checkTurn();
                        return tempDino;
                    } else {
                        findType(dino).doneAction();
                        dino.doneMovementAction();
                        checkTurn();
                        return temp;
                    }

                }

                if (deadDino == dino){  //perde il dinosauro che si è mosso
                    tempDino.IncrementEnergy(dino.ReturnEnergy());
                    Object temp = oldKillDinosaur(dino);
                    setDinosaur(tempDino);
                    if (temp == null){
                        return dino;
                    } else {//la specie muore
                        return temp;
                    }
                }
            }
            if (dino instanceof Carnivorous && tempCarr != null){
                tempEnergy = dino.IncrementEnergy(tempCarr.ReturnEnergy());
                if (tempEnergy != 0){
                    tempCarr.setEnergy(tempEnergy);
                }
                if (tempEnergy == 0){
                    game.returnMap().newCarrion(tempCarr);
                }
                setTypeVisual(game.returnCurrentPlayer().returnType());
                findType(dino).doneAction();
                dino.doneMovementAction();
                checkTurn();
                return (Integer) dino.ReturnEnergy();

            }
            if (dino instanceof Erbivorous && tempVege != null){
                tempVege.setEnergy(dino.IncrementEnergy(tempVege.ReturnEnergy()));
                setTypeVisual(game.returnCurrentPlayer().returnType());
                findType(dino).doneAction();
                dino.doneMovementAction();
                checkTurn();
                return (Integer) dino.ReturnEnergy();

            }
            setTypeVisual(findType(dino));
            findType(dino).doneAction();
            dino.doneMovementAction();
            checkTurn();
            return dino.ReturnPos();
        }

        if (movement instanceof Carnivorous || movement instanceof Erbivorous){
            oldKillDinosaur((Dinosaur) movement);
            return movement;
        }

        return movement;


    }
    
    //fa crescere o morire il dinosauro a seconda della sua energia, ritorna un intero con la dimensione attuale del dinosauro, un player se il dinosauro muore ed era l ultimo della specie
    public Object oldGrowDinosaur(Dinosaur dino){
        if (!dino.returnOtherAction()){
            return new Boolean(false);
        }
        int newD = dino.Growth();
        if (newD != 0){
            setTypeVisual(findType(dino));
            findType(dino).doneAction();
            dino.doneOtherAction();
            checkTurn();
            return (Integer) newD;
        }
        return oldKillDinosaur(dino);

    }
    
    //se il dinosauro puo' deporre un uovo e la specie non ha gia 5 dinosauri crea un dinosauro con posizione casuale e setta egg 
    //true per permettere al turno successivo di posizionare il dinoasuro sulla mappa. Ritorna null se il dinosauro è morto cercando di deporre l'uovo
    //il riferimento al dinosauro che depone l'uovo se si hanno gia 5 dinosauri nella specie e il riferimento al nuovo dinosauro se è stato deposto l'uovo
    //un Player se il dinosauro muore ed era l ultimo della specie,ritorna false se non puo muoversi
    public Object oldLayEgg(Dinosaur dino, String name){
        if (!dino.returnOtherAction()){
            return new Boolean(false);
        }
        if (dino.LayEgg()){
            Dinosaur temp = new Dinosaur(name);
            if (temp != null){
                temp.setPosition(dino.returnAdjacentPosition(game.returnMap()));
                game.returnCurrentPlayer().setEgg(temp);
                findType(dino).doneAction();
                checkTurn();
                return temp;
            } else {
                return dino;
            }
        } else {
            return oldKillDinosaur(dino);
        }

    }
    
    //Ritorna null se il dinosauro è ucciso.Se era l ultimo dinosauro della specie cancella il giocatore corrispondente e ne ritorna il riferimento
    public Type oldKillDinosaur(Dinosaur dino){
        if (((GroundBox) game.returnMap().ReturnBox(dino.getX(), dino.getY())).isDinosaurPres() == dino){
            game.returnMap().removeDinosaur(dino);
        }
        Type type = findType(dino);
        if (findType(dino).dinosaursDead(dino)){
            setTypeVisual(player.returnType());
            return null;
        }
        type.extinguish();
        return type;
    }
}
