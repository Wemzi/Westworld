package Model;

import Model.Blocks.Block;
import Model.Blocks.BlockState;
import Model.Blocks.Game;
import View.MainWindow2;

import java.util.Timer;

import static View.MainWindow2.coordToIndex;

public class GameEngine {
    /* Adattagok */
    private Timer timer;

    private Playground pg;
    /* További adattagok implementálása */

    /* Konstruktor */
    public GameEngine() {
        pg = new Playground();
        timer = new Timer();
        // Adattagok inicializálása és GameEngine beállításai

        //kezdo test blokkok hozzaadasa
        Game g=new Game(new Coord(MainWindow2.BOX_SIZE*2,2*MainWindow2.BOX_SIZE),MainWindow2.indexPairToCoord(2,2));
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
     * @param g a megépítendő blokk
     */
    void buildBlock(Block g){
        /*
         * todo: jó lenne ha a Block nevet csak 1x1-es dobozkákra használnánk és egy másik osztályt pl GameObject használnánk egy 3x1-es hullámvasútra
         * Azaz egy GameObject több Block -ból állna
         * Jelenleg egy 2x2re 4szer hívódik meg a kirajzolás. 4db 2x2es piros negyzet rajzolodik egymasra
         */

        for (int i = 0; i < coordToIndex(g.size.posX); i++) {
            for (int j = 0; j < coordToIndex(g.size.posY); j++) {
                pg.blocks[coordToIndex(g.pos.posX)+i][coordToIndex(g.pos.posY)+j]=g;
            }
        }
    }


    /* Getterek / Setterek */
    BlockState getBlockState(Block b) {
        return b.getState();
    }

    int getPlayerMoney()            { return pg.getMoney(); }
    int getPlayerDaysPassedBy()     { return pg.getDays(); }
    double getPlayerPopularity()    { return pg.getPopularity(); }



}
