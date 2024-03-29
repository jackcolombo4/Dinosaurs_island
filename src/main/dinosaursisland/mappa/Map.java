package it.polimi.dinosaursisland.mappa;

import it.polimi.dinosaursisland.dinosauri.Dinosaur;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Map implements Serializable{
    /* Attributes */
    private Box[][] map = new Box[40][40];
    private ArrayList<Box> availableBox = new ArrayList(); //Contiene le caselle ancora disponibili per la costruzione della mappa
    private ArrayList<WaterBox> lakeBox = new ArrayList(0); //Contiene le caselle di un lago in fase di costruzione dello stesso
    private ArrayList<Carrion> carrionList = new ArrayList(0);
    private ArrayList<Vegetation> vegetationList = new ArrayList(0);
    private int i, j; //contatori
    private int ground = 1280;  //numero di caselle di terra disponibili per la creazione della mappa
    private int water = 320;    //numero di caselle di acqua disponibili per la creazione della mappa
    private int vegetation = 512;   //vegetazioni disponibili per la creazione della mappa
    private int carrion = 20;
    private int x, y; //interi per le posizioni dei Box
    private Object movement;

    /* Methods */
    public void CreateMap(){
        //Creates a map with only the Box, adding all other tiles to an availableBox
        for (i = 0; i < 40; i++){
            for (j = 0; j < 40; j++){
                map[39 - i][j] = new Box(i, j);
                availableBox.add(i * 40 + j, map[39 - i][j]);
            }
        }
        SetOcean();
        while (water > 5){
            CreateLake();
        }
        SetGround();
    }
    public void SetOcean(){
        //Create the edges of the map to set it to WaterBox, removing the Boxes that are adjacent to the availableBox
        for (i = 0; i < 40; i++){
            map[0][i] = new WaterBox(39, i);
            map[39][i] = new WaterBox(0, i);
            water = water - 2;
            availableBox.remove(map[0][i]);
            availableBox.remove(map[39][i]);
            availableBox.remove(map[1][i]);
            availableBox.remove(map[38][i]);
        }
        for (i = 1; i < 39; i++){
            map[i][0] = new WaterBox(39 - i, 0);
            map[i][39] = new WaterBox(39 - i, 39);
            water = water - 2;
            availableBox.remove(map[i][0]);
            availableBox.remove(map[i][39]);
            availableBox.remove(map[i][1]);
            availableBox.remove(map[i][38]);
        }
    }
    public void CreateLake(){
        //Creates the lakes and add them to the map
        int n = (int) (Math.random() * 10) + 5; //numero di caselle del lago            
        if (water > 15){
            int first = (int) (Math.random() * availableBox.size());  //Prima Box a random nell availableBox
            WaterBox temp = new WaterBox(availableBox.get(first));
            map[39 - temp.GetX()][temp.GetY()] = new WaterBox(temp);  //Aggiunta della Box alla mappa
            lakeBox.add((WaterBox) map[39 - temp.GetX()][temp.GetY()]);   //e alle caselle del lago

            availableBox.remove(first);  //rimozione dalle box disponibili
            water--;

            for (i = 1; i < n; i++){
                temp = new WaterBox(RandomAdjacent(lakeBox.get((int) (Math.random() * lakeBox.size()))));
                while (temp.x == 0 && temp.y == 0){
                    temp = new WaterBox(RandomAdjacent(lakeBox.get((int) (Math.random() * lakeBox.size()))));
                }
                map[39 - temp.x][temp.y] = temp;
                lakeBox.add(temp);
                availableBox.remove(temp);
                water--;
            }
            FreeLakeAdjacent(lakeBox);
        } else {
            int first = (int) (Math.random() * availableBox.size());
            WaterBox temp = new WaterBox(availableBox.get(first));
            map[39 - temp.GetX()][temp.GetY()] = new WaterBox(temp);
            lakeBox.add((WaterBox) map[39 - temp.GetX()][temp.GetY()]);
            
            availableBox.remove(first);
            water--;
            j = water;

            for (i = 1; i < j + 1; i++){
                temp = new WaterBox(RandomAdjacent(lakeBox.get((int) (Math.random() * lakeBox.size()))));
                while (temp.x == 0 && temp.y == 0){
                    temp = new WaterBox(RandomAdjacent(lakeBox.get((int) (Math.random() * lakeBox.size()))));
                }
                map[39 - temp.x][temp.y] = temp;
                lakeBox.add(temp);
                availableBox.remove(temp);
                water--;
            }
            FreeLakeAdjacent(lakeBox);
        }
    }

    public void FreeLakeAdjacent(ArrayList<WaterBox> lake){
        //Removes the tiles that are adjacent to the lake, from arraylist availableBox
        for (i = 0; i < lake.size(); i++){
            if (map[39 - lake.get(i).x - 1][lake.get(i).y] instanceof Box){
                availableBox.remove(map[39 - lake.get(i).x - 1][lake.get(i).y]);
            }
            if (map[39 - lake.get(i).x][lake.get(i).y + 1] instanceof Box){
                availableBox.remove(map[39 - lake.get(i).x][lake.get(i).y + 1]);
            }
            if (map[39 - lake.get(i).x + 1][lake.get(i).y] instanceof Box){
                availableBox.remove(map[39 - lake.get(i).x + 1][lake.get(i).y]);
            }
            if (map[39 - lake.get(i).x][lake.get(i).y - 1] instanceof Box){
                availableBox.remove(map[39 - lake.get(i).x][lake.get(i).y - 1]);
            }
        }
    }

    public Box RandomAdjacent(WaterBox box){
        //Returns a random adjacent tiles, near to the tile passed as argument when the map was built
        int n = (int) (Math.random() * 3);
        switch (n){
            case 0: {
                if (availableBox.contains(map[39 - box.x - 1][box.y])){
                    return map[39 - box.x - 1][box.y];
                } else {
                    return null;
                }
            }
            case 1: {
                if (availableBox.contains(map[39 - box.x][box.y + 1])){
                    return map[39 - box.x][box.y + 1];
                } else {
                    return null;
                }
            }
            case 2: {
                if (availableBox.contains(map[39 - box.x + 1][box.y])){
                    return map[39 - box.x + 1][box.y];
                } else {
                    return null;
                }
            }
            case 3: {
                if (availableBox.contains(map[39 - box.x][box.y - 1])){
                    return map[39 - box.x][box.y - 1];
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public void SetGround(){
        //Adds on the map all tiles of ground (caselle di terra)
        for (i = 0; i < 40; i++){
            for (j = 0; j < 40; j++){
                if (map[i][j] instanceof WaterBox){
                } else {
                    map[i][j] = new GroundBox(map[i][j]);
                    ground--;
                }
            }
        }
        SetVegetation();
        SetCarrion();

    }

    public void SetVegetation(){
        //Adds random vegetations
        while (vegetation != 0){
            i = (int) (Math.random() * 39);
            j = (int) (Math.random() * 39);

            if (map[i][j] instanceof GroundBox){

                GroundBox temp = new GroundBox(map[i][j]);
                Vegetation vegeTemp = new Vegetation();
                vegetationList.add(vegeTemp);
                temp.setVegetationPres(vegeTemp);
                map[i][j] = temp;
                temp = null;
                vegetation--;
                
            }

        }
    }

    public void SetCarrion(){
        //Adds random carrions
        while (carrion != 0){
            i = (int) (Math.random() * 39);
            j = (int) (Math.random() * 39);

            if (map[i][j] instanceof GroundBox){

                GroundBox temp = new GroundBox(map[i][j]);
                Carrion tempCarr = new Carrion(temp.GetX(), temp.GetY());
                temp.setCarrionPres(tempCarr);
                carrionList.add(tempCarr);
                map[i][j] = temp;
                temp = null;
                carrion--;

            }
        }
    }

    public Object moveDinosaur(Dinosaur dino, Box box){
        int x = dino.getX();
        int y = dino.getY();
        movement = dino.Movement(this, box);

        if (movement != null){
            ((GroundBox) ReturnBox(x, y)).moveDinosaurAway(); //svuota la casella corrente
            ((GroundBox) ReturnBox(dino.getX(), dino.getY())).setDinosaurPres(dino); //assegna il dinosauro alla casella di destinazione
            dino.SetVisual(this);

            return movement;
        }
        return movement;
    }

    public boolean setDinosaur(Dinosaur dino, int x, int y){
        if (ReturnBox(x, y) instanceof GroundBox && ((GroundBox) ReturnBox(x, y)).isDinosaurPres() == null){
            ((GroundBox) ReturnBox(x, y)).setDinosaurPres(dino); //assegna il dinosauro alla casella
            dino.setPosition(ReturnBox(x, y));
            return true;
        }
        return false;
    }

    public void removeDinosaur(Dinosaur dino){
        ((GroundBox) ReturnBox(dino.getX(), dino.getY())).moveDinosaurAway();
    }

    public void PrintAvailable(){
        //Displays the list of the available tiles
        System.out.println();
        System.out.print(availableBox);
    }

    public void PrintMap(){
        for (i = 0; i < 40; i++){
            System.out.println();
            for (j = 0; j < 40; j++){
                if (map[i][j] instanceof GroundBox){
                    GroundBox temp = (GroundBox) map[i][j];
                    if (temp.isVegetationPres() != null){
                        System.out.printf("H "/*"%d,%d | ",map[i][j].x,map[i][j].y*/);
                    } else if (temp.isCarrionPres() != null){
                        System.out.printf("A ");
                    } else {
                        System.out.printf(". ");
                    }
                } else if (map[i][j] instanceof WaterBox){
                    System.out.printf("  "/*"%d,%d | ",map[i][j].x,map[i][j].y*/);
                } else if (map[i][j] instanceof Box){
                    System.out.printf("  "/*"%d,%d | ",map[i][j].x,map[i][j].y*/);
                }
            }
        }
    }

    public void setRandomCarrion(){
        Carrion carrionTemp = new Carrion();
    }
    public Box ReturnBox(int x, int y){
        return map[39 - x][y];
    }
    
    public Box returnRandomAvailableBox(){
        //Chooses a random tile among the walkable ones 
        int x = (int) (Math.random() * 39);
        int y = (int) (Math.random() * 39);
        while (ReturnBox(x, y) instanceof WaterBox || 
            ((GroundBox) ReturnBox(x, y)).isDinosaurPres() != null || 
            ((GroundBox) ReturnBox(x, y)).isCarrionPres() != null || 
            ((GroundBox) ReturnBox(x, y)).isVegetationPres() != null){
            x = (int) (Math.random() * 39);
            y = (int) (Math.random() * 39);
        }
        return ReturnBox(x, y);
    }
    
    public ArrayList<Carrion> returnCarrions(){
        return carrionList;
    }

    public ArrayList<Carrion> newCarrion(Carrion oldCarrion){
        carrionList.remove(oldCarrion);
        ((GroundBox) ReturnBox(oldCarrion.getX(), oldCarrion.getY())).setCarrionPres(null);
        Box tempBox = returnRandomAvailableBox();
        Carrion tempCarrion = new Carrion(tempBox.GetX(), tempBox.GetY());
        ((GroundBox) tempBox).setCarrionPres(tempCarrion);
        carrionList.add(tempCarrion);
        return carrionList;
    }

    public void carrionDegradation(){
        for (i = 0; i < carrionList.size(); i++){
            Carrion carrion = carrionList.get(i);
            if (!carrion.ReduceEnergy()){
                newCarrion(carrion);
            }
        }
    }

    public void vegetationGrowth(){
        for (i = 0; i < vegetationList.size(); i++){
            vegetationList.get(i).GrowEnergy();
        }
    }
    public ArrayList<Box> returnAvailable(){
        return availableBox;
    }
    public int returnVegetation(){
        return vegetation;
    }
    public ArrayList<Vegetation> returnVegeList(){
        return vegetationList;
    }
}