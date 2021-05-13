package helper;

import javafx.application.Platform;
import javafx.scene.control.Label;
import java.util.TimerTask;

public class TimeCount extends TimerTask {
    private Label timeCounter;
    private int elaspedTime;

    public TimeCount(Label timeCounter, int startTime) {
        this.timeCounter = timeCounter;
        this.elaspedTime = startTime;
    }

    public int getElaspedTime() {
        return elaspedTime;
    }

    @Override
    public void run() {
        elaspedTime +=1;
        Platform.runLater(()->{
            timeCounter.setText(String.format("%02d:%02d", elaspedTime /60, elaspedTime %60));
        });
    }
}
