package Model;

import Model.Blocks.Block;
import Model.Blocks.BlockState;
import Model.People.Person;
import View.IndexPair;

import java.awt.*;

import static View.MainWindow2.NUM_OF_COLS;
import static View.MainWindow2.NUM_OF_ROWS;


/*
Blokkok tudják magukról, hogy mekkorák, illetve tudjanak visszaadni egy színt, amit szükséges kirajzolni
Bal felső sarok, jobb alsó sarok

 */

public class Playground {
    /* Adattagok */
    public Block[][] blocks;
    private Person[] visitors;
    private Person[] employees;
    private int money;
    private int days;
    private double popularity;
    /* További adattagok implementálása */

    /* Konstruktor */
    public Playground() {
        // Adattagok inicializálása és GameEngine beállításai
        blocks = new Block[NUM_OF_ROWS][NUM_OF_COLS];//Létrehoz egy akkora tömböt, amekkora a UI-on létrejön

        money = 0;
        days = 0;
        popularity = 0;
    }
    public Playground(int num_of_rows, int num_of_cols, int money, int days, int popularity) {
        blocks = new Block[num_of_rows][num_of_cols];
        this.money = money;
        this.days = days;
        this.popularity = popularity;
    }


    /* Metódusok */
    /* Ez nem jo
    void buildBlock(IndexPair pos, IndexPair size, Block block) {// a blokk tudja magarol, hogy hol kezdodik es mekkora, ahogy az engineben van az s
        for(int i = 0; i < size.i; i++) {
            for(int j = 0; j < size.j; j++) {
                blocks[pos.i+i][pos.j+j] = block;
            }
        }
    }*/

    void startDay()         { }
    void endDay()           { }
    void update()           { }
    void updatePersons()    { }
    void updateBlocks()     { }

    /* Getterek / Setterek */
    public int getMoney()                           { return money; }
    public int getDays()                            { return days; }
    public double getPopularity()                   { return popularity; }

    public BlockState getBlockState(Block block)    { return block.getState(); }
    public Color getColor(Block block)              { return block.getColor(); }
    public Block[][] getBlocks()                    { return blocks; }
    public Block getBlockByPos(IndexPair pos)       { return blocks[pos.i][pos.j]; }

    public void setMoney(int money)                 { this.money = money; }
    public void setDays(int days)                   { this.days = days; }
    public void setPopularity(double popularity)    { this.popularity = popularity; }
}
