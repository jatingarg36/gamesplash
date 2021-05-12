package client.home.practice;

import game.sudoku.SudokuGenerator;
import helper.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

public class PracticeController {

    private Button index_btn;
    private TextField selected_field;
    private int indexX;
    private int indexY;
    private int[][] board = new int[9][9];
    private int[][] solved_board = new int[9][9];
    private TextField[][] boardField;
    private ConcurrentHashMap<TextField, Boolean> validField;
    private String selectRCcolor = "#5D5E60";
    private ObservableList<String> difficulty = FXCollections.observableArrayList("easy", "medium", "difficult");
    private Stack<StepLog> stepLogs;

    @FXML
    private ComboBox<String> difficulty_option;

    @FXML
    private Label timeCounter;

    @FXML
    private Label points;

    @FXML
    private GridPane mainGP;

    @FXML
    private Button undo_btn;

    @FXML
    void initialize() {
        difficulty_option.setItems(difficulty);
        undo_btn.setDisable(true);
        validField = new ConcurrentHashMap<>();
        boardField = new TextField[9][9];

        ObservableList<Node> child = mainGP.getChildren();
        for (Node node : child) {
            int Y = GridPane.getColumnIndex(node) - 1;
            int X = GridPane.getRowIndex(node) - 1;
            GridPane ch = (GridPane) node;
            ObservableList<Node> m = ch.getChildren();
            for (Node n : m) {
                int y = GridPane.getColumnIndex(n) - 1;
                int x = GridPane.getRowIndex(n) - 1;
                TextField t = (TextField) n;
                t.setText("");
                boardField[(X * 3 + x)][(Y * 3 + y)] = t;
                validField.put(t, true);
//                indexField.put(t,new Pair<>((x+3*X),(y+3*Y)));
            }
        }
    }

    @FXML
    void autocomplete(ActionEvent event) {

    }

    @FXML
    void selectField(MouseEvent event) {

        if (selected_field != null) {
            if (validField.get(selected_field)) {
                for (int i = 0; i < 9; i++) {
                    if (validField.get(boardField[i][indexY])) {
                        boardField[i][indexY].setStyle("-fx-background-color:transparent ;-fx-text-fill:#2E82DC; -fx-border-color:white");
                    } else {
                        boardField[i][indexY].setStyle("-fx-background-color:transparent ;-fx-text-fill:white; -fx-border-color:white");
                    }
                    if (validField.get(boardField[indexX][i])) {
                        boardField[indexX][i].setStyle("-fx-background-color:transparent; -fx-text-fill:#2E82DC; -fx-border-color:white");
                    } else {
                        boardField[indexX][i].setStyle("-fx-background-color:transparent; -fx-text-fill:white; -fx-border-color:white");
                    }
                }
            } else {
                selected_field.setStyle("-fx-background-color:transparent; -fx-text-fill:white; -fx-border-color:white");
            }
        }
        Node n = (Node) event.getSource();
        selected_field = (TextField) n;
        int Y = GridPane.getColumnIndex(n.getParent()) - 1;
        int X = GridPane.getRowIndex(n.getParent()) - 1;
        int y = GridPane.getColumnIndex((Node) event.getSource()) - 1;
        int x = GridPane.getRowIndex((Node) event.getSource()) - 1;
//        System.out.println((X*3+x)+","+(Y*3+y));
        int a = x + X * 3;
        int b = y + Y * 3;
        indexX = a;
        indexY = b;
        if (validField.get(selected_field)) {
            for (int i = 0; i < 9; i++) {
                if (validField.get(boardField[i][indexY])) {
                    boardField[i][indexY].setStyle("-fx-background-color:" + selectRCcolor + " ;-fx-text-fill:#2E82DC; -fx-border-color:white");
                } else {
                    boardField[i][indexY].setStyle("-fx-background-color:" + selectRCcolor + " ;-fx-text-fill:white; -fx-border-color:white");
                }
                if (validField.get(boardField[indexX][i])) {
                    boardField[indexX][i].setStyle("-fx-background-color:" + selectRCcolor + " ; -fx-text-fill:#2E82DC; -fx-border-color:white");
                } else {
                    boardField[indexX][i].setStyle("-fx-background-color:" + selectRCcolor + " ; -fx-text-fill:white; -fx-border-color:white");
                }
            }
            boardField[indexX][indexY].setStyle("-fx-background-color:#3B3B3B; -fx-text-fill:#2E82DC; -fx-border-color:white");
        } else {
            selected_field.setStyle("-fx-background-color:#3B3B3B; -fx-text-fill:white; -fx-border-color:white");
        }
    }

