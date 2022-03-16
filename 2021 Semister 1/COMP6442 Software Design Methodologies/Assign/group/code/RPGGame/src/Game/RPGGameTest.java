package Game;

import controllers.GameController;
import controllers.ItemController;
import controllers.MonsterController;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class RPGGameTest {

    @BeforeClass
    public void setUp() throws Exception {
        RPGGame rpgGame = new RPGGame();
    }

    @AfterClass
    public void tearDown() throws Exception {
        RPGGame rpgGame = null;
    }

    @Test(timeout=1000)
    public void run() throws Exception{
        try {
            RPGGame rpgGame;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test(timeout=1000)
    public void processCmd1(){ assertFalse(GameController.processCmd("10")); }


    @Test(timeout = 1000)
    public void MonsterFight(){ assertFalse(MonsterController.fight(null)); }



    //@Test(timeout = 1000)
    /*public void ItemControllerStart(){
        Method method = ItemController.getDeclaredMethod();
        Field field;
        try{
            field = a.getDeclaredField("state");
            field.setAccessible(true);
            a.useItem(1);
        }catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | InstantiationException e){
             e.printStackTrace();
        }
        RPGGame.state(1);
    }*/



}