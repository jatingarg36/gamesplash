package client.dialogs;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PromptDialog {

    private String title;
    private String description;
    private double x, y;


    public PromptDialog(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void start() throws Exception {
        Stage stage = new Stage();
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefWidth(360);
        anchorPane.setPrefHeight(180);
        anchorPane.setStyle("-fx-background-color:#171717;-fx-background-radius:0 ; -fx-border-radius:0; -fx-background-insets:10");
        anchorPane.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.valueOf("#131313"), 10, 0.2, 0, 0));

        anchorPane.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        anchorPane.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });

        // label
        Label content = new Label(description);
        content.setMaxWidth(300);
        content.setWrapText(true);
        content.setStyle("-fx-font-family:Raleway Bold; -fx-font-size:15;-fx-text-fill:white; ");
        AnchorPane.setTopAnchor(content, 25.0);
        AnchorPane.setLeftAnchor(content, 25.0);

        // button
        Button respond = new Button("OK");
        respond.setPrefWidth(100);
        respond.setPrefHeight(35);
        respond.setCursor(Cursor.HAND);
        respond.setTextFill(Paint.valueOf("white"));
        respond.setFont(Font.font("Raleway SemiBold", 12));
        respond.setStyle("-fx-background-color:#303030; -fx-background-radius:10; -fx-border-color:#707070; -fx-border-radius:10;");
        AnchorPane.setRightAnchor(respond, 25.0);
        AnchorPane.setBottomAnchor(respond, 15.0);

        respond.setOnAction(event -> {
            stage.close();
        });
        respond.setOnMouseEntered(e -> {
            respond.setStyle("-fx-background-color:#2E2E2E; -fx-background-radius:10");
        });
        respond.setOnMouseExited(e -> {
            respond.setStyle("-fx-background-color:#303030; -fx-background-radius:10; -fx-border-color:#707070; -fx-border-radius:10;");
        });

        anchorPane.getChildren().addAll(content, respond);

        Scene scene = new Scene(anchorPane);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle(title);
        stage.showAndWait();
    }
}
