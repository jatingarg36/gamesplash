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
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class DashboardController {
    private final Users user;
    private final Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private OutputHandler outputHandler;
    private InputHandler inputHandler;

    private ArrayList<FXMLLoader> fxmlList;
    private ArrayList<Parent> list;


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
        list = new ArrayList<>();
        for(int i=0;i<8;i++){
            list.add(new AnchorPane());
        }
        fxmlList = new ArrayList<>();

        fxmlList.add(0, new FXMLLoader(getClass().getResource("profile/profile.fxml")));

        fxmlList.add(1, new FXMLLoader(getClass().getResource("practice/practice.fxml")));

        fxmlList.add(2, new FXMLLoader(getClass().getResource("leaderboard/leaderboard.fxml")));

        fxmlList.add(3, new FXMLLoader(getClass().getResource("livegames/livegame.fxml")));

        fxmlList.add(4, new FXMLLoader(getClass().getResource("challenge/challenge.fxml")));

        fxmlList.add(5, new FXMLLoader(getClass().getResource("chatroom/chatroom.fxml")));

        fxmlList.add(6, new FXMLLoader(getClass().getResource("setting/setting.fxml")));

        fxmlList.add(7, new FXMLLoader(getClass().getResource("helpcenter/helpcenter.fxml")));

        System.out.println(fxmlList.size());

        this.activeBtn = challenge_btn;
        activeBtn.setStyle("-fx-background-color:#131314 ; -fx-background-radius:0; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 0);");

        challengeController = new ChallengeController(user, socket, ois, oos);
        fxmlList.get(4).setController(challengeController);
        inputHandler.setChallengeController(challengeController);
        try {
            list.set(4,fxmlList.get(4).load());
            contentSection.getChildren().clear();
            AnchorPane parent = (AnchorPane) list.get(4);
            AnchorPane.setBottomAnchor(parent, 0.0);
            AnchorPane.setLeftAnchor(parent, 0.0);
            AnchorPane.setRightAnchor(parent, 0.0);
            AnchorPane.setTopAnchor(parent, 0.0);
            contentSection.getChildren().add(parent);
            System.out.println(contentSection.getChildren());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void selectOption(MouseEvent e) throws IOException {
        activeBtn.setStyle("-fx-background-color: transparent; -fx-background-radius:0");
        Button selected = (Button) e.getSource();
        FXMLLoader loader = null;
        int i = 0;
        if (profile_btn.equals(selected)) {
            i = 0;
            this.activeBtn = profile_btn;
            if (fxmlList.get(i).getController() == null) {
                fxmlList.get(i).setController(new ProfileController(user, socket));
                list.set(i,fxmlList.get(i).load());
                
            }
        } else if (practice_btn.equals(selected)) {
            i = 1;
            this.activeBtn = practice_btn;
            if (fxmlList.get(i).getController() == null) {
                fxmlList.get(i).setController( new PracticeController(user, socket));
                list.set(i,fxmlList.get(i).load());
            }
        } else if (leaderboard_btn.equals(selected)) {
            i = 2;
            this.activeBtn = leaderboard_btn;
            if (fxmlList.get(i).getController() == null) {
                fxmlList.get(i).setController(new LeaderboardController());
                list.set(i,fxmlList.get(i).load());
            }
        } else if (live_btn.equals(selected)) {
            i = 3;
            this.activeBtn = live_btn;
            if (fxmlList.get(i).getController() == null) {
                fxmlList.get(i).setController(new ProfileController(user, socket));
                list.set(i,fxmlList.get(i).load());
            }
        } else if (challenge_btn.equals(selected)) {
            i = 4;
            this.activeBtn = challenge_btn;
            if (fxmlList.get(i).getController() == null) {
                fxmlList.get(i).setController(new ChallengeController(user, socket, ois, oos));
                list.set(i,fxmlList.get(i).load());
            }
        } else if (chatroom_btn.equals(selected)) {
            i = 5;
            this.activeBtn = chatroom_btn;
            if (fxmlList.get(i).getController() == null) {
                fxmlList.get(i).setController(new ChatroomController());
                list.set(i,fxmlList.get(i).load());
            }
        } else if (setting_btn.equals(selected)) {
            i = 6;
            this.activeBtn = setting_btn;
            if (fxmlList.get(i).getController() == null) {
                fxmlList.get(i).setController(new SettingController());
                list.set(i,fxmlList.get(i).load());
            }
        } else if (helpcenter_btn.equals(selected)) {
            i = 7;
            this.activeBtn = helpcenter_btn;
            if (fxmlList.get(i).getController() == null) {
                fxmlList.get(i).setController(new HelpcenterController());
                list.set(i,fxmlList.get(i).load());
            }
        }

        if (fxmlList.get(i) != null) {
            contentSection.getChildren().clear();

            AnchorPane parent = (AnchorPane) list.get(i);
            AnchorPane.setBottomAnchor(parent, 0.0);
            AnchorPane.setLeftAnchor(parent, 0.0);
            AnchorPane.setRightAnchor(parent, 0.0);
            AnchorPane.setTopAnchor(parent, 0.0);
            contentSection.getChildren().add(parent);
            System.out.println(contentSection.getChildren());
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
