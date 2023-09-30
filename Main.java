import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JPanel implements ActionListener {
    private sq[][] board = { { sq.e, sq.e, sq.e },
            { sq.e, sq.e, sq.e },
            { sq.e, sq.e, sq.e } };

    private sq currentTurn = sq.x;

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

        g2.setStroke(new BasicStroke(15));
        if (currentTurn.equals(sq.x)) {
            g.drawLine(25, 25, 75, 75);
            g.drawLine(75, 25, 25, 75);
        } else {
            g.drawOval(25,25,50,50);
        }

        g.setColor(Color.black);
        g2.setStroke(new BasicStroke(30));
        int radius = 100;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].equals(sq.o)) {
                    g.drawOval(j * 200 + 100 - radius / 2, i * 200 + 200 - radius / 2, radius, radius);
                } else if (board[i][j].equals(sq.x)) {
                    g.drawLine(j * 200 + 100 - radius / 2, i * 200 + 200 - radius / 2, j * 200 + 100 + radius / 2,
                            i * 200 + 200 + radius / 2);
                    g.drawLine(j * 200 + 100 - radius / 2, i * 200 + 200 + radius / 2, j * 200 + 100 + radius / 2,
                            i * 200 + 200 - radius / 2);
                }
            }
        }
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