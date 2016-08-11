import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Duck extends Animal
{
    protected BufferedImage image;
    public Duck()
    {
        super();
        try {                
            image = ImageIO.read(new File("duck.png"));
        } catch (IOException ex) {
        }
    }
    
    // Paint itself given the Graphics context
    public void paint(Graphics g) {
        g.drawImage(image, x, y, null);
    }
}
