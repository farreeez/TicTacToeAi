import java.util.Arrays;
import java.util.HashMap;

public class Bot {
    private HashMap<Integer, double[]> states = new HashMap<>();
    private int total = (int) Math.pow(3, 9);
    private char bot;
    private char player;
    private double gamma = 0.95;

    public Bot(int num) {
        bot = Character.forDigit(num, 10);
        if (num == 1) {
            player = Character.forDigit(2, 10);
        } else {
            player = Character.forDigit(1, 10);
        }
        for (int i = 0; i < total; i++) {
            double[] arr = { 0, 0 };
            states.put(i, arr);
        }
        solveGame();
    }

    public int[] getPlay(Main.sq[][] board) {
        int state = boardToDecimal(board);
        int square = (int) states.get(state)[0];
        int squareY;
        int squareX;

        if (square > -1) {
            squareY = square / 3;
            squareX = square % 3;
        } else {
            squareX = -1;
            squareY = -1;
        }

        int[] squarePos = { squareY, squareX };
        return squarePos;
    }

    private int boardToDecimal(Main.sq[][] board) {
        double decimalValue = 0;
        int power = 8;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                int currentSquare = getNumValue(board[i][j]);
                decimalValue += currentSquare * Math.pow(3, power);
                power--;
            }
        }
        return (int) decimalValue;
    }

    private int getNumValue(Main.sq play) {
        if (play.equals(Main.sq.x)) {
            return 1;
        } else if (play.equals(Main.sq.o)) {
            return 2;
        } else {
            return 0;
        }
    }

    private void solveGame() {
        while (true) {
            boolean stop = true;
            double oldAction = 0;
            double newAction = 0;
            for (int i = 0; i < total; i++) {
                int[] actions = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
                double[] actionRewards = { -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000 };
                char[] board = toTernary(i, actions);
                int emptyCells = getCount(board, '0') - 1;
                double probability = 1.0 / emptyCells;
                if (isPossibleBoard(board)) {
                    oldAction = states.get(i)[1];
                    if (boardContainsEmpty(board) && reward(board) == 0) {
                        for (int j = 0; j < actions.length; j++) {
                            if (actions[j] != 0) {
                                actionRewards[j] = 0;
                                for (int k = 0; k < emptyCells; k++) {
                                    char[] newBoard = getNewState(board, j, k);
                                    actionRewards[j] += probability
                                            * (reward(newBoard) + gamma * states.get(ternaryToDecimal(newBoard))[1]);
                                }
                            }
                        }
                        int max = max(actionRewards);
                        
                        double[] newState = { max, actionRewards[max] };
                        states.replace(i, newState);
                    } else {
                        double[] newState = { -1, reward(board) };
                        states.replace(i, newState);
                    }
                    newAction = states.get(i)[1];
                    if (stop) {
                        stop = oldAction == newAction;
                    }
                }
            }

            if (stop) {
                break;
            }
        }
    }

    private boolean boardContainsEmpty(char[] board) {
        for (int i = 0; i < board.length; i++) {
            if (board[i] == '0') {
                return true;
            }
        }
        return false;
    }

    private Double reward(char[] state) {
        if (reward(state, bot)) {
            return 1.0;
        } else if (reward(state, player)) {
            return -1.0;
        }

        return 0.0;
    }

    private boolean reward(char[] state, char currPlayer) {
        for (int i = 0; i < 3; i++) {
            if (state[i * 3] == currPlayer && state[i * 3 + 1] == currPlayer && state[i * 3 + 2] == currPlayer) {
                return true;
            } else if (state[i] == currPlayer && state[i + 3] == currPlayer && state[i + 6] == currPlayer) {
                return true;
            }
        }

        if (state[0] == currPlayer && state[4] == currPlayer && state[8] == currPlayer) {
            return true;
        } else if (state[6] == currPlayer && state[4] == currPlayer && state[2] == currPlayer) {
            return true;
        }

        return false;
    }

    private boolean isPossibleBoard(char[] board) {
        int xCount = 0;
        int oCount = 0;

        for (int i = 0; i < board.length; i++) {
            if (board[i] == '1') {
                xCount++;
            } else if (board[i] == '2') {
                oCount++;
            }
        }

        int difference = xCount - oCount;

        return (difference <= 1 && difference >= 0);
    }

    private char[] toTernary(int num, int[] actions) {
        char[] ternary = { '0', '0', '0', '0', '0', '0', '0', '0', '0' };
        int pos = 8;

        while (num != 0) {
            ternary[pos] = Character.forDigit(num % 3, 3);
            if (actions != null && num % 3 > 0) {
                actions[pos] = 0;
            }
            pos--;
            num /= 3;
        }

        return ternary;
    }

    private int max(double[] arr) {
        int max = 0;

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > arr[max]) {
                max = i;
            }
        }

        return max;
    }

    private int getCount(char[] board, char play) {
        int count = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == play) {
                count++;
            }
        }
        return count;
    }

    private char[] getNewState(char[] oldBoard, int action, int emptyCell) {
        char[] board = Arrays.copyOf(oldBoard, oldBoard.length);
        board[action] = bot;
        int emptyPos = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == '0') {
                if (emptyPos == emptyCell) {
                    board[i] = player;
                    break;
                }
                emptyPos++;
            }
        }
        return board;
    }

    private int ternaryToDecimal(char[] ternary) {
        int decimal = 0;
        int power = 8;

        for (int i = 0; i < ternary.length; i++) {
            int currentSquare = (int) ternary[i] - (int) '0';
            decimal += ((int) (Math.pow(3, power))) * currentSquare;
            power--;
        }

        return decimal;
    }
}
