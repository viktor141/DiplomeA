package org.example.server;

import org.example.common.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.CopyOnWriteArraySet;

public class ConnectThread implements Runnable {

    public static CopyOnWriteArraySet<ChatClient> clients = new CopyOnWriteArraySet<>();
    private static ServerSocket serverSocket;
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(Server.getInstance().PORT);

            Logger.getLogger().logging("ConnectionThread started", Logger.LogLevel.DEBUG, Server.getInstance().side);

            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();

                ChatClient client = new ChatClient(clientSocket);



                clients.add(client);


                new Thread(Server.getInstance().clientsThreads, client).start();

            }

        } catch (SocketException ignored){

        }catch (IOException e) {
            e.printStackTrace();
        }

        Logger.getLogger().logging("ConnectionThread stopped", Logger.LogLevel.INFO, Server.getInstance().side);
    }

    public static void closeConnection() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
