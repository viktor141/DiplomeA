package org.example.server;

import org.example.common.Logger;

import static org.example.server.ConnectThread.clients;

public class HandleThread implements Runnable {


    @Override
    public void run() {
        Logger.getLogger().logging("HandleThread started ", Logger.LogLevel.DEBUG, Server.getInstance().side);
        while (Server.getInstance().online) {

            for (ChatClient client: clients) {
                synchronized (client){
                    if(client.hasMessage()){
                        client.notify();
                    }
                }
            }

        }
        Logger.getLogger().logging("HandleThread stopped ", Logger.LogLevel.DEBUG, Server.getInstance().side);
    }
}
