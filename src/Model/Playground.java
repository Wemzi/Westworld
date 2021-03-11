package Model;

import Model.Blocks.Block;
import Model.Blocks.BlockState;
import Model.People.Person;
import View.IndexPair;

import java.awt.*;
import java.util.ArrayList;

import static View.MainWindow2.NUM_OF_COLS;
import static View.MainWindow2.NUM_OF_ROWS;


/*
Blokkok tudják magukról, hogy mekkorák, illetve tudjanak visszaadni egy színt, amit szükséges kirajzolni
Bal felső sarok, jobb alsó sarok

 */

public class Playground {
    /* Adattagok */
    public Block[][] blocks;
    private ArrayList<Block> buildedObjects;
    private Person[] visitors;
    private Person[] employees;
    private int money;
    private int days, hours, minutes, seconds;
    private double popularity;
    /* További adattagok implementálása */

    /* Konstruktor */
    public Playground() {
        // Adattagok inicializálása és GameEngine beállításai
        blocks = new Block[NUM_OF_ROWS][NUM_OF_COLS];//Létrehoz egy akkora tömböt, amekkora a UI-on létrejön
        buildedObjects = new ArrayList<>();

        money = 0;
        days = 1; hours = 8; minutes = 0; seconds = 0;
        popularity = 0;
    }
    public Playground(int num_of_rows, int num_of_cols, int money, int days, int popularity) {
        blocks = new Block[num_of_rows][num_of_cols];
        buildedObjects = new ArrayList<>();
        this.money = money;
        this.days = days;
        this.popularity = popularity;
    }


    /* Metódusok */

    /**
     * Metódus segítségével X,Y pozícióba lehet egy objektumot építeni
     * @param block: Megépítendő block
     * @param posX: GE-ből megkapott X pozíció ahová építeni kell
     * @param posY: GE-ből megkapott Y pozíció ahová építeni kell
     * @return  false: HA adott blockon már van valami építve
     *          true: Ha adott blockon nincs még semmi építve
     */
    public boolean buildBlock(Block block, int posX, int posY) {
        if(blocks[posX][posY] != null) return false;
        blocks[posX][posY] = block;
        return true;
    }

    void startDay()         { }
    void endDay()           { }
    void update()           { }
    void updatePersons()    { }
    void updateBlocks()     { }

    /* Getterek / Setterek */
    public int getMoney()                           { return money; }
    public int getHours()                           { return hours; }
    public int getMinutes()                         { return minutes; }
    public int getSeconds()                         { return seconds; }
    public int getDays()                            { return days; }
    public double getPopularity()                   { return popularity; }

    public BlockState getBlockState(Block block)    { return block.getState(); }
    public Color getColor(Block block)              { return block.getColor(); }
    public Block[][] getBlocks()                    { return blocks; }
    public Block getBlockByPos(IndexPair pos)       { return blocks[pos.i][pos.j]; }
    public ArrayList<Block> getBuildedObjectList()  { return buildedObjects; }

    public void setMoney(int money)                 { this.money = money; }
    public void setDays(int days)                   { this.days = days; }
    public void setHours(int hours)                 { this.hours = hours; }
    public void setMinutes(int minutes)             { this.minutes = minutes; }
    public void setSeconds(int seconds)             { this.seconds = seconds; }
    public void setPopularity(double popularity)    { this.popularity = popularity; }

    public String dateToString() {
        return  " Day: " + this.days +
                " Hour: " + this.hours +
                " Minutes: " + this.minutes +
                " Seconds: " + this.seconds;
    }
}
