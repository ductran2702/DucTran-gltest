import java.awt.*;
import java.util.Random;

public class Animal
{
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected DropObject object;
    protected Color color;

    public Animal(Color color) {
        this.x = FrameCanvas.CANVAS_WIDTH/2;
        this.y = FrameCanvas.CANVAS_HEIGHT/6;
        this.width = 40;
        this.height = 40;
        this.color = color;
        Random rand = new Random();
        int d = rand.nextInt(2);
        if(d == 0)
            object = new Egg(x, y);
        else
            object = new Shit(x, y);
    }
}