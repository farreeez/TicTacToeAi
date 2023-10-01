import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main extends JPanel implements ActionListener {
    private static sq[][] board = { { sq.e, sq.e, sq.e },
            { sq.e, sq.e, sq.e },
            { sq.e, sq.e, sq.e } };

    private static sq currentTurn = sq.x;
    static int fullSqrCount = 0;
    private static JFrame frame = new JFrame("Big Tac");
    private static JButton button = new JButton("Restart", null);

    private boolean playing = true;

    public Main() {
        setFocusable(true);
        setPreferredSize(new Dimension(600, 700));
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                gameCode(e.getX(), e.getY());
            }
        });
        Bot bot = new Bot(2);
        button.setBounds(475, 10, 100, 50);
        button.addActionListener(this);
    }

    public static void main(String[] args) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Main game = new Main();
        game.add(button);
        frame.add(game);
        frame.pack();
        frame.setVisible(true);
    }

    private void gameCode(int x, int y) {
        if (playing) {
            if (x >= 0 && x <= 600 && y > 120 && y <= 730) {
                y -= 120;

                if (y % 200 < 185 && x % 200 < 185 && y % 200 > 25 && x % 200 > 25) {
                    int row = y / 200;
                    int col = x / 200;
                    if (board[row][col].equals(sq.e)) {
                        board[row][col] = currentTurn;
                        if (currentTurn.equals(sq.x)) {
                            currentTurn = sq.o;
                        } else {
                            currentTurn = sq.x;
                        }
                        fullSqrCount++;
                        repaint();
                    }
                }
            }
        }
    }

    private boolean won(sq sqr) {
        for (int i = 0; i < 3; i++) {
            boolean won = true;
            for (int j = 0; j < 3; j++) {
                if (!board[i][j].equals(sqr)) {
                    won = false;
                    break;
                }
            }
            if (won) {
                return won;
            }

            won = true;
            for (int j = 0; j < 3; j++) {
                if (!board[j][i].equals(sqr)) {
                    won = false;
                    break;
                }
            }
            if (won) {
                return won;
            }
        }

        if (board[0][0].equals(sqr) && board[2][2].equals(sqr) && board[1][1].equals(sqr)) {
            return true;
        } else if (board[0][2].equals(sqr) && board[2][0].equals(sqr) && board[1][1].equals(sqr)) {
            return true;
        }

        return false;
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
            g.drawOval(25, 25, 50, 50);
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

        g.setFont(new Font("Comic Sans", Font.BOLD, 100));
        g.setColor(Color.BLUE);
        FontMetrics fm = g.getFontMetrics();
        int y = (700 - fm.getHeight()) / 2 + fm.getAscent();
        int x = 0;
        String str;

        if (won(sq.o)) {
            str = "O Won!";
            x = (600 - fm.stringWidth(str)) / 2;
            g.drawString(str, x, y);
        } else if (won(sq.x)) {
            str = "X Won!";
            x = (600 - fm.stringWidth(str)) / 2;
            g.drawString(str, x, y);
        } else if (fullSqrCount == 9) {
            str = "Draw!";
            x = (600 - fm.stringWidth(str)) / 2;
            g.drawString(str, x, y);
        }

        if (x != 0) {
            playing = false;
        }
        button.setBounds(475, 10, 100, 50);
        button.repaint();
    }

    private enum sq {
        e,
        x,
        o
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            playing = true;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {   
                    board[i][j] = sq.e;
                }
            }
            fullSqrCount = 0;
            currentTurn = sq.x;
            repaint();
        }
    }
}