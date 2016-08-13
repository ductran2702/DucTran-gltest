import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.*;       // Using AWT's Graphics and Color
import java.awt.event.*; // Using AWT's event classes and listener interfaces
import javax.swing.*;    // Using Swing's components and containers
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Custom Graphics Example: Using key/button to move a object left or right.
 * The moving object (sprite) is defined in its own class, with its own
 * operations and can paint itself.
 */
public class FrameCanvas extends JFrame implements MouseMotionListener {
    // Define constants for the various dimensions
    public static final int CANVAS_WIDTH = 800;
    public static final int CANVAS_HEIGHT = 600;
    public static final Color CANVAS_BG_COLOR = Color.CYAN;

    private DrawCanvas canvas; // the custom drawing canvas (an inner class extends JPanel)
    private Basket basket;     // the basket
    private int score;
    private int egg = 3;
    private int shit = 3;

    private List<Hen> henList;           // Hen list
    private List<Duck> duckList;         // Duck list
    private List<Geese> geeseList;       // Geese list

    private Image basketImage;
    private boolean isGameOver;
    private boolean isSlowDown;
    private int slowTime = 20;
    private int exploseTime = 20;

    private static Font monoFont = new Font("Monospaced", Font.BOLD | Font.ITALIC, 36);
    private static Font bigFont = new Font("Monospaced", Font.BOLD , 80);
    // Constructor to set up the GUI components and event handlers
    public FrameCanvas() {
        henList = new ArrayList<Hen>();
        duckList = new ArrayList<Duck>();
        geeseList = new ArrayList<Geese>();
        // Construct a sprite given x, y, width, height, color
        basket = new Basket(CANVAS_WIDTH / 2 - 5, CANVAS_HEIGHT * 5/6 - 80);

        this.addMouseMotionListener(this);
        // Set up the custom drawing canvas (JPanel)
        canvas = new DrawCanvas();
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        // Add both panels to this JFrame
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(canvas, BorderLayout.CENTER);

        // "super" JFrame fires KeyEvent
        addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent evt) {
                    switch(evt.getKeyCode()) {
                        case KeyEvent.VK_LEFT:  moveLeft();  break;
                        case KeyEvent.VK_RIGHT: moveRight(); break;
                        case KeyEvent.VK_A: addHen(); break;
                        case KeyEvent.VK_S: addDuck(); break;
                        case KeyEvent.VK_D: addGeese(); break;
                        case KeyEvent.VK_F: slowDown(); break;
                        case KeyEvent.VK_SPACE: explose(); break;
                        //case KeyEvent.VK_UP:    moveUp();  break;
                        //case KeyEvent.VK_DOWN:  moveDown(); break;
                    }
                }
            });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Catch the egg.");
        pack();            // pack all the components in the JFrame
        setVisible(true);  // show it
        requestFocus();    // "super" JFrame requests focus to receive KeyEvent
    }

    // Helper method to move the sprite left
    private void moveLeft() {
        if(basket.x <= 0 || isGameOver)
            return;
        // Save the current dimensions for repaint to erase the basket
        int savedX = basket.x;
        // update basket
        basket.x -= 10;
        // Repaint only the affected areas, not the entire JFrame, for efficiency
        canvas.repaint(savedX, basket.y, basket.image.getWidth(), basket.image.getHeight()); // Clear old area to background
        canvas.repaint(basket.x, basket.y, basket.image.getWidth(), basket.image.getHeight()); // Paint new location
    }

    // Helper method to move the sprite right
    private void moveRight() {
        if(basket.x >= CANVAS_WIDTH - 100 || isGameOver)
            return;
        // Save the current dimensions for repaint to erase the sprite
        int savedX = basket.x;
        // update sprite
        basket.x += 10;
        // Repaint only the affected areas, not the entire JFrame, for efficiency
        canvas.repaint(savedX, basket.y, basket.image.getWidth(), basket.image.getHeight()); // Clear old area to background
        canvas.repaint(basket.x, basket.y, basket.image.getWidth(), basket.image.getHeight()); // Paint new location
    }

    private void addHen() {
        Animal hen = new Hen();
        henList.add((Hen)hen);
        run(hen);
    }

    private void addDuck() {
        Duck duck = new Duck();
        duckList.add(duck);
        run(duck);
    }

    private void addGeese() {
        Geese geese = new Geese();
        geeseList.add(geese);
        run(geese);
    }

    private void explose(){
        for(int i = 0; i < henList.size(); i++){
            if(henList.get(i).object1 != null)
                henList.get(i).object1 = null;
            if(henList.get(i).object2 != null)
                henList.get(i).object2 = null;
        }
        for(int i = 0; i < duckList.size(); i++){
            if(duckList.get(i).object1 != null)
                duckList.get(i).object1 = null;
            if(duckList.get(i).object2 != null)
                duckList.get(i).object2 = null;
        }
        for(int i = 0; i < geeseList.size(); i++){
            if(geeseList.get(i).object1 != null)
                geeseList.get(i).object1 = null;
            if(geeseList.get(i).object2 != null)
                geeseList.get(i).object2 = null;
        }
    }

    private void slowDown(){
        if(isSlowDown)
            return;
        Thread thread = new Thread(){
                public void run(){
                    isSlowDown = true;
                    int k = 0;
                    while(true){
                        if(isSlowDown){
                            slowTime--;
                            if(slowTime > 10)
                                setObjectSpeed(true);
                            else if(slowTime == 10)
                                setObjectSpeed(false);
                            else if(slowTime == 0) {
                                isSlowDown = false;
                                slowTime = 20;
                                this.currentThread().interrupt();
                            }
                            try {
                                this.sleep(1000);
                            }catch(InterruptedException ex) {
                                this.currentThread().interrupt();
                            }
                            k++;
                        }
                    }
                }
            };
        thread.start();
    }

    private void setObjectSpeed(boolean isSlow){
        int _speed;
        if(isSlow)
            _speed = 5;
        else
            _speed = (score / 20) * 5 + 10;
        System.out.println("_speed=" + _speed);
        for(int i = 0; i < henList.size(); i++){
            if(henList.get(i).object1 != null)
                henList.get(i).object1.speed = _speed;
            if(henList.get(i).object2 != null)
                henList.get(i).object2.speed = _speed;
        }
        for(int i = 0; i < duckList.size(); i++){
            if(duckList.get(i).object1 != null)
                duckList.get(i).object1.speed = _speed;
            if(duckList.get(i).object2 != null)
                duckList.get(i).object2.speed = _speed;

        }
        for(int i = 0; i < geeseList.size(); i++){
            if(geeseList.get(i).object1 != null)
                geeseList.get(i).object1.speed = _speed;
            if(geeseList.get(i).object2 != null)
                geeseList.get(i).object2.speed = _speed;
        }
    }

    private void run(Animal animal){
        Thread thread = new Thread(){
                public void run(){
                    int k = 0;
                    int n = 0;
                    while(true){
                        // run animal
                        if(k % 10 == 0 ) {
                            Random rand = new Random();
                            n = rand.nextInt(2);
                        }
                        if(animal.x <= 0)
                            n = 0;
                        else if (animal.x >= FrameCanvas.CANVAS_WIDTH - 40)
                            n = 1;
                        if(n == 0)
                            animal.x = animal.x + 10;
                        else
                            animal.x = animal.x - 10;
                        int savedX = animal.x;
                        canvas.repaint(savedX, animal.y, animal.width * 2, animal.height);    // Clear old area to background
                        canvas.repaint(animal.x, animal.y, animal.width, animal.height);     // Paint at new location
                        // run animal end

                        // drop object
                        //System.out.println("run() object1=" + animal.object1 + " object2=" + animal.object2 + " k=" + k);
                        if(k % 50 == 0 && k != 0){
                            Random rand = new Random();
                            int d = rand.nextInt(2);
                            if(d == 0) {
                                if(animal.object1 != null)
                                    animal.object2 = new Egg(animal.x, animal.y);
                                else
                                    animal.object1 = new Egg(animal.x, animal.y);
                                //System.out.println("object1=" + animal.object1 + " object2=" + animal.object2);
                            } else {
                                if(animal.object1 != null)
                                    animal.object2 = new Shit(animal.x, animal.y);
                                else
                                    animal.object1 = new Shit(animal.x, animal.y);
                                //System.out.println("object1=" + animal.object1 + " object2=" + animal.object2);
                            }
                            k = 0;
                        }
                        handleObjects(animal);
                        // drop object end
                        try {
                            this.sleep(100);        //1000 milliseconds is one second.
                            k++;
                        }catch(InterruptedException ex) {
                            this.currentThread().interrupt();
                        }
                    }
                }
            };
        thread.start();
    }

    private void handleObjects(Animal animal){
        if(animal.object1 != null){
            animal.object1.y = animal.object1.y + animal.object1.speed;
            int objectY = animal.object1.y;
            //System.out.println("x=" + _object.x + "\ty=" + animal.object.y + " width=" + animal.object.width + " height=" + animal.object.height);
            if(!((objectY >= basket.y && objectY <= basket.y + 20 )&& (animal.object1.x > basket.x && animal.object1.x < basket.x + basket.width))){
                //System.out.println("out");
                canvas.repaint(animal.object1.x, objectY, animal.object1.width * 2, animal.object1.height);   // Clear old area to background
                canvas.repaint(animal.object1.x, animal.object1.y, animal.object1.width, animal.object1.height);     // Paint at new location
                if(objectY >= CANVAS_HEIGHT * 5/6){
                    //System.out.println("end");
                    if(animal.object1 instanceof Egg && egg > 0){
                        egg--;
                    }
                    animal.object1 = null;
                }
            } else {
                //System.out.println("in");
                int objectX = animal.object1.x;
                int objectW = animal.object1.width;
                int objectH = animal.object1.height;
                if(animal.object1 instanceof Egg){
                    score++;
                } else if(shit > 0)
                    shit--;
                if(score % 20 == 0){
                    animal.object1.speed += 5;
                }
                animal.object1 = null;
                canvas.repaint(objectX, objectY, objectW * 2, objectH);
            }
        }
        if(animal.object2 != null){
            animal.object2.y = animal.object2.y + animal.object2.speed;
            int objectY = animal.object2.y;
            //System.out.println("x=" + _object.x + "\ty=" + animal.object.y + " width=" + animal.object.width + " height=" + animal.object.height);
            if(!((objectY >= basket.y && objectY <= basket.y + 20 )&& (animal.object2.x > basket.x && animal.object2.x < basket.x + basket.width))){
                //System.out.println("out");
                canvas.repaint(animal.object2.x, objectY, animal.object2.width * 2, animal.object2.height);   // Clear old area to background
                canvas.repaint(animal.object2.x, animal.object2.y, animal.object2.width, animal.object2.height);     // Paint at new location
                if(objectY >= CANVAS_HEIGHT * 5/6){
                    //System.out.println("end");
                    if(animal.object2 instanceof Egg && egg > 0){
                        egg--;
                    }
                    animal.object2 = null;
                }
            } else {
                //System.out.println("in");
                int objectX = animal.object2.x;
                int objectW = animal.object2.width;
                int objectH = animal.object2.height;
                if(animal.object2 instanceof Egg){
                    score++;
                } else if(shit > 0)
                    shit--;
                if(score % 20 == 0) {
                    animal.object2.speed += 5;
                }
                animal.object2 = null;
                canvas.repaint(objectX, objectY, objectW * 2, objectH);
            }
        }
    }

    public void mouseMoved(MouseEvent e) {
        if(isGameOver)
            return;
        int x = e.getPoint().x;
        int y = e.getPoint().y;

        int savedX = basket.x;
        basket.x = x - basket.image.getWidth()/2;
        canvas.repaint(savedX, basket.y, basket.image.getWidth(), basket.image.getHeight()); // Clear old area to background
        canvas.repaint(basket.x, basket.y, basket.image.getWidth(), basket.image.getHeight()); // Paint new location
    }

    public void mouseDragged(java.awt.event.MouseEvent e) {}

    // Define inner class DrawCanvas, which is a JPanel used for custom drawing
    class DrawCanvas extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(CANVAS_BG_COLOR);
            g.drawLine(0, CANVAS_HEIGHT/4, CANVAS_WIDTH, CANVAS_HEIGHT/4);
            g.drawLine(0, CANVAS_HEIGHT * 5/6, CANVAS_WIDTH, CANVAS_HEIGHT * 5/6);
            g.setColor(Color.YELLOW);
            g.setFont(monoFont);
            canvas.repaint(0, 0, 60, 60);
            g.drawString("Score:" + score, 30, 40);
            g.setColor(Color.YELLOW);
            canvas.repaint(500, 0, 60, 60);
            g.drawString("Egg:" + egg, 500, 40);
            canvas.repaint(300, 0, 60, 60);
            g.setColor(Color.BLACK);
            g.drawString("Shit:" + shit, 300, 40);
            if(egg <= 0 || shit <= 0){
                isGameOver = true;
                g.setFont(bigFont);
                canvas.repaint();
                g.setColor(Color.RED);
                g.drawString("GAME OVER !!!", CANVAS_HEIGHT/5, CANVAS_WIDTH/3);
            }
            g.setColor(Color.GREEN);
            g.setFont(monoFont);
            canvas.repaint(30, 550, 60, 60);
            g.drawString("Slowable time:" + slowTime, 30, 550);
            g.setColor(Color.RED);
            canvas.repaint(450, 550, 60, 60);
            g.drawString("Explose time:" + exploseTime, 450, 550);
            basket.paint(g);
            for(int i = 0; i < henList.size(); i++){
                henList.get(i).paint(g);
                if(henList.get(i).object1 != null)
                    henList.get(i).object1.paint(g);
                if(henList.get(i).object2 != null)
                    henList.get(i).object2.paint(g);
            }
            for(int i = 0; i < duckList.size(); i++){
                duckList.get(i).paint(g);
                if(duckList.get(i).object1 != null)
                    duckList.get(i).object1.paint(g);
                if(duckList.get(i).object2 != null)
                    duckList.get(i).object2.paint(g);
            }
            for(int i = 0; i < geeseList.size(); i++){
                geeseList.get(i).paint(g);
                if(geeseList.get(i).object1 != null)
                    geeseList.get(i).object1.paint(g);
                if(geeseList.get(i).object2 != null)
                    geeseList.get(i).object2.paint(g);
            }
        }
    }

    // The entry main() method
    public static void main(String[] args) {
        // Run GUI construction on the Event-Dispatching Thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new FrameCanvas(); // Let the constructor do the job
                }
            });
    }
}