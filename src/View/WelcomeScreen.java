package View;

import View.spriteManagers.SpriteManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A játék kezdőképernyője.
 */
public class WelcomeScreen extends JFrame {
    private BufferedImage background;
    private BufferedImage exitButtonImg;
    private BufferedImage newGameImg;
    private final Rectangle exitButtonArea;
    private final Rectangle newGameButtonArea;

    public WelcomeScreen(String title) {
        super(title);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        int width=getToolkit().getScreenSize().width;
        int height=getToolkit().getScreenSize().height;

        try {
            background= SpriteManager.resize(ImageIO.read(new File("graphics/background.png")),width,height);
            exitButtonImg= ImageIO.read(new File("graphics/exit.png"));
            exitButtonImg=SpriteManager.resize(exitButtonImg,exitButtonImg.getWidth()/2, exitButtonImg.getHeight()/2);
            newGameImg= ImageIO.read(new File("graphics/newgame.png"));
            newGameImg=SpriteManager.resize(newGameImg,newGameImg.getWidth()/2, newGameImg.getHeight()/2);
        } catch (IOException e) {
            System.err.println("background.png not found!");
            BufferedImage img = new BufferedImage(width, height, ColorSpace.TYPE_RGB);
            Graphics2D graphics = img.createGraphics();

            // Fill the background with color
            graphics.setColor(Color.pink);
            graphics.fillRect(0, 0, width, height);
            background = img;
        }
        exitButtonArea=new Rectangle(3*width/5,3*height/5,exitButtonImg.getWidth(),exitButtonImg.getHeight());
        newGameButtonArea=new Rectangle(width/5,3*height/5,newGameImg.getWidth(),newGameImg.getHeight());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(exitButtonArea.contains(e.getPoint())){
                    System.err.println("User clicked on the exit button :(");
                    exitGame();
                }else if(newGameButtonArea.contains(e.getPoint())){
                    System.err.println("User clicked on the new game button :)");
                    GameWindow w=new GameWindow();
                    w.setVisible(true);
                    w.requestFocus(FocusEvent.Cause.ACTIVATION);
                    dispose();
                }
            }
        });

        setPreferredSize(new Dimension(width,height));
        setUndecorated(true);
        pack();
    }

    private void exitGame(){
        dispose();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(background,0,0,null);
        g.drawImage(exitButtonImg,exitButtonArea.x,exitButtonArea.y,null);
        g.drawImage(newGameImg,newGameButtonArea.x,newGameButtonArea.y,null);
    }
}
