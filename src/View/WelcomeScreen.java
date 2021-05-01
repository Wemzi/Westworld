package View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WelcomeScreen extends JFrame {
    private BufferedImage background;

    public WelcomeScreen(String title) {
        super(title);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            background= ImageIO.read(new File("graphics/background.png"));
        } catch (IOException e) {
            System.err.println("background.png not found!");
            BufferedImage img = new BufferedImage(getToolkit().getScreenSize().width,getToolkit().getScreenSize().height, ColorSpace.TYPE_RGB);
            Graphics2D graphics = img.createGraphics();

            // Fill the background with color
            graphics.setColor (Color.pink);
            graphics.fillRect ( 0, 0, getToolkit().getScreenSize().width,getToolkit().getScreenSize().height);
            background=img;
        }

        setVisible(true);
    }

    @Override
    public void paintComponents(Graphics g) {
        g.drawImage(background,0,0,null);
        super.paintComponents(g);
    }
}
