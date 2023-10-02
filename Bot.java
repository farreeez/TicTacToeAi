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
    }

    private void solveGame() {
        while (true) {
            boolean isSame = true;
            for (int i = 0; i < total; i++) {
                int[] actions = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
                char[] board = toTernary(i, actions);

                if (isPossibleBoard(board)) {
                    double[] actionValues = new double[9];
                    double probability = 0;
                    int emptyCells = getCount(board, '0') - 1;

                    double[] oldState = states.get(i);

                    for (int j = 0; j < actions.length; j++) {
                        if (actions[j] != 0) {
                            for (int k = 0; k < emptyCells; k++) {
                                actionValues[j] += probability * (reward() + gamma * oldState[1]);
                            }
                        } else {
                            actionValues[j] = -10000000;
                        }
                    }

                    int newOptAction = actions[max(actionValues)];

                    if (isSame) {
                        isSame = newOptAction == oldState[0];
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

    private Double reward() {
        return null;
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

}
