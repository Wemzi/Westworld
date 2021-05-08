package View;

import Model.Blocks.Block;
import Model.Blocks.FreePlace;
import Model.GameEngine;
import Model.People.Employee;
import Model.People.Visitor;
import Model.People.VisitorState;
import Model.Position;
import Model.Scaler;
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
    private final Scaler scaler;

    private BufferedImage background;

    private boolean mouseFollowing=false;
    private Block toBuild;

    public GameField(GameEngine engine,Scaler sc) {
        setPreferredSize(new Dimension(600, 600));
        setBorder(BorderFactory.createLineBorder(Color.black));
        this.engine=engine;
        this.scaler=sc;
        makeBackgroundImage();
    }

    /**
     * Kikapcsolja az egér mutatótól függő animációt.
     */
    public void disableMouseFollowing() {
        mouseFollowing=false;
    }

    /**
     * Bekapcsolja az egér mutatótól függő animációt.
     * Az animáció lényege, hogy kirajzolja a megépítendő blockot oda, ahol az egérmutató éppen áll.
     * Amennyiben nem lehet arra a területre ezt a blockot megépíteni, akor az animáció ezt egyértelműen jelzi.
     * @param block - a megépítendő Block
     */
    public void enableMouseFollowing(Block block) {
        this.mouseFollowing =true;
        toBuild =block;
    }

    private static void paintBlocks(Graphics2D gr,GameEngine gameEngine){

        for(Block b : gameEngine.getPg().getBuildedObjectList()){
                if(b!=null){
                    if(b instanceof FreePlace){continue;}
                    b.paint(gr);
                }
        }
    }

    /**
     * Megadja a Blockot befoglaló téglalapot. Kvázi a Block keretét.
     * @param block - aminek a kerete kell
     * @return  A Blockot befoglaló legkisebb téglalap
     */
    public static Rectangle getBlockAsRectangle(Block block){
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
        background = new BufferedImage(scaler.getBoxSize()* GameWindow.NUM_OF_COLS, scaler.getBoxSize()* GameWindow.NUM_OF_ROWS, BufferedImage.TYPE_INT_ARGB);

        Graphics2D bgGraphics = background.createGraphics();
        for (int i = 0; i < GameWindow.NUM_OF_COLS; i++) {
            for (int j = 0; j < GameWindow.NUM_OF_ROWS; j++) {
                bgGraphics.drawImage(sp.nextSprite(), i*scaler.getBoxSize(), j*scaler.getBoxSize(), null);
            }
        }
        bgGraphics.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gr = (Graphics2D) g;
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

    /**Egy téglalap közepére iír egy Stringet
     *
     * @param g - amivel ír
     * @param r - a téglalap, aminek a közepére lesz igazítva a szöveg
     * @param s - a beírandó szöveg
     */
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

