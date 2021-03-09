package Model.Blocks;

import Model.Coord;

import java.awt.*;

public abstract class Block {
    private static final int MAX_CONDITION=100;

    private BlockState state;
    private int buildingCost;
    private int upkeepCost;
    private int condition;
    public final Coord size; //3 blokk szeles es 2 blokk magas
    public final Coord pos; //bal felso eleme hol van
    private double popularityIncrease;

    public Block(int buildingCost, int upkeepCost, double popularityIncrease, BlockState state, Coord size, Coord pos) {
        this.buildingCost = buildingCost;
        this.upkeepCost = upkeepCost;
        this.popularityIncrease = popularityIncrease;
        this.state = state;
        this.size = size;
        this.pos = pos;
        condition=MAX_CONDITION; // :) Brányi
    }

    public Block(int buildingCost, int upkeepCost, double popularityIncrease, BlockState state) {
        this.buildingCost = buildingCost;
        this.upkeepCost = upkeepCost;
        this.popularityIncrease = popularityIncrease;
        this.state = state;
        this.size = new Coord(1,1);
        this.pos = new Coord(0,0);
        condition=MAX_CONDITION; // :) Brányi
    }

    //Methods:
    public void showInfoPanel(){}

    public BlockState getState() {
        return state;
    }

    public int getBuildingCost() {
        return buildingCost;
    }

    public int getUpkeepCost() {
        return upkeepCost;
    }

    public int getCondition() {
        return condition;
    }

    public double getPopularityIncrease() {
        return popularityIncrease;
    }

    public Color getColor(){return Color.red;}
}
