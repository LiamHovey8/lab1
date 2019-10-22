package com.example.androidlabs;

public class Message {
    protected String message;
    protected int sent;
    protected long id;
    public Message(String message, int sent, long id){
        this.sent=sent;
        this.message=message;
        this.id=id;
    }
    public Message(String message,int sent){
        this(message,sent,0);
    }
    public void update(String message,Boolean sent){

    }

    public int getSent() {
        if(sent==1||sent==0)
            return sent;
        else
            return 2;
    }

    public String getMessage() {
        return message;
    }

    public long getId(){ return id;}
}
