/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import it.polimi.dinosaursisland.dinosauri.CarnivorousType;
import it.polimi.dinosaursisland.dinosauri.Carnivorous;
import it.polimi.dinosaursisland.partita.Player;
import it.polimi.dinosaursisland.partita.Score;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * Junit4 di Score
 */
public class ScoreTest {
    
    public ScoreTest() {
    }
    private static Score score;
    private static Player player;
    private static Carnivorous dino;
    private static Carnivorous dino2;
    private static CarnivorousType dinotype;
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        dino = new Carnivorous("carnivoro");
        dino2 = new Carnivorous("carnivoro2");
        dinotype = new CarnivorousType("name",dino);
        player = new Player("giocatore1","pass1",dinotype);
        score = new Score(player);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        dino = null;
        dino2 = null;
        dinotype = null;
        player = null;
        score = null;
    }
    
    @Test
    public void returnPlayerIdtest(){
        String idExpected = "giocatore1";
        assertEquals(idExpected,score.returnPlayerId());
    }        
    
    @Test
    public void returnPlayertest(){
        Player playerExpected = player;
        assertEquals(playerExpected,score.returnPlayer());
    }        
    
    @Test
    public void assignScoreTest(){
        dinotype.newDinosaur2(dino2, player);
        int scoreExpect = 4;
        assertEquals(scoreExpect,score.assignScore());
    }        

}
