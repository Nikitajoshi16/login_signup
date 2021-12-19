package com.example.login_signup;

public class chatsclass {

    private String sender;
    private String message;
    private String receiver;
    private String type;

    public chatsclass() {
    }

    public chatsclass(String sender, String message, String receiver, String id) {
        this.sender = sender;
        this.message = message;
        this.receiver = receiver;
        this.type= id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getType() {
        return type;
    }

    public void setType(String imageuri) {
        this.type = imageuri;
    }
}
