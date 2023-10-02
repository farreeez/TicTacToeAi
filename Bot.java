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
        // Main.sq[][] board = { { Main.sq.x, Main.sq.o, Main.sq.x },
        // { Main.sq.e, Main.sq.e, Main.sq.e },
        // { Main.sq.e, Main.sq.e, Main.sq.e } };
        // // System.out.println(getPlay(board)[0]);
        // // System.out.println(getPlay(board)[1]);
        // // System.out.println(getCount(toTernary(11664, null), '0'));
        // char[] oldBoard = { '1', '2', '1', '0', '0', '0', '0', '0', '0' };
        char[] actions = toTernary(6561, null);
        for (int i = 0; i < actions.length; i++) {
            // System.out.println(actions[i]);
        }

        // System.out.println(ternaryToDecimal(toTernary(100, null)));

        // for (int i = 0; i < total; i++) {
        // if (states.get(i)[0] != 0) {
        // System.out.println(states.get(i)[0]);
        // }
        // }

        System.out.println(reward(toTernary(14427, null)));
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
            boolean isSame = true;
            for (int i = 0; i < total; i++) {
                int[] actions = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };//
                char[] board = toTernary(i, actions);//

                if (isPossibleBoard(board)) {//
                    if (boardContainsEmpty(board)) {
                        double[] actionValues = new double[9];
                        int emptyCells = getCount(board, '0') - 1;//
                        double probability = 1.0 / emptyCells;//

                        double oldAction = states.get(i)[0];

                        for (int j = 0; j < actions.length; j++) {
                            if (actions[j] != 0) {
                                for (int k = 0; k < emptyCells; k++) {
                                    char[] newStateTernary = getNewState(board, j, k);//
                                    if (i == 13932 && j == 3 && k == 2) {
                                        for (int z = 0; z < newStateTernary.length; z++) {
                                            System.out.print(newStateTernary[z] + ", ");
                                        }
                                        System.out.println();
                                        System.out.println("-----------------------");
                                    }
                                    int newStateInt = ternaryToDecimal(newStateTernary);//
                                    actionValues[j] = probability
                                            * (reward(newStateTernary) + gamma * states.get(newStateInt)[1]);
                                }
                            } else {
                                actionValues[j] = -10;
                            }
                        }

                        int newOptAction = max(actionValues);

                        if (isSame) {
                            isSame = newOptAction == oldAction;
                        }

                        double[] newState = { newOptAction, actionValues[max(actionValues)] };

                        states.replace(i, newState);
                        if (i == 13932) {
                            for (int z = 0; z < actionValues.length; z++) {
                                System.out.print(actionValues[z] + ", ");
                            }
                            System.out.println();
                            System.out.println(newOptAction);
                        }
                    } else {
                        // if(i == 18625){
                        // for (int z = 0; z < board.length; z++) {
                        // System.out.print(board[z] + ", ");
                        // }
                        // System.out.println();
                        // System.out.println(reward(board));
                        // }
                        double[] newState = { -1, reward(board) };
                        states.replace(i, newState);
                    }
                }
            }

            if (isSame) {
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
            return -10000000.0;
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
