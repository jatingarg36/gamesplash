package game.sudoku;

public class SudokuGenerator {

    private int[][] board;
    private final int N; // only 9
    private int difficulty_level; // [1,2,3]
    private int K; // number of empty cells depending upon difficulty_level;

    public SudokuGenerator() {
        this.N = 9;
        this.K = 16;
        this.board = new int[9][9];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.board[i][j] = 0;
            }
        }
    }

    public void setDifficulty(String difficulty) {
        switch (difficulty) {
            case "medium":
                difficulty_level = 2;
                break;
            case "difficult":
                difficulty_level = 3;
                break;
            default:
                difficulty_level = 1;
                break;
        }

        int max = 64, min = 16;
        // Choosing max and min for deciding         
        switch (difficulty_level) {
            case 1:
                max = 24;
                break;
            case 2:
                max = 36;
                min = 24;
                break;
            case 3:
                max = 48;
                min = 36;
                break;
            default:
                break;
        }
        this.K = (int) (Math.random() * (max - min + 1) + min);
    }

    public int[][] generateBoard() {

        SudokuSolver sudoku = new SudokuSolver(board);
        if (sudoku.solve()) {
            this.board = sudoku.getBoard();
        }
        else if(!sudoku.isSolvable()){
            return null;
        }

        for (int i = 0; i < K; i++) {
            int currentX = (int) (Math.random() * 10) % 9;
            int currentY = (int) (Math.random() * 10) % 9;
            while (board[currentX][currentY] == 0) {
                currentX = (int) (Math.random() * 10) % 9;
                currentY = (int) (Math.random() * 10) % 9;
            }
            board[currentX][currentY] = 0;
        }
        sudoku.printBoard(board);

        return this.board;
    }

//    public static void main(String[] args) {
//        SudokuGenerator sudokuGenerator = new SudokuGenerator();
//
//        sudokuGenerator.setDifficulty(1);
//        sudokuGenerator.generateBoard();
//
//        sudokuGenerator.setDifficulty(2);
//        sudokuGenerator.generateBoard();
//
//        sudokuGenerator.setDifficulty(3);
//        sudokuGenerator.generateBoard();
//
//    }

}
