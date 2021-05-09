package database.models;

import java.io.Serializable;
import java.sql.Timestamp;

public class Users implements Serializable{
    private String username,password,salt;
    private int id;
    private Timestamp joined_at;

    public Users() {
    }

    public Users(String username, String password, String salt, int id, Timestamp joined_at) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.id = id;
        this.joined_at = joined_at;
    }

    public Users(String username, String password, String salt, Timestamp joined_at) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.joined_at = joined_at;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSalt() {
        return salt;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getJoined_at() {
        return joined_at;
    }

    public void setJoined_at(Timestamp joined_at) {
        this.joined_at = joined_at;
    }
    
    
}
