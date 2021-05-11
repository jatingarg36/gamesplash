package client.auth;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class AuthScreen extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("authscreen.fxml"));
        primaryStage.setTitle("Gamesplash");
//        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
