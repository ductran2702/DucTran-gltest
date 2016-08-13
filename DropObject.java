import java.awt.*;

public abstract class DropObject extends Object
{
    protected int width;
    protected int height;
    protected int speed;
    public DropObject(int x, int y)
    {
        super(x, y);
        width = 30;
        height = 30;
        speed = 10;
    }

    abstract public void paint(Graphics g);
}
