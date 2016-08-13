import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Shit extends DropObject
{
    protected BufferedImage image;
    
    public Shit(int x, int y)
    {
        super(x, y);
        try {                
            image = ImageIO.read(new File("shit.png"));
        } catch (IOException ex) {
        }
    }
    
    public void paint(Graphics g)
    {
        if(!isExplosed)
            g.drawImage(image, x, y, width, height, null);
        else
            g.drawImage(explosionImage, x, y, width, height, null);
    }
}
