/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.sudoku;

import java.util.Arrays;
import java.util.Scanner;

// code for solving sudoku
public class SudokuSolver {

    private int[][] board;
    private boolean solved;
    private boolean solvable;

    public SudokuSolver(int[][] board) {
        this.board = board;
        this.solved = false;
        this.solvable = true;

    }

    private void setSolved(boolean solved) {
        this.solved = solved;
    }

    private void setSolvable(boolean solvable) {
        this.solvable = solvable;
    }

    public boolean isSolved() {
        return solved;
    }

    public boolean isSolvable() {
        return solvable;
    }

    
    public int[][] getBoard() {
        return board;
    }

   

    // check is operation is valid or not;
    private boolean isValid(int x, int y, int no) {

        for (int i = 0; i < 9; i++) {
            if (board[x][i] == no || board[i][y] == no) {
                return false;
            }
        }
        x = x - x % 3;
        y = y - y % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[x + i][y + j] == no) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean solve() {
        int x = -1;
        int y = -1;
        boolean found = false;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    x = i;
                    y = j;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }
        // solved
        if (!found) {
            setSolved(true);
            return true;
        }

        int available[] = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        int tmp;

        // shuffling list
        for (int i = 9; i > 0; i--) {
            int index = (int) (Math.random() * i) % (i);
            tmp = available[index];
            available[index] = available[i - 1];
            available[i - 1] = tmp;
        }

        for (int no : available) {
            if (isValid(x, y, no)) {
                board[x][y] = no;
                if (solve()) {
                    return true;
                } else {
                    board[x][y] = 0;
                }
            }
        }
        setSolvable(false);
        return false;
    }

    public void printBoard( int[][] board){
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0) {
                for (int j = 0; j < 8; j++) {
                    System.out.print(" - ");
                }
                System.out.println("");
            }
            for (int j = 0; j < 9; j++) {
                if (j % 3 == 0) {
                    System.out.print("| ");
                }
                System.out.print(board[i][j] + " ");
            }
            System.out.println("");
        }
        for (int j = 0; j < 8; j++) {
            System.out.print(" = ");
        }
    }

//    public static void main(String[] args) {
//        SudokuGenerator sudokuGenerator = new SudokuGenerator();
//        Scanner in = new Scanner(System.in);
//        System.out.println("Enter the difficulty: ");
//        sudokuGenerator.setDifficulty(in.nextInt());
//        int[][] board = sudokuGenerator.generateBoard();
//
//        SudokuSolver sudoku = new SudokuSolver(board);
//        if (sudoku.solved) {
//            for (int i = 0; i < 9; i++) {
//                for (int j = 0; j < 9; j++) {
//                    System.out.print(board[i][j] + " ");
//                }
//                System.out.println("");
//            }
//        } else if(!sudoku.solvable){
//            System.out.println("Cant solve ");
//        }
//
//    }

}
