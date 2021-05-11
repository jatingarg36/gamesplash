package client.home.profile;

import client.home.DashboardController;
import database.models.Users;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.net.Socket;

public class ProfileController{
    private final Users user;
    private final Socket socket;

    public ProfileController(Users user, Socket socket) {
        this.user = user;
        this.socket = socket;
    }
    @FXML
    private Label username;

    @FXML
    private Label badge_icon;

    @FXML
    private Label badge_title;

    @FXML
    private Circle image_holder;

    @FXML
    private Label score;

    @FXML
    private Label rank;

    @FXML
    private Label total_matches;

    @FXML
    private Label matches_won;

    @FXML
    private Label practice_count;

    @FXML
    void initialize(){
        username.setText("Hi, "+ user.getUsername());
        badge_title.setText(user.getBadge());
        score.setText(String.valueOf(user.getScore()));
        rank.setText(String.valueOf(user.getRank()));
    }
}
