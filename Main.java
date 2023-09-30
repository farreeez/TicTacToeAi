import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JPanel implements ActionListener {
    private sq[][] board = { { sq.e, sq.e, sq.e },
            { sq.e, sq.e, sq.e },
            { sq.e, sq.e, sq.e } };

    public Main() {
        setFocusable(true);
        setPreferredSize(new Dimension(600, 700));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Big Tac");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Main game = new Main();
        frame.add(game);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(30));
        g.drawLine(200, 100, 200, 700);
        g.drawLine(400, 100, 400, 700);
        g.drawLine(0, 300, 600, 300);
        g.drawLine(0, 500, 600, 500);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    private enum sq {
        e,
        x,
        o
    }
}