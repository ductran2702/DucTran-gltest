import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Basket extends Object
{
    protected int width;
    protected int height;
    protected BufferedImage image;

    public Basket(int x, int y)
    {
        super(x, y);
        this.width = 100;
        this.height = 60;
        try {                
            image = ImageIO.read(new File("basket.png"));
        } catch (IOException ex) {
        }
    }

    // Paint itself given the Graphics context
    public void paint(Graphics g) {
        g.drawImage(image, x, y, null);
    }
}
