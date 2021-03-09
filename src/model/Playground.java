package Model;

import Model.Blocks.*;
import Model.People.Person;
import View.MainWindow2;

import java.util.ArrayList;

import static View.MainWindow2.*;


/*
Mátrixot használjunk az ArrayListek helyett, sőt, ahol lehet, kerüljük a gyűjtemények,
osztályok használatát, mert jelentősen felgyorsíthatjuk
a program futását. Érdemes lehet még külön Thread-eket használni pl a Timereknek,
mert ha esetleg számításígényesebb dolgot végzünk közben, az megállíthatja a Timerünket.
Egy JPanel-t kéne használnunk, és ebben kellene az összes játékosokkal


Blokkok tudják magukról, hogy mekkorák, illetve tudjanak visszaadni egy színt, amit szükséges kirajzolni
Bal felső sarok, jobb alsó sarok

 */

public class Playground {
    /* Adattagok */
    public Block[][] blocks;
    private ArrayList<Person> visitors;
    private ArrayList<Person> employees;
    private int money;
    private int days;
    private double popularity;
    /* További adattagok implementálása */

    /* Konstruktor */
    public Playground() {
        // Adattagok inicializálása és GameEngine beállításai
        blocks = new Block[NUM_OF_ROWS][NUM_OF_COLS];//Létrehoz egy akkora tömböt, amekkora a UI-on létrejön
    }

    /* Metódusok */
    void startDay()         { }
    void endDay()           { }
    void update()           { }
    void updatePersons()    { }
    void updateBlocks()     { }

    /* Getterek / Setterek */
    public int getMoney()                           { return money; }
    public int getDays()                            { return days; }
    public double getPopularity()                   { return popularity; }

    public void setMoney(int money)                 { this.money = money; }
    public void setDays(int days)                   { this.days = days; }
    public void setPopularity(double popularity)    { this.popularity = popularity; }
}
