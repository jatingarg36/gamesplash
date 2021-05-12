package client.home;

import database.ConnectionProvider;
import database.dao.UserDao;
import database.models.Users;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;

public class DashBoard extends Application {

    private final Users currentUser;
    private final Socket currentSocket;

    public DashBoard(Users currentUser, Socket currentSocket) {
        this.currentUser = currentUser;
        this.currentSocket = currentSocket;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
        fxmlLoader.setController(new DashboardController(this.currentUser,this.currentSocket));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Dashboard");
//        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            try {
                stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void stop() throws IOException {
        //close socket
        System.out.println("logging out");
        Connection con = ConnectionProvider.getConnection();
        UserDao userDao = new UserDao(con);
        userDao.logout(currentUser);
        currentSocket.close();
        Platform.exit();
        System.exit(0);
    }
}
