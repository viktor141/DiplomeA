package org.example.server;

import org.example.common.Logger;
import org.example.common.MessageHandler;

import java.util.concurrent.ArrayBlockingQueue;

import static org.example.server.ConnectThread.clients;

public class ChatThread implements Runnable {

    public static ArrayBlockingQueue<String> messageQueue = new ArrayBlockingQueue<>(100);

    @Override
    public void run() {
        Logger.getLogger().logging("ChatThread started ", Logger.LogLevel.DEBUG, Server.getInstance().side);
        while (Server.getInstance().online) {

            if (!messageQueue.isEmpty()) {
                String message = messageQueue.poll();
                Logger.getLogger().logging("[ChatMessage] > " + message, Logger.LogLevel.INFO, Server.getInstance().side);
                for (ChatClient client : clients) {
                    MessageHandler.getInstance().sendMessage(client, message);
                }
            }

        }
        Logger.getLogger().logging("ChatThread stopped ", Logger.LogLevel.DEBUG, Server.getInstance().side);
    }
}
