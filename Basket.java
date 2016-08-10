import java.awt.*;

public class Basket extends Object
{
    protected int width;
    protected int height;

    public Basket(int x, int y, int width, int height, Color color)
    {
        super(x, y, Color.RED);
        this.width = width;
        this.height = height;
    }

    // Paint itself given the Graphics context
    public void paint(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height); // Fill a rectangle
    }
}