    @FXML
    void btn_clicked(MouseEvent event) {
        index_btn = (Button) event.getSource();
        if (selected_field != null && validField.get(selected_field)) {
            StepLog log = new StepLog(indexX, indexY,selected_field.getText());
            selected_field.setText(index_btn.getText());
            if(!index_btn.getText().equals(""))
                solved_board[indexX][indexY] = Integer.parseInt(index_btn.getText());
            else{
                solved_board[indexX][indexY] = 0;
            }
            stepLogs.push(log);
            undo_btn.setDisable(false);
        }
    }

    @FXML
    void btn_pressed(MouseEvent event) {
        Button btn = (Button) event.getSource();
        btn.setStyle("-fx-background-color: white;-fx-text-fill:#707070; -fx-border-radius:3;-fx-border-color: #707070; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 0);");

    }

    @FXML
    void btn_released(MouseEvent event) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Button btn = (Button) event.getSource();
        btn.setStyle("-fx-background-color: transparent; -fx-border-radius:3;-fx-border-color: #707070");
    }

    @FXML
    void undoStep(ActionEvent event) {
        if (!stepLogs.empty()) {
            StepLog log = stepLogs.pop();
            boardField[log.getX()][log.getY()].setText(log.getValue());
            if(!log.getValue().equals(""))
                solved_board[log.getX()][log.getY()] = Integer.parseInt(log.getValue());
            else{
                solved_board[log.getX()][log.getY()] = 0;
            }
        }
        if (stepLogs.empty())
            undo_btn.setDisable(true);
    }

    @FXML
    void hover_exit_on_btn(MouseEvent event) {
        Button button = (Button) event.getSource();

        if (button != index_btn) {
            button.setStyle("-fx-background-color: transparent; -fx-border-radius:3;-fx-border-color: #707070");
        }
    }

    @FXML
    void hover_on_btn(MouseEvent event) {
        Button button = (Button) event.getSource();

        if (button != index_btn) {
            button.setStyle("-fx-background-color:#2E2E2E; -fx-border-radius:3;-fx-background-radius:3");
        }
    }

    @FXML
    void renew_board(ActionEvent event) {
        solved_board = board;
        for (int x = 0; x < boardField.length; x++) {
            for (int y = 0; y < boardField[x].length; y++) {

                int num = board[x][y];
                if (num != 0) {
                    boardField[x][y].setText(String.valueOf(num));
                    boardField[x][y].setEditable(false);
                    validField.put(boardField[x][y], false);
                } else {
                    boardField[x][y].setText("");
                    validField.put(boardField[x][y], true);
                }
            }
        }
    }

    @FXML
    void showHint(ActionEvent event) {

    }

    @FXML
    void start_game(ActionEvent event) {
        this.stepLogs = new Stack<>();
        SudokuGenerator sudokuGenerator = new SudokuGenerator();
        sudokuGenerator.setDifficulty(difficulty_option.getValue());
        board = sudokuGenerator.generateBoard();
        solved_board = board;
        for (int x = 0; x < boardField.length; x++) {
            for (int y = 0; y < boardField[x].length; y++) {

                int num = board[x][y];
                if (num != 0) {
                    boardField[x][y].setStyle("-fx-background-color:transparent; -fx-text-fill:white; -fx-border-color:white");
                    boardField[x][y].setText(String.valueOf(num));
                    boardField[x][y].setEditable(false);
                    validField.put(boardField[x][y], false);
                } else {
                    boardField[x][y].setStyle("-fx-background-color:transparent; -fx-text-fill:#2E82DC; -fx-border-color:white");
                    boardField[x][y].setText("");
                    validField.put(boardField[x][y], true);
                }
            }
        }

    }

    @FXML
    void submit(ActionEvent event) {

    }
}
