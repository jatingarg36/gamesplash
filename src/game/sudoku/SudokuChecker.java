package game.sudoku;

import javafx.util.Pair;

import java.util.ArrayList;

public class SudokuChecker {

    private final int[][] solved_board;
    private final int[][] board;
    private final int N;

    public SudokuChecker(int[][] solved_board, int[][] board) {
        this.solved_board = solved_board;
        this.board = board;
        this.N = 9;
    }

    public boolean checkValidity() {

        for (int i = 0; i < N; i++) {
            int sum = 0;
            for (int j = 0; j < N; j++) {
                sum += solved_board[i][j];
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
                        sum += solved_board[x][y];
                    }
                }
                if (sum != 45) {
                    return false;
                }
            }
        }
        
        return true;
    }

    private ArrayList<Pair<Integer,Integer>> checkInvalids(){
        ArrayList<Pair<Integer,Integer>> invalids = new ArrayList<>();
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(board[i][j]==0) {
                    if(checkPresence(i,j)){
                        invalids.add(new Pair<>(i,j));
                    }
                }
            }
        }
        return invalids;
    }

    private boolean checkPresence(int x, int y) {
        int num= solved_board[x][y];
        for(int i=0;i<9;i++){
            if(i!=y && solved_board[x][i] == num)
                    return true;
            if(i!=x && solved_board[i][y]==num)
                return true;
        }
        return false;
    }

    public Pair<Boolean, ArrayList<Pair<Integer, Integer>>> runCheck() {
        if(checkValidity())
            return new Pair<>(true,null);
        return new Pair<>(false, checkInvalids());
    }
}
