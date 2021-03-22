package Model.Blocks;

import Model.Position;

import java.awt.*;

public class GarbageCan extends Block{

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
}
