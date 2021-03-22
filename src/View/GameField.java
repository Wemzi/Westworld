package View;

import Model.Blocks.Block;
import Model.GameEngine;
import Model.Position;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Gabor
 */
public class GameField extends JPanel {//todo serviceArea throws exceptions
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gr = (Graphics2D) g;

        for(Block[] row : engine.getPg().blocks){
            for(Block b : row){
                if(b!=null){
                    gr.setColor(b.getColor());
                    gr.fillRect(b.pos.getX_asPixel(),b.pos.getY_asPixel(),b.size.getX_asPixel(),b.size.getY_asPixel());
                    gr.setColor(Color.BLACK);
                    gr.drawRect(b.pos.getX_asPixel(),b.pos.getY_asPixel(),b.size.getX_asPixel(),b.size.getY_asPixel());
                }

            }
        }

        if(mouseFollowing){
            Point p = getMousePosition();
            if(p!=null){
                Position where=Position.useMagicGravity(new Position(p.x,p.y,true));
                toBuild.pos=where;
                /*
                if(engine.getPg().isBuildable(toBuild)){
                    gr.setColor(toBuild.getColor());
                }else{
                    gr.setColor(Color.BLACK);
                }*/
                setPreviewColor(toBuild,gr);

                gr.fillRect(where.getX_asPixel(),where.getY_asPixel(), toBuild.size.getX_asPixel(), toBuild.size.getY_asPixel() );

            }
        }
    }

    private void setPreviewColor(Block b, Graphics2D gr){
        if(b instanceof GarbageCan){
            if(!engine.getPg().isBuildable(toBuild)){
                gr.setColor(toBuild.getColor());
            }else{
                gr.setColor(Color.BLACK);
            }
        }else{
            if(engine.getPg().isBuildable(toBuild)){
                gr.setColor(toBuild.getColor());
            }else{
                gr.setColor(Color.BLACK);
            }
        }
    }

}

