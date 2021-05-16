package database.models;

import enums.Participant_State;

import java.io.Serializable;
import java.sql.Time;

public class Participants implements Serializable {
    private int participation_id;
    private int match_id;
    private int player_id;
    private boolean hasLeft;
    private Time duration;
    private int rank;
    private int score;
    private Participant_State state;

    public Participants() {
    }


    public Participant_State getState() {
        return state;
    }

    public void setState(Participant_State state) {
        this.state = state;
    }


    public int getParticipation_id() {
        return participation_id;
    }

    public void setParticipation_id(int participation_id) {
        this.participation_id = participation_id;
    }

    public int getMatch_id() {
        return match_id;
    }

    public void setMatch_id(int match_id) {
        this.match_id = match_id;
    }

    public int getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(int player_id) {
        this.player_id = player_id;
    }

    public boolean isHasLeft() {
        return hasLeft;
    }

    public void setHasLeft(boolean hasLeft) {
        this.hasLeft = hasLeft;
    }

    public Time getDuration() {
        return duration;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
