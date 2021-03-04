package Model;

public class Playground {
    /* Adattagok */
    //private ArrayList<Block> blocks;
    //private ArrayList<Person> visitors;
    //private ArrayList<Person> employees;
    private int money;
    private int days;
    private double popularity;
    /* További adattagok implementálása */

    /* Konstruktor */
    Playground() {
        // Adattagok inicializálása és GameEngine beállításai
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
