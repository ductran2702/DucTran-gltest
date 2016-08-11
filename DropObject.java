import java.awt.*;

public abstract class DropObject extends Object
{
    protected int width;
    protected int height;
    public DropObject(int x, int y)
    {
        super(x, y);
        width = 30;
        height = 30;
    }

    abstract public void paint(Graphics g);
}
