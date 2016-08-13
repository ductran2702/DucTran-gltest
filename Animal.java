import java.awt.*;
import java.util.Random;

public class Animal
{
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected DropObject object1;
    protected DropObject object2;

    public Animal() {
        this.x = FrameCanvas.CANVAS_WIDTH/2;
        this.y = FrameCanvas.CANVAS_HEIGHT/10;
        this.width = 82;
        this.height = 88;
        Random rand = new Random();
        int d = rand.nextInt(2);
        if(d == 0)
            object1 = new Egg(x, y);
        else
            object1 = new Shit(x, y);
    }
}