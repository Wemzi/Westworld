package Model.Blocks;

import Model.Position;
import View.OneColorSpriteManager;
import View.SpriteManager;

import java.awt.*;
import java.util.Objects;

public class GarbageCan extends Block{
    private static SpriteManager spriteManager;

    @Override
    public Position getPos() {
        return pos;
    }

    @Override
    public void setPos(Position pos) {
        this.pos = pos;
    }

    @Override
    public Color getColor() {
        return Color.GREEN;
    }

    @Override
    public String getName(){return "Garbage Can";}

    @Override
    protected SpriteManager getSpriteManager() {
        if(Objects.isNull(spriteManager)){spriteManager=new OneColorSpriteManager(getColor(),getSize());}
        return spriteManager;
    }
}
