import Model.Blocks.Block;
import Model.Blocks.FreePlace;
import Model.Blocks.Game;
import Model.Blocks.GameType;
import Model.GameEngine;
import Model.Position;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class testGameEngine {

    private GameEngine engine;

    @Before
    public void createGameEngine(){
        engine=new GameEngine();
    }

    @Test
    public void buildBlock_ferrisWheel(){
        Position p=new Position(0,0,false);
        Game g=new Game(GameType.FERRISWHEEL,p);

        assertTrue(g.getSize().getX_asIndex()==2 && g.getSize().getY_asIndex()==2);
        assertTrue(engine.buildBlock(g));
        assertEquals(engine.getPg().blocks[0][0],g);
        assertEquals(engine.getPg().blocks[0][1],g);
        assertEquals(engine.getPg().blocks[1][0],g);
        assertEquals(engine.getPg().blocks[1][1],g);
    }

    @Test
    public void buildBlock_usedPlace(){
        Position p=new Position(0,0,false);
        Block b0=new Game(GameType.ROLLERCOASTER,p);
        assertTrue(engine.buildBlock(b0));
        assertEquals(engine.getPg().blocks[0][0],b0);
        assertEquals(engine.getPg().blocks[0][1],b0);
        assertEquals(engine.getPg().blocks[1][0],b0);
        assertEquals(engine.getPg().blocks[1][1],b0);
        assertEquals(engine.getPg().blocks[2][0],b0);
        assertEquals(engine.getPg().blocks[2][1],b0);


        Block b1=new Game(GameType.FERRISWHEEL,new Position(0,0,false));
        Block b2=new Game(GameType.FERRISWHEEL,new Position(0,1,false));
        Block b3=new Game(GameType.FERRISWHEEL,new Position(1,0,false));
        Block b4=new Game(GameType.FERRISWHEEL,new Position(1,1,false));
        Block b5=new Game(GameType.FERRISWHEEL,new Position(2,0,false));
        Block b6=new Game(GameType.FERRISWHEEL,new Position(2,1,false));

        assertFalse(engine.buildBlock(b1));
        assertFalse(engine.buildBlock(b2));
        assertFalse(engine.buildBlock(b3));
        assertFalse(engine.buildBlock(b4));
        assertFalse(engine.buildBlock(b5));
        assertFalse(engine.buildBlock(b6));
    }

    @Test
    public void buildBlock_demolishGame(){
        //build
        Position p=new Position(0,0,false);
        Block b=new Game(GameType.FERRISWHEEL,p);
        assertTrue(engine.buildBlock(b));
        assertEquals(engine.getPg().blocks[0][0],b);

        //demolish
        engine.demolish(b);
        assertTrue(engine.getPg().blocks[0][0] instanceof FreePlace);
    }
}
