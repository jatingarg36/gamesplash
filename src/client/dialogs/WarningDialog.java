package client.dialogs;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
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

public class WarningDialog {

    private String title;
    private String description;
    private double x, y;
    boolean response = false;

    public WarningDialog(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public boolean start() throws Exception {
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
        ButtonBar buttonBar = new ButtonBar();
        Button cancel = new Button("Cancel");
        Button agree = new Button("Yes I Understand");

        cancel.setMinWidth(100);
        agree.setMinWidth(100);

        cancel.setPrefHeight(35);
        agree.setPrefHeight(35);

        cancel.setCursor(Cursor.HAND);
        agree.setCursor(Cursor.HAND);

        agree.setTextFill(Paint.valueOf("white"));
        cancel.setTextFill(Paint.valueOf("white"));

        agree.setFont(Font.font("Raleway SemiBold", 12));
        cancel.setFont(Font.font("Raleway SemiBold", 12));

        agree.setStyle("-fx-background-color:#EF2D2D; -fx-background-radius:10; -fx-border-color:#707070; -fx-border-radius:10;");
        cancel.setStyle("-fx-background-color:#303030; -fx-background-radius:10; -fx-border-color:#707070; -fx-border-radius:10;");

        ButtonBar.setButtonData(cancel, ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonBar.setButtonData(agree, ButtonBar.ButtonData.YES);
        buttonBar.getButtons().addAll(cancel,agree);
        AnchorPane.setRightAnchor(buttonBar, 25.0);
        AnchorPane.setBottomAnchor(buttonBar, 15.0);

        for (Node b : buttonBar.getButtons()){
            b.setOnMouseEntered(e -> {
                b.setStyle("-fx-background-color:#2E2E2E; -fx-background-radius:10");
            });
            b.setOnMouseExited(e -> {
                b.setStyle("-fx-background-color:#303030; -fx-background-radius:10; -fx-border-color:#707070; -fx-border-radius:10;");
            });
        }

        cancel.setOnAction(event -> {
            stage.close();
            response  = false;
        });
        agree.setOnAction(event -> {
            stage.close();
            response = true;
        });
//        cancel.setOnMouseEntered(e -> {
//            cancel.setStyle("-fx-background-color:#2E2E2E; -fx-background-radius:10");
//        });
//        cancel.setOnMouseExited(e -> {
//            cancel.setStyle("-fx-background-color:#303030; -fx-background-radius:10; -fx-border-color:#707070; -fx-border-radius:10;");
//        });
//        agree.setOnMouseEntered(e -> {
//            agree.setStyle("-fx-background-color:#2E2E2E; -fx-background-radius:10");
//        });
//        agree.setOnMouseExited(e -> {
//            agree.setStyle("-fx-background-color:#303030; -fx-background-radius:10; -fx-border-color:#707070; -fx-border-radius:10;");
//        });

        anchorPane.getChildren().addAll(content, buttonBar);

        Scene scene = new Scene(anchorPane);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle(title);
         stage.showAndWait();
         return response;
    }

}
