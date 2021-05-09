package game.sudoku;

public class SudokuChecker {

    private final int[][] board;
    private final int N;

    public SudokuChecker(int[][] board) {
        this.board = board;
        this.N = 9;
    }

    public boolean checkValidity() {

        for (int i = 0; i < N; i++) {
            int sum = 0;
            for (int j = 0; j < N; j++) {
                sum += board[i][j];
            }
            if (sum != 45) {
                return false;
            }
        }

        for (int i = 0; i < N; i += Math.sqrt(N)) {
            for (int j = 0; j < N; j += Math.sqrt(N)) {
                int sum = 0;
                for (int x = i; x < i + Math.sqrt(N); x++) {
                    for (int y = j; y < j + Math.sqrt(N); y++) {
                        sum += board[x][y];
                    }
                }
                if (sum != 45) {
                    return false;
                }
            }
        }
        
        return true;
    }

}
