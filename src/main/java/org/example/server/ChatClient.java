package org.example.server;

import org.example.common.ChatConsumer;
import org.example.common.Logger;
import org.example.common.Side;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static org.example.server.ConnectThread.clients;

public class ChatClient extends ChatConsumer implements Runnable {

    private final PrintWriter out;
    private final Scanner in;
    private final Socket socket;
    private boolean online;


    public ChatClient(Socket socket) throws IOException {
        this.socket = socket;
        name = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new Scanner(socket.getInputStream());
        online = true;
    }

    @Override
    public void sendMessage(String message) {
        out.println(message);
    }

    @Override
    public Side side() {
        return Server.getInstance().side;
    }

    public boolean hasMessage() {
        if(in.ioException() != null){
            if(online){
                close();
            }
            return false;
        }

        try{
            return in.hasNext();
        }catch (IllegalStateException e){
            return false;
        }
    }

    @Override
    public void run() {
        try {

            Logger.getLogger().logging("[Connection] <" + name + "> " + "connected.", Logger.LogLevel.DEBUG, side());

            ChatThread.messageQueue.add(name + " joined to the chat.");

            while (online) {
                synchronized (this) {

                    if (!hasMessage()) this.wait();

                    String clientMessage = in.nextLine();

                    Logger.getLogger().logging("[HandledMassage] > " + "{"+ name + "} " + clientMessage , Logger.LogLevel.DEBUG, side());

                    if (clientMessage.equals("/exit")) {
                        close();
                        return;
                    }

                    ChatThread.messageQueue.add(name + ": " + clientMessage);
                }

            }
        } catch (InterruptedException ex) {
            if(online && name != null) {
                Logger.getLogger().logging("Warning user with name " + name + " was disappeared", Logger.LogLevel.INFO, side());
            }
        }
    }

    public void close() {
        try {
            online = false;
            clients.remove(this);
            ChatThread.messageQueue.add(name + " leave from the chat.");
            in.close();
            out.close();

            socket.close();
            Logger.getLogger().logging("[Connection] <" + name + "> " + "disconnected.", Logger.LogLevel.DEBUG, side());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
