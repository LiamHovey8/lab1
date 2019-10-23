package com.example.androidlabs;

public class Message {
    protected String message;
    protected boolean sent;
    protected long id;
    public Message(String message, boolean sent, long id){
        this.sent=sent;
        this.message=message;
        this.id=id;
    }
    public Message(String message,boolean sent){
        this(message,sent,0);
    }
    public void update(String message,Boolean sent){

    }

    public boolean getSent() {
        return sent;
    }

    public String getMessage() {
        return message;
    }

    public long getId(){ return id;}
}
