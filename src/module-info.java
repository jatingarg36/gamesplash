module gamesplash {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;

    opens server.Init;
    opens client.auth;
    opens client.home;
    opens client.home.profile;
    opens client.home.practice;
    opens client.home.helpcenter;
    opens client.home.setting;
    opens client.home.chatroom;
    opens client.home.challenge;
    opens client.home.livegames;
    opens client.home.leaderboard;
    opens client.dialogs;
}