package test;

import it.polimi.dinosaursisland.serializazion.DeSerialize;
import it.polimi.dinosaursisland.mappa.Map;
import it.polimi.dinosaursisland.dinosauri.CarnivorousType;
import it.polimi.dinosaursisland.dinosauri.Dinosaur;
import it.polimi.dinosaursisland.partita.Player;
import it.polimi.dinosaursisland.dinosauri.Carnivorous;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * Junit4Test della classe CarnivorousType 
 */
public class CarnivorousTypeTest {
    
    public CarnivorousTypeTest() {}

    private static CarnivorousType typeTest;
    private static Carnivorous trex;
    private static Carnivorous velociraptor;
    private static Player playerTest;
    private static Carnivorous dinoTrex;
    private static Map map;
    private static DeSerialize deserialize;
    
    @BeforeClass
    public static void setUpClass() throws Exception {

        playerTest = new Player("utente","pass");
        trex = new Carnivorous("trex");
        velociraptor = new Carnivorous("velociraptor");
        dinoTrex = new Carnivorous("dinoTrex");
        typeTest= new CarnivorousType("cattiviTrex",trex);
        deserialize = new DeSerialize ();
        map = (Map)deserialize.deSerializeIt("mappaDino");
    }
    
    
    @AfterClass
    public static void tearDownClass() throws Exception {
        typeTest= null;
        trex = null;
        playerTest = null;
    
    
    }
    
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void dinosTest(){
        int sizeTrue = 1;
        int numTrue = 1;
        Dinosaur dinoTrue = trex;
        assertEquals(sizeTrue,typeTest.returnDinosaurs().size());
        assertEquals(dinoTrue,typeTest.returnDinosaur(0));
        assertEquals(2,typeTest.returnAvailableAction());
        assertEquals(numTrue,typeTest.returnNumDinosaur());
    }
    
    @Test
    public void newDinosaur2Test(){
        typeTest.newDinosaur2(trex, playerTest);
        Dinosaur dinoExpected = trex;
        assertEquals(dinoExpected,typeTest.returnDinosaur(1));
        assertEquals(4,typeTest.returnAvailableAction());
    }
   
    @Test
    public void newDinosaurTest(){
        int numTrue = 3;
        typeTest.newDinosaur(dinoTrex.returnName(), playerTest);
        Dinosaur dinoExpected = dinoTrex;
        assertEquals(dinoExpected.returnName(),typeTest.returnDinosaur(2).returnName());
        assertEquals(6,typeTest.returnAvailableAction());
        assertEquals(numTrue,typeTest.returnNumDinosaur());
    }
    
    @Test
    public void findDinosaurTest(){
        Dinosaur dinoExpected = typeTest.returnDinosaur(2);
        assertNull(typeTest.findDinosaur(velociraptor.returnName()));
        assertEquals(trex,typeTest.findDinosaur(trex.returnName()));
        assertEquals(trex,typeTest.findDinosaur("trex"));
        assertNull(typeTest.findDinosaur("ciao"));
        assertEquals(dinoExpected,typeTest.findDinosaur(dinoTrex.returnName()));
        
    }
    @Test
    public void setVisualTest(){
        int sizeEx = 50;
        map.setDinosaur(trex, 1, 1);
        map.setDinosaur(dinoTrex, 3, 1);
        map.moveDinosaur(trex, map.returnBox(2, 2));
        map.moveDinosaur(trex, map.returnBox(4, 4));
        map.moveDinosaur(trex, map.returnBox(6, 6));
        trex.growth();
        typeTest.setFullVisual();
        typeTest.setExploredVisual();
        assertTrue(typeTest.isInFullVisual(map.returnBox(4,4)));
        assertFalse(typeTest.isInFullVisual(map.returnBox(2,3)));
        assertTrue(typeTest.isInFullVisual(map.returnBox(8,8)));
        assertTrue(typeTest.isInFullVisual(map.returnBox(6,6)));
        assertFalse(typeTest.isInFullVisual(map.returnBox(1,1)));
        assertFalse(typeTest.isInFullVisual(map.returnBox(17, 17)));
        assertEquals(sizeEx,typeTest.returnFullVisual().size());
    }
    
    @Test
    public void exploredVisualTest(){
        int sizeExFull = 98;
        int sizeExEx = 58;
        map.moveDinosaur(trex, map.returnBox(5,5));
        map.moveDinosaur(trex, map.returnBox(8,8));
        map.moveDinosaur(dinoTrex, map.returnBox(7,7));
        map.moveDinosaur(dinoTrex, map.returnBox(7,10));
        typeTest.setFullVisual();
        typeTest.setExploredVisual();
        assertEquals(sizeExFull,typeTest.returnFullVisual().size());
        assertEquals(sizeExEx,typeTest.returnExploredVisual().size());
        
        assertTrue(typeTest.isInExploredVisual(map.returnBox(8,4)));
        assertTrue(typeTest.isInExploredVisual(map.returnBox(4,4)));
        assertTrue(typeTest.isInExploredVisual(map.returnBox(4,8)));
        assertTrue(typeTest.isInExploredVisual(map.returnBox(11,11)));
        assertTrue(typeTest.isInExploredVisual(map.returnBox(8,8)));
        assertTrue(typeTest.isInExploredVisual(map.returnBox(8,7)));
        assertFalse(typeTest.isInExploredVisual(map.returnBox(1,7)));
        assertFalse(typeTest.isInExploredVisual(map.returnBox(1,1)));
        assertFalse(typeTest.isInExploredVisual(map.returnBox(21,27)));
        assertTrue(typeTest.isInExploredVisual(map.returnBox(5,8)));
        assertTrue(typeTest.isInExploredVisual(map.returnBox(9,5)));
          
    }
    
    @Test
    public void dinosaurDeadTest(){
        typeTest.newDinosaur2(velociraptor, playerTest);
        assertTrue(typeTest.dinosaursDead(velociraptor));
        assertNull(typeTest.findDinosaur(velociraptor.returnName()));
        assertEquals(6,typeTest.returnAvailableAction());
    }
    
    @Test
    public void leftTimeTest(){
        int timeExpected = 120;
        assertEquals(timeExpected,typeTest.leftTime());
        typeTest.decrementTime();
        typeTest.decrementTime();
        int time2Expected = 118;
        assertEquals(time2Expected,typeTest.leftTime());
    }        
   
    @Test
    public void extinguishTest(){
        assertTrue(typeTest.exist());
        typeTest.extinguish();
        assertFalse(typeTest.exist());
    }        
}   
