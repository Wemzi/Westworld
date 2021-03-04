package Model;

public abstract class Block {
    private static final int MAX_CONDITION=100;

    private BlockState state;
    private int buildingCost;
    private int upkeepCost;
    private int condition;
    /*private Coordi size;
    private Coordi pos;*/
    private double popularityIncrease;

    public Block(int buildingCost, int upkeepCost, double popularityIncrease,BlockState state) {
        this.buildingCost = buildingCost;
        this.upkeepCost = upkeepCost;
        this.popularityIncrease = popularityIncrease;
        this.state = state;
        condition=MAX_CONDITION;
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
}
