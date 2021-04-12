package View;

import Model.Blocks.Block;
import Model.Blocks.Road;
import Model.GameEngine;
import Model.People.Visitor;
import Model.Playground;
import Model.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import static View.MainWindow2.*;

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
        for(Block b : gameEngine.getPg().getBuildedObjectList()){
                if(b!=null){
                    b.paint(gr);
                    if(b instanceof Road && ((Road) b).isHasGarbageCan()){
                        gr.setColor(Color.GREEN);
                        gr.fillRect(b.pos.getX_asPixel(),b.pos.getY_asPixel(),b.size.getX_asPixel()/4,b.size.getY_asPixel()/4);

                    }


                    drawBlockLabel(b,gr);
                }
        }
    }

    private static Rectangle getBlockAsRectangle(Block block){
        return new Rectangle(block.pos.getX_asPixel(),block.pos.getY_asPixel(),block.size.getX_asPixel(),block.size.getY_asPixel());
    }

    private static void paintVisitorsSprites(Graphics2D gr,GameEngine gameEngine){
        ArrayList<Visitor> visitors=new ArrayList(gameEngine.getPg().getVisitors());
        for(Visitor v : visitors){
            v.paint(gr);
        }
    }

    private static void paintVisitors(Graphics2D gr,GameEngine gameEngine){
        int[][] visitorCounter=new int[NUM_OF_COLS][NUM_OF_ROWS];
        for(Visitor v : new ArrayList<>(gameEngine.getPg().getVisitors())){
            visitorCounter[v.getPosition().getX_asIndex()][v.getPosition().getY_asIndex()]++;
            gr.setColor(Color.magenta);
            gr.fillOval(v.getPosition().getX_asPixel()+ BOX_SIZE/3,v.getPosition().getY_asPixel()+ BOX_SIZE/3, BOX_SIZE/3, BOX_SIZE/3);
        }

        for(int i=0;i<NUM_OF_COLS;i++){
            for(int j=0;j<NUM_OF_ROWS;j++){
                if(visitorCounter[i][j]<2){continue;}
                Block b=gameEngine.getPg().getBlocks()[i][j];
                gr.setColor(Color.BLACK);
                Rectangle r=new Rectangle(b.pos.getX_asPixel(),b.pos.getY_asPixel(),BOX_SIZE,BOX_SIZE);
                centerString(gr,r,String.valueOf(visitorCounter[i][j]));
            }
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gr = (Graphics2D) g;
        gr.setBackground(new Color(24, 83, 24));
        paintBlocks(gr,engine);
        paintVisitorsSprites(gr,engine);

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

    private static void drawBlockLabel(Block block, Graphics2D gr){
        /*
        if(block instanceof Queueable){
            centerString(gr,getBlockAsRectangle(block),"V:"+((Queueable) block).getQueue().size());
        }*/

    }

    public static void centerString(Graphics g, Rectangle r, String s) {
        FontRenderContext frc =
                new FontRenderContext(null, true, true);

        Font font=g.getFont();
        Rectangle2D r2D = font.getStringBounds(s, frc);
        int rWidth = (int) Math.round(r2D.getWidth());
        int rHeight = (int) Math.round(r2D.getHeight());
        int rX = (int) Math.round(r2D.getX());
        int rY = (int) Math.round(r2D.getY());

        int a = (r.width / 2) - (rWidth / 2) - rX;
        int b = (r.height / 2) - (rHeight / 2) - rY;

        g.setFont(font);
        g.drawString(s, r.x + a, r.y + b);
    }

}

