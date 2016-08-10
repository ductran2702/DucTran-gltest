import java.awt.*;

public class Geese extends Animal
{
    public Geese()
    {
        super(Color.RED);
    }
    
    // Paint itself given the Graphics context
    public void paint(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, 40, 40); // Fill a oval
    }
}
