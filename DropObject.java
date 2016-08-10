import java.awt.*;

public class DropObject extends Object
{
    protected int width;
    protected int height;
    public DropObject(int x, int y, Color color)
    {
        super(x, y, color);
        width = 20;
        height = 20;
    }

    public void paint(Graphics g)
    {
        g.setColor(color);
        g.fillOval(x, y, width, height); // Fill a oval
    }
}
