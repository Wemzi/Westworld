package Model.Blocks;

import Model.Position;

import java.awt.*;



public class Decoration  extends Block {
    private DecType decorationType;

    @Deprecated
    public Decoration(int buildingCost, int upkeepCost, double popularityIncrease, BlockState state) {
        super(buildingCost, upkeepCost, popularityIncrease, state);
        this.decorationType = DecType.BUSH;
    }

    public Decoration(DecType type,Position pos){
        this.decorationType = type;
        if(type == DecType.BUSH)
        {
            this.buildingCost = 50;
            this.upkeepCost = 8;
            this.popularityIncrease=1.1;
            this.size = new Position(1,1,false);
            this.state=BlockState.UNDER_CONSTRUCTION;
            this.pos=pos;
        }
        else if(type == DecType.JUNGLETREE){
            this.buildingCost = 80;
            this.upkeepCost = 10;
            this.popularityIncrease=1.12;
            this.size = new Position(1,1,false);
            this.state=BlockState.UNDER_CONSTRUCTION;
            this.pos=pos;
        }
        else if(type == DecType.FLOWERGARDEN){
            this.buildingCost = 150;
            this.upkeepCost = 20;
            this.popularityIncrease=1.3;
            this.size = new Position(2,3,false);
            this.state=BlockState.UNDER_CONSTRUCTION;
            this.pos=pos;
        }
        else if(type == DecType.LAKE){
            this.buildingCost = 200;
            this.upkeepCost = 40;
            this.popularityIncrease=1.2;
            this.size = new Position(2,2,false);
            this.state=BlockState.UNDER_CONSTRUCTION;
            this.pos=pos;
        }
        else if(type == DecType.MONKEYCAGE){
            this.buildingCost = 140;
            this.upkeepCost = 8;
            this.popularityIncrease=1.1;
            this.size = new Position(2,1,false);
            this.state=BlockState.UNDER_CONSTRUCTION;
            this.pos=pos;
        }
        else throw new RuntimeException("Invalid type of decoration!");
    }



    @Override
    public Color getColor() {return Color.GREEN;}

        @Override
        public String toString() {
            return  "Decoration type: " + decorationType.toString() + " " + super.toString();
        }
    }
