package client.home.challenge;

import database.ConnectionProvider;
import database.dao.CodeDao;
import database.dao.MatchDao;
import database.dao.ParticipantDao;
import database.models.Code;
import database.models.Matches;
import database.models.Participants;
import database.models.Users;
import enums.Difficulty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Date;

public class ChallengeController {

    enum STATE{
        NONE,
        ONCREATE,
        ONJOIN,
        INMATCH
    }

    private Users users;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public ChallengeController(Users users, Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
        this.users = users;
        this.socket = socket;
        this.ois = ois;
        this.oos = oos;
    }
    private Connection connection;
    private CodeDao codeDao;
    private MatchDao matchDao;
    private ParticipantDao participantDao;


    private STATE state;
    private String code;

    @FXML private HBox head_hbox;
    @FXML private Button create;
    @FXML private Button join;
    @FXML private Button ready;
    @FXML private Button leave;
    @FXML private TextField code_field;
    @FXML private ComboBox<Difficulty> difficulty;
    @FXML private AnchorPane challenge_head_anchor;
    @FXML private Label cancel;

    @FXML
    void initialize(){
        connection = ConnectionProvider.getConnection();
        codeDao = new CodeDao(connection);
        matchDao = new MatchDao(connection);
        participantDao = new ParticipantDao(connection);

        code = null;

        state = STATE.NONE;
        setupInit();
        setupEvents();
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
            state = STATE.INMATCH;
            Code model = result.getValue();
            code = model.getCode();
            modifyToMatchUI();
            try {
                Participants participants = participantDao.participate(model.getMatch_id(),users.getUser_id());
                oos.writeObject(participants);
                oos.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else{
            // invalid code;
        }
    }

    private void createMatch(Difficulty difficulty) {
        int match_id = matchDao.createMatch(users.getUser_id(),difficulty,4,new Timestamp(new Date().getTime()));
        if(match_id!=0){
            state = STATE.INMATCH;
            modifyToMatchUI();
            code = codeDao.generateCode(match_id);
            // write to socket about code generation;
            try {
                Matches matches = matchDao.getMatchDetails(match_id);
                oos.writeObject(matches);
                oos.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            // couldnt create the match
        }
    }

    private void modifyToMatchUI() {

        AnchorPane.clearConstraints(head_hbox);
        AnchorPane.setTopAnchor(head_hbox,10.0);
        AnchorPane.setRightAnchor(head_hbox,20.0);
        AnchorPane.setBottomAnchor(head_hbox,10.0);
        head_hbox.getChildren().removeAll(head_hbox.getChildren());
        head_hbox.getChildren().addAll(ready,leave);
    }

    private void setupInit() {
        head_hbox.getChildren().removeAll(head_hbox.getChildren());
        head_hbox.getChildren().addAll(create,join);

        difficulty.getItems().addAll(Difficulty.EASY,Difficulty.MEDIUM,Difficulty.HARD,Difficulty.EXTREME);
    }


}
