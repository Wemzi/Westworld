package View;

import Model.Blocks.Block;
import Model.GameEngine;

import javax.swing.*;
import java.awt.*;

import static View.MainWindow2.indexToCoord;

/**
 *
 * @author Gabor
 */
public class GameField extends JPanel {
    private final GameEngine engine;

    public GameField(GameEngine engine) {
        setPreferredSize(new Dimension(600, 600));
        setBorder(BorderFactory.createLineBorder(Color.black));
        this.engine=engine;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gr = (Graphics2D) g;

        /*
        if(p1!=null && p2!=null){
            Graphics2D gr = (Graphics2D)g;

            gr.setColor(p1.color);
            gr.fillOval(p1.x-5, p1.y-5, 10, 10);
            for(Line l :p1.lines){gr.drawLine(l.x1, l.y1, l.x2, l.y2);}

            gr.setColor(p2.color);
            gr.fillOval(p2.x-5, p2.y-5, 10, 10);
            for(Line l :p2.lines){gr.drawLine(l.x1, l.y1, l.x2, l.y2);}
        }*/

        for(Block[] row : engine.getPg().blocks){
            for(Block b : row){
                if(b!=null){
                    gr.setColor(b.getColor());
                    gr.fillRect(b.pos.posX,b.pos.posY,indexToCoord(b.size.i),indexToCoord(b.size.j));
                }

            }
        }
    }

}

