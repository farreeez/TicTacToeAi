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

    private void solveGame() {
        while (true) {
            boolean isSame = true;
            for (int i = 0; i < total; i++) {
                int[] actions = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
                char[] board = toTernary(i, actions);

                if (isPossibleBoard(board)) {
                    double[] actionValues = new double[9];
                    int emptyCells = getCount(board, '0') - 1;
                    double probability = 1.0 / emptyCells;

                    double oldAction = states.get(i)[0];

                    for (int j = 0; j < actions.length; j++) {
                        if (actions[j] != 0) {
                            for (int k = 0; k < emptyCells; k++) {
                                char[] newStateTernary = getNewState(board, actions[j], k);
                                int newStateInt = ternaryToDecimal(newStateTernary);
                                actionValues[j] = probability
                                        * (reward(newStateTernary) + gamma * states.get(newStateInt)[1]);
                            }
                        } else {
                            actionValues[j] = -10000000;
                        }
                    }

                    int newOptAction = actions[max(actionValues)];

                    if (isSame) {
                        isSame = newOptAction == oldAction;
                    }

                    double[] newState = { newOptAction, actionValues[max(actionValues)] };

                    states.replace(i, newState);
                }
            }

            if (isSame) {
            break;
            }
        }
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
            if (state[i] == currPlayer && state[i + 1] == currPlayer && state[i + 2] == currPlayer) {
                return true;
            } else if (state[i * 3] == currPlayer && state[i * 3 + 1] == currPlayer && state[i * 3 + 2] == currPlayer) {
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
            if (actions != null) {
                actions[pos] = num % 3;
            }
            pos--;
            num /= 3;
        }

        return ternary;
    }

    private int max(double[] arr) {
        int max = 0;

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
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
        board[action - 1] = bot;
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

        for (int i = 0; i < ternary.length; i++) {
            decimal += (int) Math.pow((int) ternary[i] - (int) '0', i);
        }

        return decimal;
    }
}
