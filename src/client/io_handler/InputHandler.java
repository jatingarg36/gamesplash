package client.io_handler;

import client.home.challenge.ChallengeController;
import client.home.chatroom.ChatroomController;
import client.home.helpcenter.HelpcenterController;
import client.home.leaderboard.LeaderboardController;
import client.home.livegames.LiveGameController;
import client.home.practice.PracticeController;
import client.home.profile.ProfileController;
import client.home.setting.SettingController;
import database.models.Message;
import database.models.SystemNotification;
import database.models.Users;
import enums.Message_Type;
import enums.NotificationType;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;

public class InputHandler extends Thread{

    private final ObjectInputStream objectInputStream;
    private Object object;

    /* controllers */
    private ProfileController profileController;
    private ChallengeController challengeController;
    private ChatroomController chatroomController;
    private LeaderboardController leaderboardController;
    private LiveGameController liveGameController;
    private PracticeController practiceController;
    private HelpcenterController helpcenterController;
    private SettingController settingController;


    public void setProfileController(ProfileController profileController) {
        this.profileController = profileController;
    }

    public void setChallengeController(ChallengeController challengeController) {
        this.challengeController = challengeController;
    }

    public void setChatroomController(ChatroomController chatroomController) {
        this.chatroomController = chatroomController;
    }

    public void setLeaderboardController(LeaderboardController leaderboardController) {
        this.leaderboardController = leaderboardController;
    }

    public void setLiveGameController(LiveGameController liveGameController) {
        this.liveGameController = liveGameController;
    }

    public void setPracticeController(PracticeController practiceController) {
        this.practiceController = practiceController;
    }

    public void setHelpcenterController(HelpcenterController helpcenterController) {
        this.helpcenterController = helpcenterController;
    }

    public void setSettingController(SettingController settingController) {
        this.settingController = settingController;
    }

    public InputHandler(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
    }

    @Override
    public void run() {
        while(true){
            try{
                Thread.sleep(2000);
                System.out.println("working");
                object = objectInputStream.readObject();

                if(object instanceof Users){
                    System.out.println(((Users) object).getUsername());
                }else if(object instanceof SystemNotification) {
                    SystemNotification systemNotification = (SystemNotification) object;
                    if(systemNotification.getNotificationType() == NotificationType.START_MATCH){
                        Platform.runLater(() -> challengeController.startMatch());
                    }
                    if(systemNotification.getNotificationType() == NotificationType.PLAYER_JOINED){
                        Platform.runLater(()->challengeController.playerJoined(systemNotification.getNotification()));
                    }
                }
                else if(object instanceof Message){
                    Message message = (Message) object;
                    if(message.getMessage_type() == Message_Type.FOR_MATCH) {
                        Platform.runLater(() -> challengeController.setMessage(message.getSender_username(),message.getMessage()));
                    }
                }
                else {
                    System.out.println("nothing");
                }

            } catch (IOException | ClassNotFoundException | InterruptedException e) {
//                e.printStackTrace();
                break;
            }
        }
    }
}
