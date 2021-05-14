package client.home.practice;

import client.dialogs.PromptDialog;
import client.dialogs.WarningDialog;
import database.ConnectionProvider;
import database.dao.PracticeDao;
import database.dao.UserDao;
import enums.Difficulty;
import enums.Status;
import database.models.Users;
import game.sudoku.SudokuChecker;
import game.sudoku.SudokuGenerator;
import helper.ScoreHandler;
import helper.StepLog;
import helper.TimeCount;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.net.Socket;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PracticeController {


    private final Users users;
    private final Socket socket;
    private Button index_btn;
    private TextField selected_field;
    private int indexX;
    private int indexY;
    private int[][] board;
    private int[][] solved_board;
    private int[][] frequence_board;
    private TextField[][] boardField;
    private ConcurrentHashMap<TextField, Boolean> validField;
    private final String selectRCcolor = "#5D5E60";
    private final ObservableList<Difficulty> difficulty = FXCollections.observableArrayList(Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD,Difficulty.EXTREME);
    private Stack<StepLog> stepLogs;
    private boolean gameState; // 1 for running 0 for stopped;
    private Timer timer;
    private PracticeDao practiceDao;
    private UserDao userDao;
    private int practice_id;
    private Thread query_executor;
    private boolean isAutocomplete;
    private ScoreHandler scoreTask;
    private TimeCount timeCount;
    private boolean isColored_Blue = false;

    @FXML
    private Button start_btn;

    @FXML
    private ComboBox<Difficulty> difficulty_option;

    @FXML
    private Label timeCounter;

    @FXML
    private Label max_points;

    @FXML
    private GridPane mainGP;

    @FXML
    private Button undo_btn;

    @FXML
    private Button reset_btn;

    public PracticeController(Users users, Socket socket) {
        this.users = users;
        this.socket = socket;
    }

    @FXML
    void initialize() {
        practiceDao = new PracticeDao(ConnectionProvider.getConnection(), users);
        userDao = new UserDao(ConnectionProvider.getConnection());
        gameState = false;
        start_btn.setText("Start");
        start_btn.setStyle("-fx-background-color: #2E82DC; -fx-text-fill:white; -fx-background-radius:8");
        difficulty_option.setItems(difficulty);
        undo_btn.setDisable(true);
        isAutocomplete = false;
        reset_btn.setDisable(true);
        validField = new ConcurrentHashMap<>();
        boardField = new TextField[9][9];
        difficulty_option.setDisable(false);
        max_points.setText("250 pts");
        timeCounter.setText("00:00");
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
        if(isColored_Blue){
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (validField.get(boardField[i][j])) {
                        boardField[i][j].setStyle("-fx-background-color:transparent ;-fx-text-fill:#2E82DC; -fx-border-color:white");
                    } else {
                        boardField[i][j].setStyle("-fx-background-color:transparent ;-fx-text-fill:white; -fx-border-color:white");
                    }
                }
            }
            isColored_Blue = false;
        }
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
        insertNumber(index_btn.getText());
    }

    private void setFrequency(int x, int y, int num) {
//        if frequence_board[x][y] is 000010000 and inserted with 2
//        frequence_board[x][y] will be 000010010
        if (num != 0)
            frequence_board[x][y] = frequence_board[x][y] | (1 << (num - 1));
        System.out.println("at " + x + "," + y + " : " + num);
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

    private void insertNumber(String input) {
        if (selected_field != null && validField.get(selected_field)) {
            StepLog log = new StepLog(indexX, indexY, selected_field.getText());
            selected_field.setText(input);
            if (!input.equals("")) {
                solved_board[indexX][indexY] = Integer.parseInt(input);
                setFrequency(indexX, indexY, solved_board[indexX][indexY]);
            } else {
                solved_board[indexX][indexY] = 0;
            }
            stepLogs.push(log);
            undo_btn.setDisable(false);
        }
    }

    @FXML
    void key_typed(KeyEvent e) {
        if (gameState) {
            System.out.println(e.getCode());
            if (e.getCode().getCode() == KeyCode.BACK_SPACE.getCode()) {
                insertNumber("");
            } else if (e.getCode().getCode() >= KeyCode.DIGIT1.getCode() && e.getCode().getCode() <= KeyCode.DIGIT9.getCode()) {
                insertNumber(e.getCode().getChar());
            } else if (e.getCode().getCode() >= KeyCode.NUMPAD1.getCode() && e.getCode().getCode() <= KeyCode.NUMPAD9.getCode()) {
                insertNumber(e.getCode().getChar());
            }
        }
    }

    @FXML
    void undoStep(ActionEvent event) {
        if (!stepLogs.empty()) {
            StepLog log = stepLogs.pop();
            boardField[log.getX()][log.getY()].setText(log.getValue());
            if (!log.getValue().equals(""))
                solved_board[log.getX()][log.getY()] = Integer.parseInt(log.getValue());
            else {
                solved_board[log.getX()][log.getY()] = 0;
            }
        }
        if (stepLogs.empty())
            undo_btn.setDisable(true);
    }

    @FXML
    void hover_exit_on_btn(MouseEvent event) {
        Button button = (Button) event.getSource();

        button.setStyle("-fx-background-color: transparent; -fx-border-radius:3;-fx-border-color: #707070");
    }

    @FXML
    void hover_on_btn(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color:#2E2E2E; -fx-border-radius:3;-fx-background-radius:3");
    }

    @FXML
    void reset_board(ActionEvent event) {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {

                int num = board[x][y];
                solved_board[x][y] = num;

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
    void showHint(ActionEvent event) {

    }


    @FXML
    void start_game(ActionEvent event) {
        if (!gameState) {
            try {
                if (difficulty_option.getValue() == null) {
                    PromptDialog promptDialog = new PromptDialog("Choose Difficulty Level", "to start your game please choose a difficulty level");
                    promptDialog.start();
                } else {
                    start_btn.setText("Terminate");
                    start_btn.setStyle("-fx-background-color: #EF2D2D; -fx-text-fill:white; -fx-background-radius:8");
                    startGame(difficulty_option.getValue());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                WarningDialog warningDialog = new WarningDialog("Terminate", "Are you sure? Termination of game will not reward any points and your progress in this game will be erased");
                if (warningDialog.start()) {

                    terminateGame();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void startGame(Difficulty difficulty) {
        board = new int[9][9];
        solved_board = new int[9][9];
        frequence_board = new int[9][9];

        SudokuGenerator sudokuGenerator = new SudokuGenerator();
        sudokuGenerator.setDifficulty(difficulty);
        board = sudokuGenerator.generateBoard();
        reset_btn.setDisable(false);
        difficulty_option.setDisable(true);
        this.stepLogs = new Stack<>();
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                int num = board[x][y];
                solved_board[x][y] = num;
                if (num != 0) {
                    boardField[x][y].setStyle("-fx-background-color:transparent; -fx-text-fill:white; -fx-border-color:white");
                    boardField[x][y].setText(String.valueOf(num));
                    boardField[x][y].setEditable(false);
                    validField.put(boardField[x][y], false);
                    frequence_board[x][y] = (1 << (num - 1));
                } else {
                    boardField[x][y].setStyle("-fx-background-color:transparent; -fx-text-fill:#2E82DC; -fx-border-color:white");
                    boardField[x][y].setText("");
                    validField.put(boardField[x][y], true);
                    frequence_board[x][y] = 0;
                }
            }
        }


        gameState = true;
        Timestamp now = new Timestamp(new Date().getTime());
        query_executor = new Thread(() -> {
            int practice_id = practiceDao.startPractice(now, difficulty);
            userDao.setUserStatus(users.getUser_id(), Status.IN_PRACTICE);

        }, "query executor");
        query_executor.start();
        startTimer();

    }

    private void startTimer() {
        scoreTask = new ScoreHandler(max_points, 250);
        timeCount = new TimeCount(timeCounter, 0);
        timer = new Timer("practice");
        timer.schedule(timeCount, 0, 1000);
        timer.schedule(scoreTask, 1000 * 60, 1000 * 60);
    }


    private void terminateGame() {
        try {
            query_executor.join();
            if (practice_id != 0) {
                query_executor = new Thread(() -> {
                    practiceDao.cancelPractice(practice_id);
                    userDao.setUserStatus(users.getUser_id(),Status.AVAILABLE);
                }, "query executor");
                query_executor.start();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopTimer();
        initialize();
    }

    private void stopTimer() {
        timer.cancel();
    }

    @FXML
    void submit(ActionEvent event) {
        try {
            System.out.println(scoreTask.getMaxScore());
            if (isBoardCompleted()) {
                WarningDialog dialog = new WarningDialog("Submit Game", "Are you ready to submit the board?\nNote: in case of incorrect submission points will be deducted");
                if (dialog.start()) {
                    Timestamp ended_at = new Timestamp(new Date().getTime());
                    SudokuChecker checker = new SudokuChecker(solved_board, board);
                    Pair<Boolean, ArrayList<Pair<Integer, Integer>>> result = checker.runCheck();
                    if (result.getKey()) {
                        stopTimer();
                        int score = calculateResult();
                        PromptDialog congrats = new PromptDialog("Successful Submission",String.format("***********************************\n" +
                                "***********************************\n" +
                                "*******                    ********\n" +
                                "*******   Congratulation   ********\n" +
                                "*******                    ********\n" +
                                "*******    Your Score      ********\n" +
                                "*******        %03d         ********\n" +
                                "*******                    ********\n" +
                                "***********************************\n" +
                                "***********************************",score));
                        congrats.start();
                        //show congratulations and result
                        System.out.println("***********************************");
                        System.out.println("***********************************");
                        System.out.println("**********               **********");
                        System.out.println("*********                 *********");
                        System.out.println("********                   ********");
                        System.out.println("*****   Congrat\"LOL\"ulation   *****");
                        System.out.println("********                   ********");
                        System.out.println("*********                 *********");
                        System.out.println("**********               **********");
                        System.out.println("***********************************");
                        System.out.println("***********************************");

                        query_executor.join();
                        query_executor = new Thread(() -> {
                            practiceDao.submitPractice(practice_id, ended_at, score);
                            userDao.updateScore(users.getUser_id(),score);
                            userDao.setUserStatus(users.getUser_id(),Status.AVAILABLE);
                        }, "query executor");
                        query_executor.start();
                        stopTimer();
                        initialize();
                    } else {
                        scoreTask.deductScore(20);
                        ArrayList<Pair<Integer, Integer>> incorrectInputs = result.getValue();
                        for (Pair<Integer, Integer> coordi : incorrectInputs) {
                            boardField[coordi.getKey()][coordi.getValue()].setStyle("-fx-background-color:rgba(200,45,45,0.5); -fx-text-fill:white; -fx-border-color:white");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int calculateResult() {
        int max = scoreTask.getMaxScore();
        int penalty = 2;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                frequence_board[i][j] = frequence_board[i][j] ^ (1<<(solved_board[i][j]-1));
                if (frequence_board[i][j] != 0) {
                    while (frequence_board[i][j] > 0) {
                        if (max == 0)
                            return 0;
                        if (frequence_board[i][j] % 2 == 1)
                            max -= penalty;
                        frequence_board[i][j] >>= 1;
                    }
                }
            }
        }
        return max;
    }

    private boolean isBoardCompleted() {
        boolean isValid = true;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (solved_board[i][j] == 0) {
                    boardField[i][j].setStyle("-fx-background-color:rgba(46,160,246,0.5); -fx-text-fill:white; -fx-border-color:white");
                    isValid = false;
                }
            }
        }
        if(!isValid){
            isColored_Blue = true;
        }
        return isValid;
    }
}
