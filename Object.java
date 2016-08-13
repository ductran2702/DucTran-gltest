import java.awt.*;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Object
{
    protected int x;
    protected int y;
    protected boolean isExplosed;
    protected BufferedImage explosionImage;
    
    /**
     * Constructor for objects of class Objects
     */
    public Object(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.isExplosed = false;
        try {
            explosionImage = ImageIO.read(new File("explosion.png"));
        } catch (IOException ex) {
        }
    }
    
    public void setExplosed(boolean b){
        isExplosed = b;
    }
}
