package test;


import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

/**
 *  JunitCore di tutti i Test .
 */
public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		Result result = JUnitCore.runClasses(DinosaursTest.class);
		Result result1 = JUnitCore.runClasses(GameControllerTest.class);
		Result result2 = JUnitCore.runClasses(GameTest.class);
		Result result3 = JUnitCore.runClasses(MapTest.class);
		Result result4 = JUnitCore.runClasses(BoxTest.class);
		Result result5 = JUnitCore.runClasses(CarnivorousTypeTest.class);
		Result result6 = JUnitCore.runClasses(ErbivorousTypeTest.class);
		Result result7 = JUnitCore.runClasses(DecoderTest.class);
		Result result8 = JUnitCore.runClasses(CarrionTest.class);
		Result result9 = JUnitCore.runClasses(ScoreTest.class);
		Result result10 = JUnitCore.runClasses(PlayerTest.class);
		Result result11 = JUnitCore.runClasses(VegetationTest.class);
		Result result12 = JUnitCore.runClasses(CommandTest.class);
                
		System.out.println(result.getRunCount() + " test eseguiti in " + + result.getRunTime() + " ms");
                System.out.println();
		if         (result.wasSuccessful()) System.out.println("OK!test eseguito con successo!");
		else            System.out.println("Sono falliti " + result.getFailureCount() + " test");
	}

}