package Model;

import Model.Blocks.Block;
import Model.Blocks.BlockState;

import java.util.Timer;

public class GameEngine {
    /* Adattagok */
    private Timer timer;

    private Playground pg;
    /* További adattagok implementálása */

    /* Konstruktor */
    GameEngine() {
        pg = new Playground();
        timer = new Timer();
        // Adattagok inicializálása és GameEngine beállításai
    }

    /* Metódusok */
    void setSpeed(int speed) { }


    /* Getterek / Setterek */
     BlockState getBlockState(Block b) {
        return b.getState();
    }

    int getPlayerMoney()            { return pg.getMoney(); }
    int getPlayerDaysPassedBy()     { return pg.getDays(); }
    double getPlayerPopularity()    { return pg.getPopularity(); }




}
