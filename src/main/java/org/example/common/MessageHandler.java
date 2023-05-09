package org.example.common;

public class MessageHandler {

    private static MessageHandler instance = null;

    private MessageHandler(){

    }

    public static MessageHandler getInstance(){
        if(instance == null){
            instance = new MessageHandler();
        }
        return instance;
    }

    public void sendMessage(ChatConsumer consumer, String message){
        Logger.getLogger().logging(consumer.side() + " sent to <" + consumer.getName() + "> " + message, Logger.LogLevel.DEBUG, consumer.side());
        consumer.sendMessage(message);
    }
}
