package test;

import it.polimi.dinosaursisland.dinosauri.Carnivorous;
import it.polimi.dinosaursisland.mappa.*;
import it.polimi.dinosaursisland.serializazion.DeSerialize;



import org.junit.AfterClass;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


//mancano CreateLake,FreeLakeAdjacent
    public class MapTest {
        
    static Map mapTest;
    static Box temp;
    static Box temp1;
    static Carnivorous dinoTest = new Carnivorous(2,10);    
    static Carnivorous dinoTest1 = new Carnivorous(1,1);    
    static DeSerialize deSeria;
    Map mapDino = (Map)deSeria.deSerializeIt("mappaDino");

    @BeforeClass
    public static void setUpClass() {
        mapTest = new Map();
        temp = new Box();
        temp1 = new Box();
        deSeria = new DeSerialize();
        
    }
    @AfterClass
    public static void tearDownClass(){
        mapTest = null;
        temp = null;
    }
    
   
    @Test
    public void CounterTest() {
        int contWaterExpected = 320;
        int contGroundExpected = 1280;
        int contWater = 0;
        int contGround = 0;
        int groundEx = 0;
        int waterEx = 0;
        mapTest.createMap();
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 40; j++) {
                if (mapTest.returnBox(i, j) instanceof WaterBox) {
                    contWater++;
                }
                else    
                    contGround++;
                          
                }
            }    
        
        assertEquals(waterEx,mapTest.returnWater());
        assertEquals(contWaterExpected, contWater);
        assertEquals(contGroundExpected, contGround);
        assertEquals(groundEx,mapTest.retrunGround());
        
    }
    
    
   
    @Test
    public void availableTest(){
        assertNotNull(mapTest.returnAvailable().size());
    }
    
    @Test
    public void CarrionTest(){
        int carrionExpected = 20;
        int carrionCont = 0;
        int vegetationCont = 0;
        int vegetationExpected = 512;
        
        for(int i = 1;i<39;i++){
          for(int j=1;j<39;j++){
              Box temp = mapTest.returnBox(i, j);
                if(temp instanceof GroundBox){
                  if(((GroundBox)temp).isCarrionPres()!= null){
                                    carrionCont++;
                    }
                }
             }
           }
       assertEquals(carrionExpected, carrionCont);
    }
    
    
    @Test
    public void moveTest(){
        
        mapDino.printMap();
        dinoTest1.setEnergy(20);
        mapDino.setDinosaur(dinoTest, dinoTest.getX(), dinoTest.getY());
        mapDino.setDinosaur(dinoTest1, dinoTest1.getX(), dinoTest1.getY());
        
        assertEquals(dinoTest1,mapDino.moveDinosaur(dinoTest1, mapDino.returnBox(4,4)));
        Object movement = mapDino.returnBox(2,12);
        temp = mapDino.returnBox(2, 10);
        assertEquals(dinoTest,((GroundBox)temp).isDinosaurPres());
        assertEquals(movement,mapDino.moveDinosaur(dinoTest, mapDino.returnBox(2,12)));
        assertNull(((GroundBox)temp).isDinosaurPres());
        temp1 = mapDino.returnBox(2, 12);
        assertEquals(dinoTest,((GroundBox)temp1).isDinosaurPres());
        dinoTest.setAction();
        assertEquals(mapDino.returnBox(1, 9),mapDino.moveDinosaur(dinoTest, mapDino.returnBox(1,9)));
        assertNull(mapDino.moveDinosaur(dinoTest, mapDino.returnBox(2,9)));
        
    }
    
    @Test
    public void setDinoTest(){
        assertTrue(mapDino.setDinosaur(dinoTest1, dinoTest1.getX(),dinoTest1.getY()));
        assertFalse(mapDino.setDinosaur(dinoTest1, 3,9));
    }
    
    @Test
    public void removeTest(){
       mapDino.setDinosaur(dinoTest, 2,10);
       temp = mapDino.returnBox(2, 10);
       assertEquals(dinoTest,((GroundBox)temp).isDinosaurPres());
       mapDino.removeDinosaur(dinoTest);
       assertNull(((GroundBox)temp).isDinosaurPres());
       
    }
    
    @Test
    public void vegeTest(){
        Map map3 = new Map();
        map3.createMap();
        int numExpected = 0;
        int sizeExpected = 512; 
        System.out.println(map3.returnVegeList().size());
        System.out.println(map3.returnVegetation());
        
        assertEquals(numExpected, map3.returnVegetation());
        assertEquals(sizeExpected, map3.returnVegeList().size());
    
    }
    @Ignore
    @Test
    public void vegetationTest(){
        
        int vegetationExpeceted = 512;
        int vegetationCont = 0;
        for(int i = 1;i<39;i++){
          for(int j=1;j<39;j++){
              Box temp = mapTest.returnBox(i, j);
              if(temp instanceof GroundBox){
                if(((GroundBox)temp).isVegetationPres()!= null){
                                    vegetationCont++;
                  }
                }
            }
        }
        assertEquals(vegetationExpeceted, vegetationCont);
    }
    @Test
    public void OceanTest(){
        for(int i= 0; i<40;i++)
            for(int j=0;j<40;j++)
                if(i==0 || j==0 || i==39 || j==39){
                    assertTrue(mapTest.returnBox(i, j) instanceof WaterBox);
                    assertFalse(mapTest.returnAvailable().contains(mapTest.returnBox(i, j)));
                    
                }
    } 
    
    
    
    @Ignore
    @Test
    public void FreeLakeAdjacentTest(){
        
    }
    @Ignore
    @Test
    public void SetVegetationTest(){
        int numExpected = 0;
        assertEquals(numExpected,mapTest.returnVegetation());
    }        
    @Ignore
    @Test
    public void SetCarrionTest(){
        int numExpected = 0;
        assertEquals(numExpected,mapTest.returnCarrion());
    }
    @Ignore
    @Test
    public void SetDinosaur(){
        assertTrue(mapTest.setDinosaur(dinoTest, 7, 4));
        
    
    }
    @Ignore
    @Test
    public void SetDinosaur2(){
        int x;
        int y;
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 40; j++) {
                temp = mapTest.returnBox(i, j);
                if (temp instanceof WaterBox) {
                    assertFalse(mapTest.setDinosaur(dinoTest, temp.returnX(), temp.returnY()));
                }
            }
        }
    }
    }


    

    










