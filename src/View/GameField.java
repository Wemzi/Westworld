package View;

import Model.Blocks.Block;
import Model.Blocks.GarbageCan;
import Model.Blocks.Road;
import Model.GameEngine;
import Model.People.Visitor;
import Model.Playground;
import Model.Position;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Gabor
 */
public class GameField extends JPanel {
    private final GameEngine engine;

    private boolean mouseFollowing=false;
    private Block toBuild;

    public GameField(GameEngine engine) {
        setPreferredSize(new Dimension(600, 600));
        setBorder(BorderFactory.createLineBorder(Color.black));
        this.engine=engine;
    }

    public void disableMouseFollowing() {
        mouseFollowing=false;
    }

    public void enableMouseFollowing(Block size) {
        this.mouseFollowing =true;
        toBuild =size;
    }

    private static void paintBlocks(Graphics2D gr,GameEngine gameEngine){
        for(Block[] row : gameEngine.getPg().blocks){
            for(Block b : row){
                if(b!=null){
                    gr.setColor(b.getColor());
                    gr.fillRect(b.pos.getX_asPixel(),b.pos.getY_asPixel(),b.size.getX_asPixel(),b.size.getY_asPixel());
                    gr.setColor(Color.BLACK);
                    gr.drawRect(b.pos.getX_asPixel(),b.pos.getY_asPixel(),b.size.getX_asPixel(),b.size.getY_asPixel());

                    if(b instanceof Road && ((Road) b).isHasGarbageCan()){
                        gr.setColor(Color.GREEN);
                        gr.fillRect(b.pos.getX_asPixel(),b.pos.getY_asPixel(),b.size.getX_asPixel()/4,b.size.getY_asPixel()/4);

                    }
                }

            }
        }
    }

    private static void paintVisitors(Graphics2D gr,GameEngine gameEngine){
        for(Visitor v :gameEngine.getPg().getVisitors()){
            gr.setColor(Color.magenta);
            gr.fillOval(v.getPosition().getX_asPixel()+MainWindow2.BOX_SIZE/3,v.getPosition().getY_asPixel()+MainWindow2.BOX_SIZE/3,MainWindow2.BOX_SIZE/3,MainWindow2.BOX_SIZE/3);
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gr = (Graphics2D) g;
        paintBlocks(gr,engine);
        paintVisitors(gr,engine);

        if(mouseFollowing){
            Point p = getMousePosition();
            if(p!=null){
                Position where=Position.useMagicGravity(new Position(p.x,p.y,true));
                toBuild.pos=where;
                setPreviewColor(toBuild,gr,engine.getPg());

                gr.fillRect(where.getX_asPixel(),where.getY_asPixel(), toBuild.size.getX_asPixel(), toBuild.size.getY_asPixel() );

            }
        }
    }

    private static void setPreviewColor(Block toBuild, Graphics2D gr, Playground pg){
            if(pg.isBuildable(toBuild)){
                gr.setColor(toBuild.getColor());
            }else{
                gr.setColor(Color.BLACK);
            }
    }

}

