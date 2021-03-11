package Model;

import Model.Blocks.Block;
import Model.Blocks.BlockState;
import Model.Blocks.Game;
import View.IndexPair;
import View.MainWindow2;

import java.util.Timer;
import java.util.TimerTask;

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

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                pg.setSeconds(pg.getSeconds()+1);
                if(pg.getSeconds() >= 60) {
                    pg.setSeconds(0);
                    pg.setMinutes(pg.getMinutes()+1);
                }
                if(pg.getSeconds() >= 60 && pg.getMinutes() >= 60) {
                    pg.setSeconds(0);
                    pg.setMinutes(0);
                    pg.setHours(pg.getHours()+1);
                }
                if(pg.getSeconds() >= 60 && pg.getMinutes() >= 60 && pg.getHours() >= 20) {
                    pg.setSeconds(0);
                    pg.setMinutes(0);
                    pg.setHours(8);
                    pg.setDays(pg.getDays()+1);
                }
                //Viewban lekérni
            }
        }, 1000, 1000);

        //kezdo test blokkok hozzaadasa
        //Game g=new Game( new IndexPair(2,3),MainWindow2.indexPairToCoord(2,2));
        //buildBlock(g);
    }


    public Playground getPg() { return pg; }

    /* Metódusok */
    void setTimerOff() { timer.cancel(); System.out.println("Timer leállítva!"); }
    void setTimerSpeed(int speedMs) { }

    /**
     * Metódus segítségével meg lehet építeni egy adott blockot.
     * Ha első építkezés sikeres, akkor berakjuk 1x az objektumot a listába, majd, ha a block nagyobb, mint...
     * ... 1x1, akkor for ciklussal megépítjük az összes többi blockot és behelyezzük a mátrixba.
     * @param block amit meg szeretnénk építeni
     * @return  true: Ha PG metódus true-t adott vissza és végigmegy a ciklus
     *          false: Ha PG metódus false-t adott vissza
     */
    public boolean buildBlock(Block block) {
        if(!(pg.buildBlock(block, coordToIndex(block.pos.posX), coordToIndex(block.pos.posY)))) return false;
        pg.getBuildedObjectList().add(block); System.out.println("BuiledObjectList-be bekerült a megépítendő block");

        for (int i = 1; i < block.size.i; i++) {
            for (int j = 1; j < block.size.j; j++) {
                return pg.buildBlock(block, coordToIndex(block.pos.posX)+i, coordToIndex(block.pos.posY)+j);
            }
        }
        return true;
    }


    /* Getterek / Setterek */
    BlockState getBlockState(Block b) { return pg.getBlockState(b); }

    public Playground getPlayground()      { return pg; }

    public int getPlayerMoney()            { return pg.getMoney(); }
    public int getPlayerDaysPassedBy()     { return pg.getDays(); }
    public double getPlayerPopularity()    { return pg.getPopularity(); }




}
