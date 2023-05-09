package org.example.common;


public abstract class ChatConsumer {
    protected String name;

    public abstract void sendMessage(String message);

    public abstract Side side();

    public String getName(){
        return name;
    }
}
