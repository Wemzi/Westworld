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
                pg.setMoney(pg.getMoney()+10);
                pg.setDays(pg.getDays()+1);
                System.out.println("10 mp eltelt! Játékos pénze megnövekedett 10-zel!");
            }
        }, 1000, 10000);

        //kezdo test blokkok hozzaadasa
        Game g=new Game(new IndexPair(2,3),MainWindow2.indexPairToCoord(2,2));
        buildBlock(g);
    }


    public Playground getPg() {
        return pg;
    }

    public String getDate(){return Integer.toString(pg.getDays());} //todo valami datumra hasonlito formatumot csinalni belole

    /* Metódusok */
    void setTimerOff() { timer.cancel(); System.out.println("Timer leállítva!"); }
    void setTimerSpeed(int speedMs) { }

    public void buildBlock(Block g){
        for (int i = 0; i < g.size.i; i++) {
            for (int j = 0; j < g.size.j; j++) {
                pg.blocks[coordToIndex(g.pos.posX)+i][coordToIndex(g.pos.posY)+j]=g;
            }
        }
    }


    /* Getterek / Setterek */
    BlockState getBlockState(Block b) { return pg.getBlockState(b); }

    public Playground getPlayground()      { return pg; }

    public int getPlayerMoney()            { return pg.getMoney(); }
    public int getPlayerDaysPassedBy()     { return pg.getDays(); }
    public double getPlayerPopularity()    { return pg.getPopularity(); }




}
