package helper;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.TimerTask;

public class ScoreHandler extends TimerTask {

    private Label score_label;
    private int maxScore;

    public ScoreHandler(Label score_label, int maxScore) {
        this.score_label = score_label;
        this.maxScore = maxScore;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void deductScore(int penalty) {
        this.maxScore -= penalty;
        if(maxScore>0){
            maxScore = Math.max(0,maxScore-penalty);
            Platform.runLater(()->score_label.setText(maxScore+" pts"));
        }
    }

    @Override
    public void run() {
        if(maxScore>0) {
            maxScore =Math.max(0,maxScore-5);
            Platform.runLater(() -> score_label.setText(maxScore + " pts"));
        }
    }

}
