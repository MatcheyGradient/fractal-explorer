import javax.swing.*;

public class Main extends JFrame {

    Main(){
        this.add(new Window());
        this.setTitle("mandelbrot");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String[] args){
        new Main();
    }
}
