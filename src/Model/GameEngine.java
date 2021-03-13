package Model;

import Model.Blocks.Block;
import Model.Blocks.BlockState;
import Model.Blocks.FreePlace;

import java.util.Timer;
import java.util.TimerTask;


public class GameEngine {
    /* Adattagok */
    private Timer timer;
    private Playground pg;
    // TODO: Ki kellene találni, hogyan fogunk végigiterálni a Person-ökön, és meghívni a megfelelő metódusaikat
    // TODO: -||- A blokkokkal, cél a szimuláció elindítása.
    // TODO: A person-öket mozgatni kéne, annak függvényében, mire vágynak. (playfulness, hunger). Elég ha teleportálnak.
    // TODO: Kéne kezdőpénz, és le kell vonni az összeget, amikor építünk valamit. Legyen nagyon sok pénzünk kezdetben.
    // TODO: Jó lenne letesztelni a játékok run() metódusát, hogy működik-e a cooldown
    // TODO: Töltsük fel a pályát FreePlace-ekkel a játék kezdetekor
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
        //Game g=new Game( new Position(2,3,false),new Position(2,2,false));
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
    public boolean buildBlock(Block block) {//todo 1x1nél nagyobbra nem mukodik jol az ellenorzes, siman beleépít a nagyobb blokk egyik kockájába
        if(!(pg.buildBlock(block,block.pos.getX_asIndex(), block.pos.getY_asIndex()))) return false;
        pg.getBuildedObjectList().add(block); System.out.println("BuiledObjectList-be bekerült a megépítendő block");

        for (int i = 1; i < block.size.getX_asIndex(); i++) {
            for (int j = 1; j < block.size.getY_asIndex(); j++) {
                return pg.buildBlock(block, block.pos.getX_asIndex()+i, block.pos.getY_asIndex()+j);
            }
        }
        return true;
    }

    public void demolish(FreePlace b){//todo implement
        //b.pos helyre berakni ezt a free place
    }


    /* Getterek / Setterek */
    BlockState getBlockState(Block b) { return pg.getBlockState(b); }

    public Playground getPlayground()      { return pg; }

    public int getPlayerMoney()            { return pg.getMoney(); }
    public int getPlayerDaysPassedBy()     { return pg.getDays(); }
    public double getPlayerPopularity()    { return pg.getPopularity(); }




}
