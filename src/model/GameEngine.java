package Model;

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
    /* int getBlockState(Block b) {
        if (b.state == underConstruction) {
            return 1;
        }
        else if (b.state == underRepair) {
            return 2;
        }
        else if (b.state == used) {
            return 3;
        }
        else if(b.state == free) {
            return 0;
        }
        else {
            return -1;
        }
    } */

    int getPlayerMoney()            { return pg.getMoney(); }
    int getPlayerDaysPassedBy()     { return pg.getDays(); }
    double getPlayerPopularity()    { return pg.getPopularity(); }




}
