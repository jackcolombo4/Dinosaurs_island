package it.polimi.dinosaursisland.partita;

import it.polimi.dinosaursisland.mappa.*;
import it.polimi.dinosaursisland.dinosauri.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {
    private ArrayList<Player> playerList = new ArrayList(0);
    private ArrayList<Player> activeList = new ArrayList(0);
    private Player currentPlayer;
    private Map map;
    private Turn turn;
    private GameController gameController;
    private Ranking ranking = new Ranking(playerList);
    private String[] gif = {"dinosaur1.gif", "dinosaur2.gif", "dinosaur3.gif", 
    "dinosaur4.gif", "dinosaur5.gif", "dinosaur6.gif", "dinosaur7.gif", "dinosaur8.gif", "dinosaurBear.gif"};
    /******* Constructors *******/
    public Game() {
        map = new Map();
        map.CreateMap();
        gameController = new GameController(this);
    }
    public Game(Map map){
        this.map = map;
        gameController = new GameController(this);
    }
    ){
    /******* Methods for managing *******/
    public Player tokenPlayer(String token) {
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).returnToken().equals(token)) {
                return playerList.get(i);
            }
        }
        return null;
    }

    public Map returnMap() {
        return map;
    }

    public Player returnCurrentPlayer() {
        return currentPlayer;
    }

    public ArrayList<Player> returnPlayerList() {
        return playerList;
    }

    public ArrayList<Player> returnActiveList() {
        return activeList;
    }
    
    public GameController returnController(){
        return gameController;
    }
    
    public Player isPlayerPres(String id) {
        //Check if the player is already registered 
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).returnId().equals(id)) {
                return playerList.get(i);   
            }
        }
        return null;
    }
    
    public Ranking updateRanking() {
        ranking.updateRanking(playerList);
        return ranking;
    }
     
    public boolean isTypeNameAvailable(String type) {;
        if (!playerList.isEmpty()) {
            for (int i = 0; i < playerList.size(); i++) {
                if (playerList.get(i).returnType() != null && playerList.get(i).returnType().returnName().equals(type)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean playerConnected(String token) {
        if (activeList.contains(tokenPlayer(token))) {
            return true;
        }
        return false;
    }
    ){
    /******* Methods for handling the match partita *******/
    public boolean newRegister(String userId, String password) {
        if (isPlayerPres(userId) == null) {

            Player tempPlayer = new Player(userId,   password);
            playerList.add(tempPlayer);
            playerList.get(playerList.size() - 1).setGif(gif[playerList.size() - 1]);
            if (tempPlayer.returnId().equals("bear")) {
                tempPlayer.setGif("dinosaurBear.gif");
            }   
            return true;
        }
        return false;
    }

    public boolean newLogin(String userId, String password) {
        Player login = isPlayerPres(userId);
        if (login != null    && login.passwordCorrect(password) && activeList.size() < 8) {
            if (activeList.isEmpty()) {
                currentPlayer = login;
            }
            return true;
        }
        return false;
    }

    public boolean createType(Player player, String name, int type) {
        Box temp = map.returnRandomAvailableBox();
        if (type == 1) {
            player.setType(new CarnivorousType(name, new Carnivorous(temp.GetX(), temp.GetY(), name.concat("1"))));
            player.returnType().returnDinosaur(0).setPlayer(player);
            gameController.setTypeVisual(player.returnType());
            return true;

        }
        if (type == 2) {
            player.setType(new ErbivorousType(name, new Erbivorous(temp.GetX(), temp.GetY(), name.concat("1"))));
            player.returnType().returnDinosaur(0).setPlayer(player);
            gameController.setTypeVisual(player.returnType());
            return true;
        }
        return false;
    }

    public boolean gameAccess(Player player) {
        if (activeList.size() < 8) {
            if (activeList.isEmpty()) {
                currentPlayer = player;
            }
            activeList.add(player);
            for (int i = 0; i < player.returnType().returnDinosaurs().size(); i++) {
                Dinosaur dinoTemp = player.returnType().returnDinosaurs().get(i);
                map.setDinosaur(dinoTemp, dinoTemp.getX(), dinoTemp.getY());    //ottimizzare parametri setDinosaur           
                gameController.setTypeVisual(player.returnType());
            }
            return true;    
        }
        return false;
    }

    public boolean gameOut(Player player){
        if(activeList.contains(player)){
            activeList.remove(player);
            for (int i = 0; i < player.returnType().returnDinosaurs().size(); i++) {
                Dinosaur dinoTemp = player.returnType().returnDinosaurs().get(i);
                map.removeDinosaur(dinoTemp);       
            }
            return true;
        }
        return true;
    }

    public void setNextPlayer() {
        if (activeList.isEmpty()) {
            return;
        }
        if (activeList.indexOf(currentPlayer) == (activeList.size() - 1)) {
            currentPlayer = activeList.get(0);
        } else {
            currentPlayer = activeList.get(activeList.indexOf(currentPlayer) + 1);
        }
    }
    
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    ){
    public boolean Register(String userId, String password, int type, String name, String firstName) {

        if (type == 1) {
            if (isPlayerPres(userId) == null) {
                Box temp = map.returnRandomAvailableBox();
                CarnivorousType tempType = new CarnivorousType(name, new Carnivorous(temp.GetX(), temp.GetY(), firstName));
                Player tempPlayer = new Player(userId, password, tempType);
                tempPlayer.returnType().returnDinosaur(0).setPlayer(tempPlayer);
                playerList.add(tempPlayer);
                gameController.setTypeVisual(tempType);
                playerList.get(playerList.size() - 1).setGif(gif[playerList.size() - 1]);
                if (tempPlayer.returnId().equals("bear")) {
                    tempPlayer.setGif("dinosaurBear.gif");
                }
                return true;
            }
            return false;
        }
        if (type == 2) {
            if (isPlayerPres(userId) == null) {
                Box temp = map.returnRandomAvailableBox();
                ErbivorousType tempType = new ErbivorousType(name, new Erbivorous(temp.GetX(), temp.GetY(), firstName));
                Player tempPlayer = new Player(userId, password, tempType);
                tempPlayer.returnType().returnDinosaur(0).setPlayer(tempPlayer);
                playerList.add(tempPlayer);
                gameController.setTypeVisual(tempType);
                playerList.get(playerList.size() - 1).setGif(gif[playerList.size() - 1]);
                if (tempPlayer.returnId().equals("bear")) {
                    tempPlayer.setGif("dinosaurBear.gif");
                }
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean Login(String userId, String password) {
        Player login = isPlayerPres(userId);
        if (login != null && login.passwordCorrect(password) && activeList.size() < 8) {
            if (activeList.isEmpty()) {
                currentPlayer = login;
            }
            activeList.add(login);
            for (int i = 0; i < login.returnType().returnDinosaurs().size(); i++) {
                Dinosaur dinoTemp = login.returnType().returnDinosaurs().get(i);
                map.setDinosaur(dinoTemp, dinoTemp.getX(), dinoTemp.getY());    //ottimizzare parametri setDinosaur           

            }
            return true;

        }
        return false;
    }
    
    public void setGameController(GameController gameController){
        this.gameController = gameController;
    }
}
