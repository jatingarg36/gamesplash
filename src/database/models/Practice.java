package database.models;

import java.io.Serializable;
import java.sql.Timestamp;

public class Practice implements Serializable {
    private int practice_id;
    private int player_id;
    private Timestamp start_time;
    private Timestamp end_time;
    private int score;
    private String difficulty;

    public Practice() {
    }

    public Practice(int practice_id, int player_id, Timestamp start_time, Timestamp end_time, int score, String difficulty) {
        this.practice_id = practice_id;
        this.player_id = player_id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.score = score;
        this.difficulty = difficulty;
    }

    public Practice(int practice_id, Timestamp start_time, Timestamp end_time, int score, String difficulty) {
        this.practice_id = practice_id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.score = score;
        this.difficulty = difficulty;
    }

    public int getPractice_id() {
        return practice_id;
    }

    public void setPractice_id(int practice_id) {
        this.practice_id = practice_id;
    }

    public int getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(int player_id) {
        this.player_id = player_id;
    }

    public Timestamp getStart_time() {
        return start_time;
    }

    public void setStart_time(Timestamp start_time) {
        this.start_time = start_time;
    }

    public Timestamp getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Timestamp end_time) {
        this.end_time = end_time;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

}
