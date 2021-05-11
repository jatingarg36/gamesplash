package database.models;

import java.io.Serializable;
import java.sql.Timestamp;

public class Users implements Serializable {
    private String username, password, salt;
    private int user_id;
    private Timestamp joined_at;
    private boolean isActive;
    private String status;
    //    private enum status{available,busy,in_game,away}; // enum('available','busy','in game','away')
    private int score;
    private int rank;
    private String badge;
    //    private enum badge{freshman,beginner,intermediate,advanced,expert,guru,headmaster};// enum('freshman','beginner','intermediate','advanced','expert','guru','headmaster')
    private Timestamp last_visited;

    public Users() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Timestamp getJoined_at() {
        return joined_at;
    }

    public void setJoined_at(Timestamp joined_at) {
        this.joined_at = joined_at;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public Timestamp getLast_visited() {
        return last_visited;
    }

    public void setLast_visited(Timestamp last_visited) {
        this.last_visited = last_visited;
    }

    public Users(String username, String password, String salt, int user_id, Timestamp joined_at, boolean isActive, String status, int score, int rank, String badge, Timestamp last_visited) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.user_id = user_id;
        this.joined_at = joined_at;
        this.isActive = isActive;
        this.status = status;
        this.score = score;
        this.rank = rank;
        this.badge = badge;
        this.last_visited = last_visited;
    }
}
