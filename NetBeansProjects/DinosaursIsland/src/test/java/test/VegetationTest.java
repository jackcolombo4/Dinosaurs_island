package test;

import it.polimi.dinosaursisland.mappa.Vegetation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * Junit4 Test per Vegetation
 */
public class VegetationTest {
    
    public VegetationTest() {
    }
    
    private static Vegetation vegetation;
    @BeforeClass
    public static void setUpClass() throws Exception {
    vegetation = new Vegetation();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    vegetation = null;
    }
    
    @Test
    public void growthEnergy(){
        System.out.println(vegetation.returnEnergy());
        int energyExpected = vegetation.returnEnergy();
        int energyConstant = (vegetation.returnEnergy()*20/100);
        vegetation.growEnergy();
        assertEquals(energyExpected,vegetation.returnEnergy());
        vegetation.setEnergy(100);
        int energyTemp = vegetation.returnEnergy();
        vegetation.growEnergy();
        assertEquals(energyConstant + energyTemp ,vegetation.returnEnergy());
}
}
    
