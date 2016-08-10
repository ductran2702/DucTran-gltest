import java.awt.*;

public class Duck extends Animal
{
    public Duck()
    {
        super(Color.GREEN);   
    }
    
    // Paint itself given the Graphics context
    public void paint(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, 40, 40); // Fill a oval
    }
}
