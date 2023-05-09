package org.example.client;


import org.example.common.Logger;
import org.example.common.MessageHandler;

import java.util.Scanner;

public class ChatHandler {
    private final Scanner scanner = new Scanner(System.in);
    private final ChatServer server;

    public ChatHandler(ChatServer chatServer) {
        this.server = chatServer;
        new Thread(Client.threadGroup, this::messageReceiver).start();
        messageSender();
    }

    private void messageSender() {
        Logger.getLogger().logging("You successfully connected", Logger.LogLevel.INFO, Client.side);
        while (true) {
            String message;
            if(Client.isTest){
                if(Client.testMessages.peek() == null){
                    continue;
                }else {
                    message = Client.testMessages.poll();
                }
            }else {
                message = scanner.nextLine();
            }

            MessageHandler.getInstance().sendMessage(server, message);

            if (message.equals("/exit")) {
                return;
            }
        }
    }

    public void messageReceiver() {
        Logger.getLogger().logging("Message receiving thread started", Logger.LogLevel.DEBUG, Client.side);
        while (Client.online) {

            if (server.hasMessage()) {
                String message = server.getMessage();
                Logger.getLogger().logging("Received message: " + message, Logger.LogLevel.DEBUG, Client.side);
                System.out.println(message);
            }

        }

    }
}
