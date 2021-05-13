package client.home;

import client.auth.AuthScreen;
import client.home.challenge.ChallengeController;
import client.home.chatroom.ChatroomController;
import client.home.helpcenter.HelpcenterController;
import client.home.leaderboard.LeaderboardController;
import client.home.livegames.LiveGameController;
import client.home.practice.PracticeController;
import client.home.profile.ProfileController;
import client.home.setting.SettingController;
import client.io_handler.InputHandler;
import client.io_handler.OutputHandler;
import database.ConnectionProvider;
import database.dao.UserDao;
import database.models.Users;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;

public class DashboardController {
    private final Users user;
    private final Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private OutputHandler outputHandler;
    private InputHandler inputHandler;

    public DashboardController(Users user, Socket socket) {
        this.user = user;
        this.socket = socket;
        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());

            inputHandler = new InputHandler(ois);
            inputHandler.start();
//            System.out.println("hel1");
            oos.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Button activeBtn;

    /* controllers */
    private ProfileController profileController;
    private ChallengeController challengeController;
    private ChatroomController chatroomController;
    private LeaderboardController leaderboardController;
    private LiveGameController liveGameController;
    private PracticeController practiceController;
    private HelpcenterController helpcenterController;
    private SettingController settingController;


    @FXML
    private Button profile_btn;

    @FXML
    private Button leaderboard_btn;

    @FXML
    private Button live_btn;

    @FXML
    private Button challenge_btn;

    @FXML
    private Button practice_btn;

    @FXML
    private Button chatroom_btn;

    @FXML
    private Button helpcenter_btn;

    @FXML
    private Button setting_btn;

    @FXML
    private Button signout_btn;

    @FXML
    private AnchorPane contentSection;


    @FXML
    void initialize() {
        this.activeBtn = profile_btn;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("profile/profile.fxml"));
        if (profileController == null)
            profileController = new ProfileController(user,socket);
        loader.setController(profileController);
        try {
            contentSection.getChildren().clear();
            AnchorPane parent = loader.load();
            AnchorPane.setBottomAnchor(parent,0.0);
            AnchorPane.setLeftAnchor(parent,0.0);
            AnchorPane.setRightAnchor(parent,0.0);
            AnchorPane.setTopAnchor(parent,0.0);
            contentSection.getChildren().add(parent);
            System.out.println(contentSection.getChildren().stream().count());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void selectOption(MouseEvent e) throws IOException {
        activeBtn.setStyle("-fx-background-color: transparent; -fx-background-radius:0");
        Button selected = (Button) e.getSource();
        FXMLLoader loader = null;
        if (profile_btn.equals(selected)) {
            this.activeBtn = profile_btn;
            loader = new FXMLLoader(getClass().getResource("profile/profile.fxml"));
            if (profileController == null)
                profileController = new ProfileController(user,socket);
            loader.setController(profileController);
        } else if (practice_btn.equals(selected)) {
            this.activeBtn = practice_btn;
            loader = new FXMLLoader(getClass().getResource("practice/practice.fxml"));
            if (practiceController == null)
                practiceController = new PracticeController(user,socket);
            loader.setController(practiceController);
        } else if (leaderboard_btn.equals(selected)) {
            this.activeBtn = leaderboard_btn;
            loader = new FXMLLoader(getClass().getResource("leaderboard/leaderboard.fxml"));
            if (practiceController == null)
                leaderboardController = new LeaderboardController();
            loader.setController(leaderboardController);
        } else if (live_btn.equals(selected)) {
            this.activeBtn = live_btn;
            loader = new FXMLLoader(getClass().getResource("livegames/livegame.fxml"));
            if (liveGameController == null)
                liveGameController = new LiveGameController();
            loader.setController(liveGameController);
        } else if (challenge_btn.equals(selected)) {
            this.activeBtn = challenge_btn;
            loader = new FXMLLoader(getClass().getResource("challenge/challenge.fxml"));
            if (challengeController == null)
                challengeController = new ChallengeController();
            loader.setController(challengeController);
        } else if (chatroom_btn.equals(selected)) {
            this.activeBtn = chatroom_btn;
            loader = new FXMLLoader(getClass().getResource("chatroom/chatroom.fxml"));
            if (chatroomController == null)
                chatroomController = new ChatroomController();
            loader.setController(chatroomController);
        } else if (setting_btn.equals(selected)) {
            this.activeBtn = setting_btn;
            loader = new FXMLLoader(getClass().getResource("setting/setting.fxml"));
            if (settingController == null)
                settingController = new SettingController();
            loader.setController(settingController);
        } else if (helpcenter_btn.equals(selected)) {
            this.activeBtn = helpcenter_btn;
            loader = new FXMLLoader(getClass().getResource("helpcenter/helpcenter.fxml"));
            if (helpcenterController == null)
                helpcenterController = new HelpcenterController();
            loader.setController(helpcenterController);
        }

        if (loader != null) {
            contentSection.getChildren().clear();
            AnchorPane parent = loader.load();
            AnchorPane.setBottomAnchor(parent,0.0);
            AnchorPane.setLeftAnchor(parent,0.0);
            AnchorPane.setRightAnchor(parent,0.0);
            AnchorPane.setTopAnchor(parent,0.0);
            contentSection.getChildren().add(parent);
            System.out.println(contentSection.getChildren().stream().count());
        }
        activeBtn.setStyle("-fx-background-color:#131314 ; -fx-background-radius:0; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 0);");
    }

    @FXML
    void hoverEnter(MouseEvent e) {
        if (!activeBtn.equals(e.getSource()))
            ((Button) e.getSource()).setStyle("-fx-background-color:#4A4949 ; -fx-background-radius:0");
    }

    @FXML
    void hoverExit(MouseEvent e) {
        if (!activeBtn.equals(e.getSource())) {
            ((Button) e.getSource()).setStyle("-fx-background-color: transparent; -fx-background-radius:0");
        }
    }

    @FXML
    void signout(ActionEvent e) throws IOException {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
        Platform.exit();
//        completePendingQueries();
        Connection con = ConnectionProvider.getConnection();
        UserDao userDao = new UserDao(con);
        userDao.logout(user);
        socket.close();
        new AuthScreen().start(new Stage());
    }




}
