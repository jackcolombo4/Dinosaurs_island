
package test;

import it.polimi.dinosaursisland.mappa.Carrion;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * Junit4 test dei metodi Carrion
 */
public class CarrionTest {
    
    public CarrionTest(){}
    
    private static Carrion carrion;
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        carrion = new Carrion(1,1);
    
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        carrion = null;
    }
    
    @Test
    public void reduceEnergyTest(){
        assertTrue(carrion.reduceEnergy());
        carrion.setEnergy(0);
        assertFalse(carrion.reduceEnergy());
    }      
    
    @Test
    public void setEnergyTest(){
        int energyExpected=100;
        carrion.setEnergy(100);
        assertEquals(energyExpected,carrion.returnEnergy());
    }        
}