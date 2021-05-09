package server.Init;

import client.auth.AuthScreen;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import server.Server;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ResourceBundle;

public class Controller{


    @FXML private TextField server_port_input;
    @FXML private Button btn_start_server;
    @FXML private Label error_display;

    private boolean state = false;
    private Server server = null;
    private double x, y;

    public Controller() {
    }


    @FXML
    private void startServer() {
        if (this.state) {
            this.state = false;

            if (server != null) {
                server.Stop();
            }

            toggleUIState();

        } else {
            int valid = 0;
            String msg = "";
            try {
                NumberFormat.getInstance().parse(server_port_input.getText());  // execption if port_no. is invalid
                int port = Integer.parseInt(server_port_input.getText());
                if (port > 1023 && port < 65353) {
                    server = new Server(port);
                    server.start();
                    this.state = true;
                    toggleUIState();
                } else {
                    valid = 3;
                    msg = "Invalid value of port (must be between 1024-65352)";
                    setTextField(msg, valid);
                }

            } catch (ParseException | NumberFormatException e) {
                valid = 1;
                msg = e.getLocalizedMessage() + ": " + "Invalid Input in port Field (port must be integer)";
                setTextField(msg, valid);

            } catch (IOException ex) {
                msg = "Error with Port: " + ex.getLocalizedMessage() + ": Try Another port number";
                valid = 2;
                setTextField(msg, valid);
            } catch (Exception e) {
                msg = "Error: " + e.getLocalizedMessage();
                valid = 3;
                setTextField(msg, valid);

            }
        }
    }

    private void setTextField(String msg, int valid) {
        System.out.println( valid+" : "+msg);
        error_display.setText(msg);
        error_display.setVisible(true);
    }

    private void toggleUIState() {
        error_display.setVisible(false);
        if (this.state) {
            btn_start_server.setText("Stop Server");
            server_port_input.setEditable(!this.state);
        } else {
            btn_start_server.setText("Start Server");
            server_port_input.setEditable(!this.state);
        }
    }

    @FXML
    private void dragged(MouseEvent e) {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setX(e.getScreenX() - x);
        stage.setY(e.getScreenY() - y);
    }

    @FXML
    private void pressed(MouseEvent e) {
        x = e.getSceneX();
        y = e.getSceneY();
    }

    @FXML
    private void close(MouseEvent e) {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void minimize(MouseEvent e) {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML private void do_nothing(){

    }
}
