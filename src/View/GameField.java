package View;

import Model.Blocks.Block;
import Model.Blocks.FreePlace;
import Model.Blocks.Queueable;
import Model.GameEngine;
import Model.People.Employee;
import Model.People.Visitor;
import Model.People.VisitorState;
import Model.Position;
import View.spriteManagers.SpriteManager;
import View.spriteManagers.StaticSpriteManager;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author Gabor
 */
public class GameField extends JPanel {
    private final GameEngine engine;

    private BufferedImage background;

    private boolean mouseFollowing=false;
    private Block toBuild;

    public GameField(GameEngine engine) {
        setPreferredSize(new Dimension(600, 600));
        setBorder(BorderFactory.createLineBorder(Color.black));
        this.engine=engine;
        makeBackgroundImage();
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
                    if(b instanceof FreePlace){continue;}
                    b.paint(gr);
                    /*
                    if(b instanceof Road && ((Road) b).isHasGarbageCan()){
                        gr.setColor(Color.GREEN);
                        gr.fillRect(b.pos.getX_asPixel(),b.pos.getY_asPixel(),b.size.getX_asPixel()/4,b.size.getY_asPixel()/4);

                    }*/
                    drawBlockLabel(b,gr);
                }
        }
    }

    private static Rectangle getBlockAsRectangle(Block block){
        return new Rectangle(block.pos.getX_asPixel(),block.pos.getY_asPixel(),block.size.getX_asPixel(),block.size.getY_asPixel());
    }

    private static void paintVisitors(Graphics2D gr, GameEngine gameEngine){
        ArrayList<Visitor> visitors=new ArrayList<>(gameEngine.getPg().getVisitors());
        for(Visitor v : visitors){
            if (v.getState() != VisitorState.DOING_SOMETHING) {
                v.paint(gr);
            }
        }
    }

    private static void paintEmployees(Graphics2D gr, GameEngine gameEngine){
        ArrayList<Employee> visitors=new ArrayList<>(gameEngine.getPg().getEmployees());
        for(Employee v : visitors){
            v.paint(gr);
        }
    }

    private void drawBackground(Graphics2D gr){
        gr.drawImage(background,0,0,null);
    }

    private void makeBackgroundImage(){
        SpriteManager sp=new StaticSpriteManager("graphics/grass.png",new Position(1,1,false));
        background = new BufferedImage(MainWindow2.BOX_SIZE*MainWindow2.NUM_OF_COLS, MainWindow2.BOX_SIZE*MainWindow2.NUM_OF_ROWS, BufferedImage.TYPE_INT_ARGB);

        Graphics2D bgGraphics = background.createGraphics();
        for (int i = 0; i < MainWindow2.NUM_OF_COLS; i++) {
            for (int j = 0; j < MainWindow2.NUM_OF_ROWS; j++) {
                bgGraphics.drawImage(sp.nextSprite(), i*MainWindow2.BOX_SIZE, j*MainWindow2.BOX_SIZE, null);
            }
        }
        bgGraphics.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gr = (Graphics2D) g;
        //gr.setBackground(new Color(24, 83, 24));
        drawBackground(gr);
        paintBlocks(gr,engine);
        paintVisitors(gr,engine);
        paintEmployees(gr,engine);

        if(mouseFollowing){
            Point p = getMousePosition();
            if(p!=null){
                Position where=Position.useMagicGravity(new Position(p.x,p.y,true));
                toBuild.pos=where;

                if(engine.getPg().isBuildable(toBuild)){
                    toBuild.paint(gr);
                }else{
                    gr.setColor(Color.BLACK);
                    gr.fillRect(where.getX_asPixel(),where.getY_asPixel(), toBuild.size.getX_asPixel(), toBuild.size.getY_asPixel() );
                }

            }
        }
    }


    private static void drawBlockLabel(Block block, Graphics2D gr){

        if(block instanceof Queueable){
            centerString(gr,getBlockAsRectangle(block),"Q:"+((Queueable) block).getQueue().size());
        }

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

