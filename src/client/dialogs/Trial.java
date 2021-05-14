package client.dialogs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Trial extends Application{
    public static void main(String[] args) throws Exception {
        launch(args);

    }
    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = new Pane();
        Button button = new Button("Click Me");
        button.setOnAction(event -> {
            PromptDialog promptDialog = new PromptDialog("some","other");
            try {
                promptDialog.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        pane.getChildren().add(button);
        stage.setScene(new Scene(pane));
        stage.show();
    }
}

