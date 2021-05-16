package client.home.challenge;

import database.ConnectionProvider;
import database.dao.CodeDao;
import database.dao.MatchDao;
import database.dao.ParticipantDao;
import database.models.*;
import enums.Difficulty;
import enums.Message_Type;
import enums.Participant_State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class ChallengeController {



    enum STATE{
        NONE,
        ONCREATE,
        ONJOIN,
        NOT_READY,
        READY,
        INMATCH,
        SUBMITTED
    }

    private final Users users;
    private final Socket socket;
    private final ObjectInputStream ois;
    private final ObjectOutputStream oos;

    private Connection connection;
    private CodeDao codeDao;
    private MatchDao matchDao;
    private ParticipantDao participantDao;

    private Participants participants;

    private STATE state;
    private String code;
    private ConcurrentHashMap<String,AnchorPane> playersList;

    public ChallengeController(Users users, Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
        this.users = users;
        this.socket = socket;
        this.ois = ois;
        this.oos = oos;
        this.state = STATE.NONE;
    }



    @FXML private VBox challenge_container;
    @FXML private HBox head_hbox;
    @FXML private VBox head_vbox;
    @FXML private Button create;
    @FXML private Button join;
    @FXML private Button ready;
    @FXML private Button leave;
    @FXML private Button submit;
    @FXML private TextField code_field;
    @FXML private TextField codeText;
    @FXML private ComboBox<Difficulty> difficulty;
    @FXML private AnchorPane challenge_head_anchor;
    @FXML private AnchorPane challenge_body_anchor;
    @FXML private Label cancel;
    @FXML private VBox playerJoined_holder;
    @FXML private VBox message_holder;
    @FXML private AnchorPane playerJoind_card;
    @FXML private TextField msg_field;


    @FXML
    void initialize(){
        if(state!=STATE.INMATCH && state!=STATE.READY && state!=STATE.SUBMITTED){
            connection = ConnectionProvider.getConnection();
            codeDao = new CodeDao(connection);
            matchDao = new MatchDao(connection);
            participantDao = new ParticipantDao(connection);

            code = null;

            state = STATE.NONE;
            setupInit();
            setupEvents();
        }
        else{
            modifyToMatchUI();
        }
    }

    @FXML void btn_clicked(MouseEvent event) {

    }

    @FXML void btn_pressed(MouseEvent event) {
        Button btn = (Button) event.getSource();
        btn.setStyle("-fx-background-color: white;-fx-text-fill:#707070; -fx-border-radius:3;-fx-border-color: #707070; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 0);");
    }

    @FXML void btn_released(MouseEvent event) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Button btn = (Button) event.getSource();
        btn.setStyle("-fx-background-color: transparent; -fx-border-radius:3;-fx-border-color: #707070");
    }

    @FXML void hover_exit_on_btn(MouseEvent event) {
        Button button = (Button) event.getSource();

        button.setStyle("-fx-background-color: transparent; -fx-border-radius:3;-fx-border-color: #707070");

    }

    @FXML void hover_on_btn(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color:#2E2E2E; -fx-border-radius:3;-fx-background-radius:3");

    }

    @FXML void key_typed(KeyEvent event) {

    }

    @FXML void selectField(MouseEvent event) {

    }

    @FXML void submit_action(ActionEvent event){
        state = STATE.SUBMITTED;
        System.out.println("Match Submitted");
    }

    @FXML void ready_action(ActionEvent event){
        Button btn = (Button) event.getSource();

        try{
            if(participantDao.setState(participants.getParticipation_id(), Participant_State.READY)){
                this.participants.setState(Participant_State.READY);
                System.out.println(this.participants.getState());
                oos.flush();
                oos.reset();
                oos.writeObject(this.participants);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        state = STATE.READY;
        btn.setText("Waiting...");
        btn.setStyle("-fx-background-color: #5D5E60 ; -fx-background-radius:0");


    }
    @FXML void leave_challenge(ActionEvent event){
        state = STATE.NONE;
        //doLeaveFormalities();
        initialize();
    }

    @FXML void send_message(ActionEvent event){
        if(msg_field.getText()!=null){


            try {
                String msg = msg_field.getText();
                msg_field.setText("");
                msg_field.setPromptText("Type a message ...");

                // send message to socket adding match details;
                Message message = new Message(msg,users.getUsername(),participants.getMatch_id(),Message_Type.FOR_MATCH);
                oos.reset();
                oos.flush();
                oos.writeObject(message);
                setMessage(users.getUsername(),msg);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }else{
            //do nothing
        }
    }

    // when the match has been started
    private void modifyInMatchUI(){
        head_hbox.getChildren().remove(ready);
        head_hbox.getChildren().add(submit);

    }

    private void setupEvents() {
        create.setOnAction(event -> {
            if(state!=STATE.ONCREATE){
                head_hbox.getChildren().removeAll(head_hbox.getChildren());
                head_hbox.getChildren().addAll(difficulty,create,cancel);

                state = STATE.ONCREATE;
            }
            else{
                if(difficulty.getValue()!=null){
                    createMatch(difficulty.getValue());
                }
            }
        });

        join.setOnAction(event -> {
            if(state!=STATE.ONJOIN){
                head_hbox.getChildren().removeAll(head_hbox.getChildren());
                head_hbox.getChildren().addAll(code_field,join,cancel);
                state = STATE.ONJOIN;
            }
            else{
                if(code_field.getText()!=null) {
                    joinMatch(code_field.getText());
                }
            }
        });
        cancel.setOnMouseClicked(event -> {
            head_hbox.getChildren().removeAll(head_hbox.getChildren());
            head_hbox.getChildren().addAll(create,join);
            state = STATE.NONE;
        });
    }

    private void joinMatch(String input_code) {
        Pair<Boolean, Code> result = codeDao.validateCode(input_code);
        if(result.getKey()){
            state = STATE.NOT_READY;
            Code model = result.getValue();
            code = model.getCode();
            try {
                participants = participantDao.participate(model.getMatch_id(),users.getUser_id());
                oos.writeObject(participants);
                oos.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
            modifyToMatchUI();
        }
        else{
            // invalid code;
        }
    }

    private void createMatch(Difficulty difficulty) {
        int match_id = matchDao.createMatch(users.getUser_id(),difficulty,4,new Timestamp(new Date().getTime()));
        if(match_id!=0){
            state = STATE.NOT_READY;
            code = codeDao.generateCode(match_id);
            // write to socket about code generation;
            try {
                Matches matches = matchDao.getMatchDetails(match_id);
                oos.flush();
                oos.reset();
                oos.writeObject(matches);

                participants = participantDao.participate(match_id,users.getUser_id());
                oos.flush();
                oos.reset();
                oos.writeObject(participants);

            } catch (IOException e) {
                e.printStackTrace();
            }
            modifyToMatchUI();
        }
        else {
            // couldnt create the match
        }
    }


    public void startMatch() {
        // start Timer;
        modifyInMatchUI();
    }

    private AnchorPane playerJoinedCard(String username,Participant_State state){
        AnchorPane pane = new AnchorPane();
        pane.setStyle("-fx-background-color: #3B3B3B ; -fx-background-radius:5");
        pane.setPadding(new Insets(5));
        Label name = new Label(username);
        name.setFont(new Font("Raleway SemiBold",20));
        name.setTextFill(Color.WHITE);
        AnchorPane.setRightAnchor(name,80.0);
        AnchorPane.setLeftAnchor(name,20.0);
        AnchorPane.setTopAnchor(name,0.0);
        AnchorPane.setBottomAnchor(name,0.0);

        Label st = new Label(state.toString().toLowerCase());
        st.setFont(new Font("Raleway ExtraBold",16));
        st.setTextFill(Color.WHITE);
        AnchorPane.setRightAnchor(st,10.0);
        AnchorPane.setTopAnchor(st,0.0);
        AnchorPane.setBottomAnchor(st,0.0);

        pane.getChildren().addAll(name,st);

        return pane;
    }

    public void playerJoined(String username) {
        AnchorPane pane;
        if(!username.equals(users.getUsername()))
            pane = playerJoinedCard(username,Participant_State.NOT_READY);
        else
            pane = playerJoinedCard("You",Participant_State.NOT_READY);
        playersList.put(username,pane);
        playerJoined_holder.getChildren().add(pane);
    }


    private AnchorPane playerMsgCard(String username,String message){
        AnchorPane pane = new AnchorPane();
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(5));
        vBox.setSpacing(10);
        vBox.setFillWidth(true);
        Label sender,msg;
        if(username.equals(users.getUsername())){
            sender = new Label("you");
            vBox.setAlignment(Pos.CENTER_RIGHT);
            AnchorPane.setRightAnchor(vBox,10.0);
        }
        else{
            sender = new Label(username.toLowerCase());
            vBox.setAlignment(Pos.CENTER_LEFT);
            AnchorPane.setLeftAnchor(vBox,10.0);
        }
        msg = new Label(message);
        sender.setTextFill(Color.WHITE);
        sender.setPadding(new Insets(0,10,0,10));
        sender.setFont(new Font("Raleway ExtraBold",14));

        msg.setTextFill(Color.WHITE);
        msg.setPadding(new Insets(10));
        msg.setFont(new Font("Raleway SemiBold",13));
        msg.setWrapText(true);
        msg.setMaxWidth(300);
        msg.setStyle("-fx-background-color: #3B3B3B; -fx-background-radius:5");

        vBox.getChildren().addAll(sender,msg);
        pane.getChildren().add(vBox);
        return pane;
    }

    public void setMessage(String username,String message){
        message_holder.getChildren().add(playerMsgCard(username,message));
    }

    // when the match is created and players are joining (may or may not be in ready state)
    private void modifyToMatchUI() {
        playersList = new ConcurrentHashMap<>();
        challenge_container.getChildren().clear();
        challenge_container.getChildren().addAll(challenge_head_anchor,challenge_body_anchor);
        AnchorPane.clearConstraints(head_hbox);
        AnchorPane.setTopAnchor(head_hbox,10.0);
        AnchorPane.setRightAnchor(head_hbox,20.0);
        AnchorPane.setBottomAnchor(head_hbox,10.0);
        head_hbox.getChildren().removeAll(head_hbox.getChildren());
        head_hbox.getChildren().addAll(ready,leave);
        if(code!=null) {
            codeText.setText(code);
            head_vbox.setVisible(true);
        }
        else{
            Label  text = (Label) head_vbox.getChildren().get(0);
            text.setText("Unable to create code\nLeave the match and try again");
            text.setStyle("-fx-text-fill:red");
            head_vbox.getChildren().get(1).setVisible(false);
            head_vbox.setVisible(true);
        }
    }

    private void setupInit() {
        challenge_container.getChildren().clear();
        challenge_container.getChildren().add(challenge_head_anchor);
        AnchorPane.setBottomAnchor(head_hbox,80.0);
        AnchorPane.setTopAnchor(head_hbox,100.0);
        AnchorPane.setRightAnchor(head_hbox,0.0);
        AnchorPane.setLeftAnchor(head_hbox,0.0);
        head_hbox.getChildren().removeAll(head_hbox.getChildren());
        head_hbox.getChildren().addAll(create,join);
        head_vbox.setVisible(false);
        difficulty.getItems().clear();
        difficulty.setPromptText("Choose Difficulty");
        difficulty.getItems().addAll(Difficulty.EASY,Difficulty.MEDIUM,Difficulty.HARD,Difficulty.EXTREME);
    }


}
