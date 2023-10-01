import java.util.HashMap;

public class Bot {
    private HashMap<Integer, Integer> states = new HashMap<>();
    private int total = (int) Math.pow(3, 9);
    private char bot;
    private char player;

    public Bot(int num) {
        bot = Character.forDigit(num, 10);
        if (num == 1) {
            player = Character.forDigit(2, 10);
        } else {
            player = Character.forDigit(1, 10);
        }
        for (int i = 0; i < total; i++) {
            states.put(i, 0);
        }
    }

    private void solveGame() {
        while (true) {
            for (int i = 0; i < total; i++) {
                int[] actions = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
                char[] board = toTernary(i, actions);

                if (isPossibleBoard(board)) {
                    
                    double[] actionValues = new double[9];
                    double probability = 0;
                    int emptyCells = 0;
                    for (int j = 0; j < actions.length; j++) {
                        if (actions[j] != 0) {
                            for (int k = 0; k < emptyCells; k++) {
                                actionValues[j] += probability * (reward() + states.get(i));
                            }
                        } else {
                            actionValues[j] = -10000000;
                        }
                    }

                    states.replace(i, actions[max(actionValues)]);
                }
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
            actions[pos] = num % 3;
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

}
