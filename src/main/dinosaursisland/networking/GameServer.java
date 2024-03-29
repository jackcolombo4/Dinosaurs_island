package it.polimi.dinosaursisland.networking;

import it.polimi.dinosaursisland.dinosauri.CarnivorousType;
import it.polimi.dinosaursisland.dinosauri.Dinosaur;
import it.polimi.dinosaursisland.dinosauri.ErbivorousType;
import it.polimi.dinosaursisland.exceptions.*;
import it.polimi.dinosaursisland.partita.Game.*;
import it.polimi.dinosaursisland.mappa.*;
import it.polimi.dinosaursisland.partita.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameServer {
    private ConnectionWaiter connectionWaiter;
    private ArrayList<Socket> sockets = new ArrayList(0);
    private int port;
    private Dinosaur dino;
    private Player player;
    private Box box;
    private Game game = new Game();
    private GameController gameController;
    private boolean registration;
    private boolean login;
    private boolean type;
    private Encoder encoder;
    private Decoder decoder = new Decoder();
    private ArrayList<String> tokens = new ArrayList(0);
    private String string;
    private Timer timer;

    public GameServer(int port){
        this.port = port;
        gameController = game.returnController();
    }
    public GameServer(int port,Game game){
        this.port = port;
        gameController = game.returnController();
    }
    
    public Game returnGame(){
        return game;
    }
    
    public void startServer(){
        connectionWaiter = new ConnectionWaiter(port, this);
        connectionWaiter.start();
    }

    public void createClientHandler(Socket socket){
        sockets.add(socket);
        (new ClientHandler(socket,this)).start();
    }
    
    public String generateToken(){
        double token = (Math.random());
        return String.valueOf(token);
    }

    public int returnNumType(String string){
        if (string.equals("c")){
            return 1;
        }
        if (string.equals("e")){
            return 2;
        }
        return 0;
    }

    public boolean validToken(String token){
        if (tokens.contains(token)){
            return true;
        }
        return false;
    }

    public synchronized Command doCommand(Command command){
        if (command == null){
            return new Command("@comandoErrato");
        }
        if (command.returnCommand().equals("@comandoErrato")){
            return command;
        }
        if (command.returnCommand().equals("@creaUtente") && command.returnParameters().size() == 2){
            return createUser(command);
        }
        if (command.returnCommand().equals("@login") && command.returnParameters().size() == 2){
            return login(command);
        }
        if (command.returnCommand().equals("@creaRazza") && command.returnParameters().size() == 3){
            return createType(command);
        }
        if (command.returnCommand().equals("@accessoPartita") && command.returnParameters().size() == 1){
            return gameAccess(command);
        }
        if (command.returnCommand().equals("@uscitaPartita") && command.returnParameters().size() == 1){
            return gameOut(command);
        }
        if (command.returnCommand().equals("@listaGiocatori")){
            return playerList(command);
        }
        if (command.returnCommand().equals("@classifica") && command.returnParameters().size() == 1){
            return returnRanking(command);
        }
        if (command.returnCommand().equals("@logout") && command.returnParameters().size() == 1){
            return returnRanking(command);
        }
        if (command.returnCommand().equals("@mappaGenerale") && command.returnParameters().size() == 1){
            return returnMapString(command);
        }
        if (command.returnCommand().equals("@listaDinosauri") && command.returnParameters().size() == 1){
            return dinosaurList(command);
        }
        if (command.returnCommand().equals("@statoDinosauro") && command.returnParameters().size() == 2){
            return dinosaurState(command);
        }
        if (command.returnCommand().equals("@vistaLocale") && command.returnParameters().size() == 2){
            return returnLocalVisual(command);
        }
        if (command.returnCommand().equals("@muoviDinosauro") && command.returnParameters().size() == 4){
            return dinosaurMovement(command);
        }
        if (command.returnCommand().equals("@cresciDinosauro") && command.returnParameters().size() == 2){
            return growthDinosaur(command);
        }
        if (command.returnCommand().equals("@deponiUovo") && command.returnParameters().size() == 2){
            return dinosaurEgg(command);
        }
        if (command.returnCommand().equals("@passaTurno") && command.returnParameters().size() == 1){
            return passTurn(command);
        }
        return new Command("@no");
    }


    public Command createType(Command command){
        if (!validToken(command.returnToken())){
            return decoder.decodeCommand("@no,@tokenNonValido");
        }
        if ((game.tokenPlayer(command.returnToken())).returnType() != null){
            return decoder.decodeCommand("@no,@razzaGiaCreata");
        }
        if (!game.isTypeNameAvailable(command.returnParameter(1))){
            return decoder.decodeCommand("@no,@nomeRazzaGiaOccupato");
        }
        type = game.createType(game.tokenPlayer(command.returnToken()), command.returnParameter(1), returnNumType(command.returnParameter(2)));
        if (type){
            return new Command("@ok");
        }
        return new Command("@no");
    }

    public Command login(Command command){
        login = game.newLogin(command.returnParameter(0), command.returnParameter(1));
        if (login){
            String token = "T=";
            token = token.concat(generateToken());
            tokens.add(token);
            game.isPlayerPres(command.returnParameter(0)).setToken(token);
            return decoder.decodeCommand("@ok".concat(",").concat(token));
        }
        return decoder.decodeCommand("@no,@autenticazioneFallita");
    }

    public Command logOut(Command command){
        if (!validToken(command.returnToken())){
            return decoder.decodeCommand("@no,@tokenNonValido");
        }
        tokens.remove(command.returnToken());
        game.tokenPlayer(command.returnToken()).revokeToken();
        return decoder.decodeCommand("@ok");
    }

    public Command createUser(Command command){
        registration = game.newRegister(command.returnParameter(0), command.returnParameter(1));
        if (registration){
            return new Command("@ok");
        }
        return decoder.decodeCommand("@no,@usernameOccupato");
    }

    public Command gameAccess(Command command){
        if (!validToken(command.returnToken())){
            return decoder.decodeCommand("@no,@tokenNonValido");
        }
        if (game.returnActiveList().size() == 8){
            return decoder.decodeCommand("@no,@troppiGiocatori");
        }
        if (game.tokenPlayer(command.returnToken()).returnType() == null){
            return decoder.decodeCommand("@no,@razzaNonCreata");
        }
        if (game.gameAccess(game.tokenPlayer(command.returnToken()))){
            return decoder.decodeCommand("@ok");
        }
        return decoder.decodeCommand("@no");
    }

    public Command gameOut(Command command){
        if (!tokens.contains(command.returnToken())){
            return decoder.decodeCommand("@tokenNonValido");
        }
        if (game.playerConnected(command.returnToken())){
            game.gameOut(game.tokenPlayer(command.returnToken()));
            return decoder.decodeCommand("@ok");
        }
        return decoder.decodeCommand("@no");
    }

    public Command playerList(Command command){
        if (!tokens.contains(command.returnToken())){
            return decoder.decodeCommand("@tokenNonValido");
        }
        if (game.returnActiveList().isEmpty()){
            return decoder.decodeCommand("@listaGiocatori");
        }
        string = "@listaGiocatori";
        for (int i = 0; i < game.returnActiveList().size(); i++){
            string = string.concat(",");
            string = string.concat(game.returnActiveList().get(i).returnId());
        }
        return decoder.decodeCommand(string);
    }

    public Command returnRanking(Command command){
        if (!tokens.contains(command.returnToken())){
            return decoder.decodeCommand("@tokenNonValido");
        }
        if (!game.returnPlayerList().isEmpty()){
            Ranking ranking = game.updateRanking();
            return decoder.decodeCommand(ranking.returnStringRanking());
        }
        return decoder.decodeCommand("@no");
    }

    public Command returnMapString(Command command){
        if (!tokens.contains(command.returnToken())){
            return decoder.decodeCommand("@tokenNonValido");
        }
        if (!game.returnActiveList().contains(game.tokenPlayer(command.returnToken()))){
            return decoder.decodeCommand("@nonInPartita");
        }
        if (game.tokenPlayer(command.returnToken()).returnType() != null){
            String mapString = "@mappaGenerale,{40,40},";
            for (int i = 0; i < 40; i++){
                for (int j = 0; j < 40; j++){
                    if (game.tokenPlayer(command.returnToken()).returnType().isInExploredVisual(game.returnMap().ReturnBox(i, j))){
                        if (game.returnMap().ReturnBox(i, j) instanceof GroundBox && 
                            ((GroundBox) game.returnMap().ReturnBox(i, j)).isVegetationPres() == null){
                            mapString = mapString.concat("[t]");
                        }
                        if (game.returnMap().ReturnBox(i, j) instanceof GroundBox && 
                            ((GroundBox) game.returnMap().ReturnBox(i, j)).isVegetationPres() != null){
                            mapString = mapString.concat("[v]");
                        }
                        if (game.returnMap().ReturnBox(i, j) instanceof WaterBox){
                            mapString = mapString.concat("[a]");
                        }
                    } else {
                        mapString = mapString.concat("[b]");
                    }
                }
                mapString = mapString.concat(";");
            }
            return decoder.decodeCommand(mapString);
        }
        return decoder.decodeCommand("@no");
    }

    public Command returnLocalVisual(Command command){
        if (!tokens.contains(command.returnToken())){
            return decoder.decodeCommand("@tokenNonValido");
        }
        dino = gameController.findDinoByName(command.returnParameter(1));
        if (dino == null){
            return decoder.decodeCommand("@idNonValido");
        }
        if (!game.returnActiveList().contains(game.tokenPlayer(command.returnToken()))){
            return decoder.decodeCommand("@nonInPartita");
        }
        return decoder.decodeCommand(gameController.localVisual(dino));
    }

    public Command dinosaurList(Command command){
        if (!tokens.contains(command.returnToken())){
            return decoder.decodeCommand("@tokenNonValido");
        }
        if (!game.playerConnected(command.returnToken())){
            return decoder.decodeCommand("@listaDinosauri,+");
        }
        string = "@listaDinosauri";
        for (int i = 0; i < game.tokenPlayer(command.returnToken()).returnType().returnDinosaurs().size(); i++){
            string = string.concat(",");
            string = string.concat(game.tokenPlayer(command.returnToken()).returnType().returnDinosaurs().get(i).ReturnName());
        }
        return decoder.decodeCommand(string);
    }

    public String returnDinosaurState(Command command){
        Dinosaur dinoTemp = gameController.findDinoByName(command.returnParameter(1));
        if (dinoTemp == null){
            return "";
        }
        String name = gameController.findPlayer(gameController.findType(dinoTemp)).returnId();
        String typeName = gameController.findType(dinoTemp).returnName();
        String typeTemp = null;
        if (gameController.findType(dinoTemp) instanceof ErbivorousType){
            typeTemp = "e";
        }
        if (gameController.findType(dinoTemp) instanceof CarnivorousType){
            typeTemp = "c";
        }
        if (game.tokenPlayer(command.returnToken()).returnType().returnDinosaurs().contains(dinoTemp)){
            return ",".concat(name).concat(",").concat(typeName).concat(",").concat(typeTemp).concat(",{").concat(String.valueOf(dinoTemp.getX())).concat(",").concat(String.valueOf(dinoTemp.getY())).concat("},").concat(String.valueOf(dinoTemp.ReturnSize())).concat(",").concat(String.valueOf(dinoTemp.ReturnEnergy())).concat(String.valueOf(dinoTemp.ReturntAge()));
        } else {
            return ",".concat(name).concat(",").concat(typeName).concat(",").concat(typeTemp).concat(",{").concat(String.valueOf(dinoTemp.getX())).concat(",").concat(String.valueOf(dinoTemp.getY())).concat("},").concat(String.valueOf(dinoTemp.ReturnSize()));
        }
    }

    public Command dinosaurState(Command command){
        if (!tokens.contains(command.returnToken())){
            return decoder.decodeCommand("@tokenNonValido");
        }
        dino = gameController.findDinoByName(command.returnParameter(1));
        if (dino == null){
            return decoder.decodeCommand("@idNonValido");
        }
        if (!game.returnActiveList().contains(game.tokenPlayer(command.returnToken()))){
            return decoder.decodeCommand("@nonInPartita");
        }
        string = "@statoDinosauro";
        return decoder.decodeCommand(string.concat(returnDinosaurState(command)));
    }
    
    public Command growthDinosaur(Command command){        
        if (!tokens.contains(command.returnToken())){
            return decoder.decodeCommand("@tokenNonValido");
        }
        dino = gameController.findDinoByName(command.returnParameter(1));
        if(dino==null){    
            return decoder.decodeCommand("@idNonValido");
        }
        if (!game.returnActiveList().contains(game.tokenPlayer(command.returnToken()))){
            return decoder.decodeCommand("@nonInPartita");
        }
        if(!game.tokenPlayer(command.returnToken()).equals(game.returnCurrentPlayer())){
            return decoder.decodeCommand("@nonIlTuoTurno");
        }
        int size = dino.ReturnSize();
        try{
            gameController.GrowDinosaur(dino);
            
        }
        catch(GrowDinosaurException gde){
            if(gde.returnNewSize()== size)
                return decoder.decodeCommand("@raggiuntaDimensioneMax");
            if(gde.returnNewSize()!= size)
                return decoder.decodeCommand("@ok");
        }
        catch(ActionNotPermittedException anpe){
            return decoder.decodeCommand("@raggiuntoLimiteMosseDinosauro");
        }
        catch(DinoDeathException dde){
            return decoder.decodeCommand("@mortePerInedia");
        }
        catch(TypeDeathException tde){
            return decoder.decodeCommand("@mortePerInedia");
        } 
    return decoder.decodeCommand("@no");
    }

    public Command dinosaurMovement(Command command){
        if (!tokens.contains(command.returnToken())){
            return decoder.decodeCommand("@tokenNonValido");
        }
        dino = gameController.findDinoByName(command.returnParameter(1));
        player = game.tokenPlayer(command.returnToken());
        if (dino == null){
            return decoder.decodeCommand("@idNonValido");
        }
        if (!game.returnCurrentPlayer().equals(player)){
            return decoder.decodeCommand("@nonIlTuoTurno");
        }
        if (!game.returnActiveList().contains(player)){
            return decoder.decodeCommand("@nonInPartita");
        }
        try {
            gameController.MoveDinosaur(dino, new Box(command.getX(), command.getY()));
        }
        catch (NotMovedException nme){
            return decoder.decodeCommand("@destinazioneNonValida");
        }
        catch (YetMovedException yme){
            return decoder.decodeCommand("@raggiuntoLimiteMosseDinosauro");
        }
        catch (DinoDeathException dde){
            return decoder.decodeCommand("@mortePerInedia");
        }
        catch (TypeDeathException tde){
            return decoder.decodeCommand("@mortePerInedia");
        } 
        catch (MoveDeathException mde){
            return decoder.decodeCommand("@mortePerInedia");
        } 
        catch (EatException ee){
            return decoder.decodeCommand("@ok");
        } 
        catch (MovedException me){
            return decoder.decodeCommand("@ok");
        } 
        catch (FightException fe){
            if (fe.returnDeadDino().equals(dino)){
                return decoder.decodeCommand("@ok,@combattimento,p");
            }
            if (!fe.returnDeadDino().equals(dino)){
                return decoder.decodeCommand("@ok,@combattimento,v");
            }

        }
        return decoder.decodeCommand("@no");  
    }

    public Command dinosaurEgg (Command command){
        if (!tokens.contains(command.returnToken())){
            return decoder.decodeCommand("@tokenNonValido");
        }
        dino = gameController.findDinoByName(command.returnParameter(1));
        if(dino==null){    
            return decoder.decodeCommand("@idNonValido");
        }
        if (!game.returnActiveList().contains(game.tokenPlayer(command.returnToken()))){
            return decoder.decodeCommand("@nonInPartita");
        }
        if(!game.tokenPlayer(command.returnToken()).equals(game.returnCurrentPlayer())){
            return decoder.decodeCommand("@nonIlTuoTurno");
        }
        try{
            gameController.layEgg(dino, command.returnParameter(1));
            
        }
        catch(ActionNotPermittedException anpe){
            return decoder.decodeCommand("@raggiuntoLimiteMosseDinosauro");
        }
        catch(DinoDeathException dde){
            return decoder.decodeCommand("@mortePerInedia");
        }
        catch(TypeDeathException tde){
            return decoder.decodeCommand("@mortePerInedia");
        }
        catch(EggDeposedException ede){
            string = "@ok";
            string = string.concat(",");
            string = string.concat(ede.returnEgg().ReturnName());
            
            return decoder.decodeCommand(string);
            }
        return decoder.decodeCommand("@no");
    }
    
    public Command passTurn (Command command){
        player = game.tokenPlayer(command.returnToken());
        if (!tokens.contains(command.returnToken())){
            return decoder.decodeCommand("@tokenNonValido");
        }
        if (!game.returnCurrentPlayer().equals(player)){
            return decoder.decodeCommand("@nonIlTuoTurno");
        }
        if (!game.returnActiveList().contains(player)){
            return decoder.decodeCommand("@nonInPartita");
        }
        nextTurn();
        return decoder.decodeCommand("@ok");
    }
    
    public Command nextTurn(){
        try{
            gameController.nextTurn();
        }catch(TypeDeathException tde){
            startTurn();
            return decoder.decodeCommand("@cambioTurno".concat(game.returnCurrentPlayer().returnId()));
        }
        startTurn();
        return decoder.decodeCommand("@cambioTurno".concat(game.returnCurrentPlayer().returnId()));
    }
    
    public void startTurn(){
        System.out.println("inizio");
        todo(10);
        System.out.println("fine");
    }
    public void todo( int seconds){
        timer = new Timer (  ) ;
        timer.schedule ( new TimerAction() , seconds*1000 ) ;
    }
    class TimerAction extends TimerTask {
        public void run (  ){
            nextTurn();
            timer.cancel (  ) ; //Terminate the thread
        }
    }
}