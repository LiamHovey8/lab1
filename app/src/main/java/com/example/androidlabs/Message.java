package com.example.androidlabs;

public class Message {
    protected String message;
    protected Boolean sent;
    public Message(String message, Boolean sent){
        this.sent=sent;
        this.message=message;
    }

    public Boolean getSent() {
        return sent;
    }

    public String getMessage() {
        return message;
    }
}
