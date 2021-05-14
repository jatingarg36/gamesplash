package database.models;

import enums.Difficulty;
import enums.Match_Status;

import java.io.Serializable;
import java.sql.Timestamp;

public class Matches implements Serializable {
    private int match_id;
    private int player_started;
    private Difficulty difficulty;
    private int winner;
    private boolean isLive,canJoin;
    private Match_Status match_status;
    private int max_participation;
    private Timestamp created_at;
    private Timestamp start_time;
    private Timestamp end_time;

    public Matches(){
    }

    public int getMatch_id() {
        return match_id;
    }

    public void setMatch_id(int match_id) {
        this.match_id = match_id;
    }

    public int getPlayer_started() {
        return player_started;
    }

    public void setPlayer_started(int player_started) {
        this.player_started = player_started;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public boolean isCanJoin() {
        return canJoin;
    }

    public void setCanJoin(boolean canJoin) {
        this.canJoin = canJoin;
    }

    public Match_Status getMatch_status() {
        return match_status;
    }

    public void setMatch_status(Match_Status match_status) {
        this.match_status = match_status;
    }

    public int getMax_participation() {
        return max_participation;
    }

    public void setMax_participation(int max_participation) {
        this.max_participation = max_participation;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
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
}
