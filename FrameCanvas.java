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
public class FrameCanvas extends JFrame {
    // Define constants for the various dimensions
    public static final int CANVAS_WIDTH = 800;
    public static final int CANVAS_HEIGHT = 600;
    public static final Color CANVAS_BG_COLOR = Color.CYAN;

    private DrawCanvas canvas; // the custom drawing canvas (an inner class extends JPanel)
    private Basket basket;     // the basket
    private int score;
    private int egg = 3;
    private int shit = 3;
    private int speed = 10;

    private List<Hen> henList;           // Hen list
    private List<Duck> duckList;         // Duck list
    private List<Geese> geeseList;       // Geese list

    private Image basketImage;

    private static Font monoFont = new Font("Monospaced", Font.BOLD | Font.ITALIC, 36);
    private static Font bigFont = new Font("Monospaced", Font.BOLD , 80);
    // Constructor to set up the GUI components and event handlers
    public FrameCanvas() {
        henList = new ArrayList<Hen>();
        duckList = new ArrayList<Duck>();
        geeseList = new ArrayList<Geese>();
        // Construct a sprite given x, y, width, height, color
        //sprite = new Sprite(CANVAS_WIDTH / 2 - 5, CANVAS_HEIGHT / 2 - 40, 10, 80, Color.RED);
        basket = new Basket(CANVAS_WIDTH / 2 - 5, CANVAS_HEIGHT * 5/6 - 80);
        //hen = new Hen(CANVAS_WIDTH/2);

        // Set up the custom drawing canvas (JPanel)
        canvas = new DrawCanvas();
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        // Add both panels to this JFrame
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(canvas, BorderLayout.CENTER);
        //cp.add(btnPanel, BorderLayout.SOUTH);

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
        if(basket.x <= 0)
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
        if(basket.x >= CANVAS_WIDTH - 100)
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

    private void run(Animal animal){
        Thread thread = new Thread(){
                public void run(){
                    int k = 0;
                    int n = 0;
                    while(true){
                        try {
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
                            if(k % 50 == 0 ){
                                Random rand = new Random();
                                int d = rand.nextInt(2);
                                if(d == 0)
                                    animal.object = new Egg(animal.x, animal.y);
                                else
                                    animal.object = new Shit(animal.x, animal.y);
                                k = 0;
                            }

                            if(animal.object != null){
                                animal.object.y = animal.object.y + speed;
                                int objectY = animal.object.y;
                                int objectX = animal.object.x;
                                int objectW = animal.object.width;
                                int objectH = animal.object.height;
                                //System.out.println("objectX=" + objectX + " objectY=" + objectY + " objectW=" + objectW + " objectH=" + objectH);
                                //System.out.println("basket.x=" + basket.x + " basket.y=" + basket.y + "basket.width=" + basket.width);
                                if(!((objectY >= basket.y && objectY <= basket.y + 20 )&& (animal.object.x > basket.x && animal.object.x < basket.x + basket.width))){
                                    //System.out.println("out");
                                    canvas.repaint(animal.object.x, objectY, animal.object.width * 2, animal.object.height);    // Clear old area to background
                                    canvas.repaint(animal.object.x, animal.object.y, animal.object.width, animal.object.height);     // Paint at new location
                                    if(objectY >= CANVAS_HEIGHT * 5/6){
                                        if(animal.object instanceof Egg && egg > 0){
                                            egg--;
                                        }
                                        animal.object = null;
                                    }
                                } else {
                                    //System.out.println("in");
                                    //int objectX = animal.object.x;
                                    //int objectW = animal.object.width;
                                    //int objectH = animal.object.height;
                                    if(animal.object instanceof Egg){
                                        score++;
                                    } else
                                        shit--;
                                    if(score % 20 == 0)
                                        speed += 5;
                                    animal.object = null;
                                    canvas.repaint(objectX, objectY, objectW * 2, objectH);
                                }
                            }
                            // drop object end
                            this.sleep(100);        //100 milliseconds is one second.
                            k++;
                        }catch(InterruptedException ex) {
                            this.currentThread().interrupt();
                        }
                    }
                }
            };
        thread.start();
    }

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
            g.setColor(Color.RED);
            canvas.repaint(500, 0, 60, 60);
            g.drawString("Egg:" + egg, 500, 40);
            canvas.repaint(300, 0, 60, 60);
            g.setColor(Color.BLACK);
            g.drawString("Shit:" + shit, 300, 40);
            if(egg <= 0 || shit <= 0){
                g.setFont(bigFont);
                canvas.repaint();
                g.setColor(Color.RED);
                g.drawString("GAME OVER !!!", CANVAS_HEIGHT/5, CANVAS_WIDTH/3);
            }
            basket.paint(g);
            //g.drawImage(basketImage, 0, 0, getWidth(), getHeight(), null);
            for(int i = 0; i < henList.size(); i++){
                henList.get(i).paint(g);
                if(henList.get(i).object != null)
                    henList.get(i).object.paint(g);
            }
            for(int i = 0; i < duckList.size(); i++){
                duckList.get(i).paint(g);
                if(duckList.get(i).object != null)
                    duckList.get(i).object.paint(g);
            }
            for(int i = 0; i < geeseList.size(); i++){
                geeseList.get(i).paint(g);
                if(geeseList.get(i).object != null)
                    geeseList.get(i).object.paint(g);
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