import java.util.Random;
import java.awt.*;       // Using AWT's Graphics and Color
import java.awt.event.*; // Using AWT's event classes and listener interfaces
import javax.swing.*;

public class Hen extends Animal
{
    public Hen()
    {
        super(Color.YELLOW);
    }

    // Paint itself given the Graphics context
    public void paint(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, 40, 40); // Fill a oval
    }
}
