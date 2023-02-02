import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static java.awt.Color.*;

class Window extends JPanel implements ActionListener{

    static double c1 = 0;
    static double c2 = 0;
    static int iteration = 100;
    static boolean mand = true;
    static int colormode = 0;

    static double randChoice = Math.random() * 100;

    Timer t = new Timer(1, this);

    static boolean recalc = true;

    static final int boardSizeX = 900;
    static final int boardSizeY = 900;
    static int[][][] buffer = new int[boardSizeX][boardSizeY][3];

    static int x, y, size;
    static boolean held = false;

    static double draggedX, draggedY;
    static double initialX;
    static double initialY;

    static double x1 = -2;
    static double x2 = 2;
    static double y1 = -2;
    static double y2 = 2;

    Window(){
        this.setPreferredSize(new Dimension(boardSizeX, boardSizeY));
        this.setBackground(new Color(0, 0, 0, 255));
        this.setFocusable(true);
        this.addKeyListener(new KeyListener());
        this.addMouseMotionListener(new MouseTracker());
        this.addMouseListener(new ClickListener());
        t.start();
    }

    public void paint(Graphics g) {
        super.paint(g);
        draw(g);
    }

    void draw(Graphics g){
        if(recalc){
            for(int i = 0; i < boardSizeX; i++){
                for(int j = 0; j < boardSizeY; j++){
                    double[] m = convertPoint(i, j);
                    int bounds = bounded(m[0], m[1]);
                    Color c = getColor(bounds);
                    buffer[i][j] = new int[]{c.getRed(), c.getGreen(), c.getBlue()};
                }
            }
            recalc = false;
        }

        drawBuffer(g);

        if(held){
            if(draggedX > initialX) {
                x = (int) initialX;
                y = (int) initialY;
                size = (int) (draggedX - initialX);
            }

            g.setColor(new Color(255, 255, 255, 47));
            g.fillRect(x, y, size, size);
            g.setColor(white);
            g.drawRect(x, y, size, size);
        }

        g.setColor(red);
        g.drawRect(boardSizeX / 2, boardSizeY / 2, 1, 1);

        g.setColor(black);

        g.setFont(new Font("arial", Font.PLAIN, 20));

        g.drawString("X: " + ((4 - ((x2 - x1)) / 2) + x1) + ", Y: " + ((4 - ((y2 - y1)) / 2) + y1), 10, 25);
        g.drawString("Iterations: " + iteration, 10, 45);
    }

    void drawBuffer(Graphics g){
        for(int i = 0; i < buffer.length; i++){
            for(int j = 0; j < buffer[0].length; j++){
                g.setColor(new Color(buffer[i][j][0], buffer[i][j][1], buffer[i][j][2]));
                g.fillRect(i, j, 1, 1);
            }
        }
    }

    static double[] convertPoint(double x, double y){
        return new double[] {x1 + ((x2 - x1) * x) / boardSizeX, y1 + ((y2 - y1) * y) / boardSizeY};
    }

    static double[] squareImaginary(double a, double b){
        double num1 = (a * a) - (b * b);
        double num2 = 2 * a * b;
        return new double[] {num1, num2};
    }

    static int bounded (double a, double b){
        if(Math.abs(a) + Math.abs(b) > 4) return 1;
        double a1 = a, b1 = b;
        for(int i = 0; i < iteration; i++){
            double[] sq = squareImaginary(a1, b1);
            if(mand){
                a1 = sq[0] + a;
                b1 = sq[1] + b;
            } else {
                a1 = sq[0] + c1;
                b1 = sq[1] + c2;
            }
            if(Math.abs(a1) + Math.abs(b1) > 4){
                return i;
            }
        }

        return -1;
    }

    static Color getColor(int iterations){
        switch(colormode){
            case 0:
                float a = (iterations == -1) ? (float) randChoice : (float) (iterations / randChoice + randChoice);
                return getHSBColor((a == -1) ? (float) randChoice : a, iterations, a);
            case 1:
                int i = (iterations == -1) ? 0 : Math.min(iterations, 255);
                return new Color(i, i, i);
            case 2:
                return Color.getHSBColor((float) iterations / 50, 1, iterations == -1 ? 0 : 1);
        }
        return new Color(0, 0, 0);
    }

    static void startSelect(int x, int y){
        Window.x = x;
        Window.y = y;
        held = true;
    }

    static void end(){
        Window.x = 0;
        Window.y = 0;
        Window.size = 0;
        held = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //repaint();
    }

    public class MouseTracker implements MouseMotionListener{

        @Override
        public void mouseDragged(MouseEvent e) {
            draggedX = e.getX();
            draggedY = e.getY();

            Window.this.repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            double[] cs = convertPoint(e.getX(), e.getY());
            c1 = cs[0];
            c2 = cs[1];

            if(!mand){
                recalc = true;
                Window.this.repaint();
            }
        }
    }

    public class KeyListener implements java.awt.event.KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            double a = (Window.x2 - Window.x1) * .025;
            switch(e.getKeyCode()) {
                case KeyEvent.VK_R:
                    x1 = -2;
                    x2 = 2;
                    y1 = -2;
                    y2 = 2;
                    iteration = 100;
                    break;
                case KeyEvent.VK_X:
                    randChoice = Math.random() * 100;
                    break;
                case KeyEvent.VK_DOWN:
                    Window.y1 += a;
                    Window.y2 += a;
                    break;
                case KeyEvent.VK_UP:
                    Window.y1 -= a;
                    Window.y2 -= a;
                    break;
                case KeyEvent.VK_LEFT:
                    Window.x1 -= a;
                    Window.x2 -= a;
                    break;
                case KeyEvent.VK_RIGHT:
                    Window.x1 += a;
                    Window.x2 += a;
                    break;
                case KeyEvent.VK_0:
                    Window.x1 += a / 2;
                    Window.x2 -= a / 2;
                    Window.y1 += a / 2;
                    Window.y2 -= a / 2;
                    break;
                case KeyEvent.VK_9:
                    Window.x1 -= a / 2;
                    Window.x2 += a / 2;
                    Window.y1 -= a / 2;
                    Window.y2 += a / 2;
                    break;
                case KeyEvent.VK_S:
                    mand = !mand;
                    break;
                case KeyEvent.VK_A:
                    iteration -= 10;
                    break;
                case KeyEvent.VK_D:
                    iteration += 10;
                    break;
                case KeyEvent.VK_C:
                    colormode = (colormode != 2)? colormode + 1 : 0;
                    break;
            }

            Window.this.repaint();

            recalc = true;
        }

        @Override
        public void keyReleased(KeyEvent e) {}
    }
    public class ClickListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            initialX = e.getX();
            initialY = e.getY();

            if(mand){
                startSelect(e.getX(), e.getY());
            }

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(mand){
                end();

                double a = (Window.x2 - Window.x1);
                x1 += (initialX / boardSizeX) * a;
                x2 -= a * (boardSizeX - draggedX)/boardSizeX;
                y1 += (initialY / boardSizeY) * a;
                y2 -= a * (boardSizeY - (initialY + (draggedX - initialX)))/boardSizeY;

                recalc = true;
                Window.this.repaint();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}
