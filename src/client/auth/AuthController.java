package client.auth;

import client.Client;
import client.home.DashBoard;
import database.ConnectionProvider;
import database.dao.UserDao;
import database.models.Users;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Objects;

public class AuthController {

    @FXML
    private Label auth_label;
    @FXML
    private TextField username;
    @FXML
    private Label uname_error;
    @FXML
    private PasswordField password;
    @FXML
    private Label pass_error;
    @FXML
    private TextField client_port;
    @FXML
    private Label port_error;
    @FXML
    private Label auth_hint;
    @FXML
    private Label auth_hint_bold;
    @FXML
    private Button auth_btn;

    private boolean state = true; // true for login, false for signup
    private boolean isAlive = true;
    private static Connection con;

    @FXML
    void initialize() {
        con = ConnectionProvider.getConnection();
    }

    @FXML
    void authenticate(ActionEvent event) {
        processState(false);
        uname_error.setText(null);
        pass_error.setText(null);
        port_error.setText(null);
        uname_error.setVisible(true);
        pass_error.setVisible(true);
        port_error.setVisible(true);

        UserDao dao = new UserDao(con);
        if (validate_port() && valid_credentials(dao)) {
            Users user;
            try {
                if (state)
                    user = login(dao);
                else
                    user = register(dao);

                if (user != null) {
                    int port = Integer.parseInt(client_port.getText());
                    Socket clientSocket = new Client(port).getSocket();
                    this.startClientHome(user, clientSocket,event);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("here");
        processState(true);
    }

    private void processState(boolean isActive) {
        if (!isActive) {
            auth_btn.setText("Loading...");
            auth_btn.setCursor(Cursor.WAIT);
        } else {
            System.out.println("here again");
            if (state)
                auth_btn.setText("login");
            else
                auth_btn.setText("signup");
            auth_btn.setCursor(Cursor.HAND);
        }
    }


    private Users login(UserDao userDao) throws IOException {

        String uname = username.getText();
        String pass = String.valueOf(password.getText());

        Pair<String, Users> currentUser = userDao.loginUser(uname, pass);

        switch (currentUser.getKey()) {
            case "success":
                System.out.println(currentUser.getValue().getUsername());
                return currentUser.getValue();
            case "invalid password":
                pass_error.setText("Invalid Password");
                break;
            default:
                uname_error.setText("Couldn't login try again");
                password.setText("");
                break;
        }
        return null;
    }


    private Users register(UserDao userDao) throws IOException {
        String uname = username.getText();
        String pass = String.valueOf(password.getText());

        Pair<String, Users> currentUser = userDao.registerUser(uname, pass);

        if (currentUser.getKey().equals("success")) {
            return currentUser.getValue();
        } else {
            uname_error.setText("Couldn't register try again: " + currentUser.getKey());
            password.setText("");
        }
        return null;

    }

    private void startClientHome(Users user, Socket clientSocket,ActionEvent e) throws IOException {
        processState(true);
        ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
        new DashBoard(user,clientSocket).start(new Stage());
    }


    private boolean validate_port() {
        String err_msg = "unknown error";
        try {
            if (client_port.getText() == null) {
                throw new NullPointerException("null value");
            }
            NumberFormat.getInstance().parse(client_port.getText());
            int port = Integer.parseInt(client_port.getText());
            if (port > 1023 && port < 65535) {
                return true;
            } else {
                err_msg = "port address range exploited: must be within 1024-65534";
            }
        } catch (NullPointerException np) {
            err_msg = "port cannot be null : " + np.getLocalizedMessage();
        } catch (ParseException | NumberFormatException e) {
            err_msg = e.getLocalizedMessage() + ": " + "Invalid Input in port Field (port must be integer)";
        }
        port_error.setText(err_msg);
        return false;
    }

    private boolean valid_credentials(UserDao userDao) {
        boolean valid = false;
        String uname = username.getText();
        String pass = password.getText();
        System.out.println(uname + " : " + pass);
        if (uname.equals("") || pass.equals("")) {
            if (uname.equals("")) {
                uname_error.setText("field cannot be null");
            }
            if (pass.equals("")) {
                pass_error.setText("field cannot be null");
            }
            return false;
        }
        if (!state) {
            if (userDao.checkUser(uname)) {
                uname_error.setText("user already registered");
            } else {
                valid = true;
            }
        } else {
            if (userDao.checkUser(uname)) {
                valid = true;
            } else {
                uname_error.setText("no such user exists");
            }
        }
        return valid;
    }

    @FXML
    void toggle(MouseEvent event) {
        if (state) {
            auth_label.setText("Signup");
            auth_btn.setText("signup");
            auth_hint_bold.setText("Login");
            auth_hint.setText("Already Registered");
        } else {
            auth_btn.setText("login");
            auth_label.setText("Login");
            auth_hint_bold.setText("Signup");
            auth_hint.setText("Not Registered yet?");
        }
        this.state = !state;
    }

    @FXML
    void close(ActionEvent e){
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.close();
    }
}
