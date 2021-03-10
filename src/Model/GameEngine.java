package Model;

import Model.Blocks.Block;
import Model.Blocks.BlockState;
import Model.Blocks.Game;
import View.IndexPair;
import View.MainWindow2;

import java.util.Timer;

import static View.MainWindow2.coordToIndex;

public class GameEngine {
    /* Adattagok */
    private Timer timer;
    private Playground pg;


    /* Konstruktor */
    public GameEngine() {
        pg = new Playground();
        timer = new Timer();
        // Adattagok inicializálása és GameEngine beállításai

        //kezdo test blokkok hozzaadasa
        Game g=new Game(new IndexPair(2,2),MainWindow2.indexPairToCoord(2,2));
        buildBlock(g);
    }

    public Playground getPg() {
        return pg;
    }

    /* Metódusok */
    void setSpeed(int speed) { }

    /**
     * A paraméterként kapott Blockot beleteszi a playground blokk mátrixába.
     * Ha a blokk nem 1x1-es akkor belekerül többször is a mátrixba a referenciája.
     * @param block a megépítendő blokk
     */
    public void buildBlock(Block block){
        /*
         * todo: jó lenne ha a Block nevet csak 1x1-es dobozkákra használnánk és egy másik osztályt pl GameObject használnánk egy 3x1-es hullámvasútra
         * Azaz egy GameObject több Block -ból állna
         * Jelenleg egy 2x2re 4szer hívódik meg a kirajzolás. 4db 2x2es piros negyzet rajzolodik egymasra
         */
        IndexPair size = new IndexPair(3,3 );
        IndexPair pos = new IndexPair(0, 0);
        for (int i = 0; i < block.size.i; i++)
            for (int j = 0; j < block.size.j; j++) {
                pos.setIndexPair(coordToIndex(block.pos.posX)+i, coordToIndex(block.pos.posY)+j);
                pg.buildBlock(pos, size, block);
            }
    }


    /* Getterek / Setterek */
    BlockState getBlockState(Block b) { return pg.getBlockState(b); }

    public Playground getPlayground()      { return pg; }

    public int getPlayerMoney()            { return pg.getMoney(); }
    public int getPlayerDaysPassedBy()     { return pg.getDays(); }
    public double getPlayerPopularity()    { return pg.getPopularity(); }




}
