package database.models;

import enums.Message_Type;

import java.io.Serializable;

public class Message implements Serializable {
    private String message;
    private String sender_username;
    private int receiver_id; // in case of match this will be match id
    private Message_Type message_type;

    public Message(String message, String sender_username, int receiver_id, Message_Type message_type) {
        this.message = message;
        this.sender_username = sender_username;
        this.receiver_id = receiver_id;
        this.message_type = message_type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender_username() {
        return sender_username;
    }

    public void setSender_username(String sender_username) {
        this.sender_username = sender_username;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public Message_Type getMessage_type() {
        return message_type;
    }

    public void setMessage_type(Message_Type message_type) {
        this.message_type = message_type;
    }
}
