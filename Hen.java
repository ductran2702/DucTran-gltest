import java.util.Random;
import java.awt.*;       // Using AWT's Graphics and Color
import java.awt.event.*; // Using AWT's event classes and listener interfaces
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;


public class Hen extends Animal
{
    protected BufferedImage image;
    public Hen()
    {
        super();
        try {                
            image = ImageIO.read(new File("hen.png"));
        } catch (IOException ex) {
        }
    }

    // Paint itself given the Graphics context
    public void paint(Graphics g) {
        g.drawImage(image, x, y, null);
    }
}
