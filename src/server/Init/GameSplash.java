package server.Init;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import server.Server;

import java.io.IOException;

public class GameSplash extends Application {

    private Server server;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gamesplash.fxml"));
        loader.setController(new Controller(server,false));
        Parent root = loader.load();
        primaryStage.setTitle("Gamesplash Server");
//        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setMinWidth(720);
        primaryStage.setMinHeight(480);
        primaryStage.setOnCloseRequest(windowEvent -> {
//            server.Stop();
        });
    }
}
